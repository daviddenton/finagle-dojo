import com.twitter.finagle.{Security, Service}
import com.twitter.util.{Await, Future}

object SecurityHarness extends App {

  Security.serve("localhost:8000", new Service[String, String] {
    override def apply(request: String) = Future(request)
  })

  val client = Security.newService("localhost:8000")

  println(Await.result(client("hello")))
}
