package dojo

import com.twitter.finagle.http.{Method, Request, Response, Status}
import com.twitter.util.Future

class UserDirectoryClient(ports: Int*) {

  private val client = ResilientHttpClient.newService(ports: _*)

  def lookup(id: Int): Future[String] = {
    val request = Request(Method.Post, "/")
    request.contentString = id.toString
    client(request)
      .flatMap {
        resp: Response =>
          resp.status match {
            case Status.Ok => Future(resp.contentString)
            case _ => Future.exception(new RuntimeException(s"error occurred for id $id"))
          }
      }
  }
}
