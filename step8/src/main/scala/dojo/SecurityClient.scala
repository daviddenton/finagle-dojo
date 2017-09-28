package dojo

import com.twitter.finagle.Http
import com.twitter.finagle.http.{Method, Request, Response, Status}
import com.twitter.util.Future

sealed class Result

case class Granted(name: String) extends Result

case object Denied extends Result

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
