package dojo

import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future

import scala.util.Random

class BadBehaviour extends SimpleFilter[Request, Response] {

  private def shouldThrow() = Random.nextInt(10) > 2

  override def apply(request: Request, service: Service[Request, Response]): Future[Response] =
    if (shouldThrow()) Future(Response(Status.InternalServerError)) else service(request)
}