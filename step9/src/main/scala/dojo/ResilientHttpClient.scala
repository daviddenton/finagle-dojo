package dojo

import java.util.concurrent.atomic.AtomicLong

import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finagle.service.{RetryBudget, RetryFilter}
import com.twitter.finagle.util.DefaultTimer
import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Duration, Return}

object HttpRetry {
  private implicit val timer = DefaultTimer

  def apply(backoffDurations: Stream[Duration]) = RetryFilter[Request, Response](backoffDurations) {
    case (_, Return(r: Response)) if r.status == Status.InternalServerError => true
  }
}

object ResilientHttpClient {
  private val backoffDurations = Stream(Duration.fromMilliseconds(500), Duration.fromMilliseconds(1000))

  def newService(ports: Int*): Service[Request, Response] =
    HttpRetry(backoffDurations)
      .andThen(Http.newService(ports.map("localhost:" + _).mkString(",")))
}

class UpToNRetries(private val start: Long) extends RetryBudget {
  private val remaining = new AtomicLong(start)

  def deposit(): Unit = remaining.incrementAndGet()

  def tryWithdraw(): Boolean = if (balance > 0) {
    println("trying")
    remaining.decrementAndGet()
    true
  } else {
    println("rejecting")

    false
  }

  def balance: Long = {
    println("balance" + remaining.get())

    remaining.get()
  }
}
