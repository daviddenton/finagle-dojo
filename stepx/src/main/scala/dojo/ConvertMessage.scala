package dojo

import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finagle.{Filter, Service}
import com.twitter.util.Future

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