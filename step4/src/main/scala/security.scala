import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finagle.{Filter, Service}
import com.twitter.util.Future

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

/**
  * 4. Mount the Service into a running HTTP Finagle server, and a domain client which we can use to call it remotely.
  */
