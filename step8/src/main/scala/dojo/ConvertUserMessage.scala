package dojo

import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finagle.{Filter, Service}
import com.twitter.util.Future

class ConvertUserMessage extends Filter[Request, Response, Int, String] {
  override def apply(request: Request, service: Service[Int, String]) = {
    Future(request.contentString.toInt)
      .flatMap(service)
      .map {
        accessResult =>
          responseWithContent(Status.Ok, accessResult)
      }
      .handle {
        case _: UnknownId => Response(Status.NotFound)
      }
  }

  private def responseWithContent(status: Status, name: String) = {
    val response = Response(status)
    response.contentString = name
    response
  }
}

