package dojo

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.service.RetryFilter
import com.twitter.finagle.util.DefaultTimer
import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Duration, Return, Try}

object ResilientHttp {
  val backOff = Stream(Duration.fromMilliseconds(500), Duration.fromMilliseconds(1000))

  def newService(ports: Int*): Service[Request, Response] = {
    RetryFailures(backOff)
      .andThen(Http.newService(ports.map("localhost:" + _).mkString(",")))
  }
}

object RetryFailures {
  private val shouldRetry: PartialFunction[(Request, Try[Response]), Boolean] = {
    case (_, Return(rep)) => rep.status.code != 200
  }

  private implicit val t = DefaultTimer

  def apply(backoff: Stream[Duration]): RetryFilter[Request, Response] = RetryFilter(backoff)(shouldRetry)
}
