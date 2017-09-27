package dojo

import com.twitter.finagle.http.{Method, Request, Response, Status}
import com.twitter.finagle.{Filter, Http, ListeningServer, Service}
import com.twitter.util.{Await, Future}

case class UnknownId(id: Int) extends Exception(s"the user $id is unknown")

class DirectoryService extends Service[Int, String] {

  private val idToName = Map(1 -> "rita", 2 -> "bob", 3 -> "sue")

  override def apply(request: Int): Future[String] = idToName.get(request)
    .map(name => Future(name))
    .getOrElse(Future.exception(UnknownId(request)))
}

case class EntryAttempt(id: Int)

case class AccessResult(name: String, granted: Boolean, message: String)

class SecurityService(directoryService: DirectoryService) extends Service[EntryAttempt, AccessResult] {
  override def apply(request: EntryAttempt): Future[AccessResult] = directoryService(request.id)
    .map(AccessResult(_, true, "success"))
    .handle {
      case e: UnknownId => AccessResult("unknown", false, e.getMessage)
    }
}

class ConvertMessage extends Filter[Request, Response, EntryAttempt, AccessResult] {
  override def apply(request: Request, service: Service[EntryAttempt, AccessResult]): Future[Response] =
    Future(EntryAttempt(request.contentString.toInt))
      .flatMap(service)
      .map {
        accessResult =>
          if (accessResult.granted) responseWithContent(Status.Ok, accessResult.name)
          else responseWithContent(Status.Forbidden, accessResult.message)
      }
      .handle {
        case _: NumberFormatException => responseWithContent(Status.BadRequest, request.contentString)
      }

  private def responseWithContent(ok: Status, name: String) = {
    val response = Response(ok)
    response.contentString = name
    response
  }
}

sealed class Result
case class Granted(name: String) extends Result
case object Denied extends Result

class SecuritySystem(port: Int) {
  def start(): ListeningServer = {
    val service = new ConvertMessage().andThen(
      new SecurityService(new DirectoryService())
    )

    Http.serve("localhost:8000", service)
  }
}

class SecurityClient(port: Int) {
  private val client = Http.newService("localhost:" + port)

  def access(id: Int): Future[Result] = {
    val request = Request(Method.Post, "/")
    request.contentString = id.toString
    client(request)
      .flatMap {
        resp: Response =>
          resp.status match {
            case Status.Ok => Future(Granted(resp.contentString))
            case Status.Forbidden => Future(Denied)
            case _ => Future.exception(new RuntimeException(s"error occured for id $id"))
          }
      }
  }
}

object SecurityApp extends App {
  new SecuritySystem(8000).start()

  println(Await.result(new SecurityClient(8000).access(1)))
  println(Await.result(new SecurityClient(8000).access(2)))
  println(Await.result(new SecurityClient(8000).access(5)))
}