import com.twitter.finagle.{Echo, Service}
import com.twitter.util.{Await, Future}

object EchoTest extends App {

  Echo.serve("localhost:8000", new Service[String, String] {
    override def apply(request: String) = Future(request)
  })

  val client = Echo.newService("localhost:8000")

  println(Await.result(client("hello")))
}
