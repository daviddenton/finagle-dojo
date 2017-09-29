package dojo

import com.twitter.finagle.Http
import com.twitter.finagle.http.{Method, Request, Response, Status}
import com.twitter.util.Future

/**
  * Adapt this client to load balance between multiple server instances.
  */
class UserDirectoryClient(ports: Int*) {

  private val client =
    Http.newService(ports.map("localhost:" + _).mkString(","))

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
