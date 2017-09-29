package dojo

import java.util.concurrent.atomic.AtomicLong

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.service.RetryBudget
import com.twitter.finagle.{Http, Service}

object ResilientHttpClient {
  def newService(ports: Int*): Service[Request, Response] =
    Http.client
      .withRetryBudget(new UpToNRetries(2))
      .newService(ports.map("localhost:" + _).mkString(","))
}

class UpToNRetries(private val start: Long) extends RetryBudget {
  private val remaining = new AtomicLong(start)

  def deposit(): Unit = remaining.incrementAndGet()

  def tryWithdraw(): Boolean = if (balance > 0) {
    remaining.getAndDecrement()
    true
  } else false

  def balance: Long = remaining.get()
}
