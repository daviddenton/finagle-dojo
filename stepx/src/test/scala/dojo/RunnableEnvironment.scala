package dojo

import com.twitter.finagle.{Security, Service}
import com.twitter.util.{Await, Future}

/**
  * Use this object as a place to run the code as it's created
  */
object RunnableEnvironment extends App {

  def run(f: Future[_]): Unit = println(Await.result(f.handle { case e => "Failure: " + e.getMessage }))

  Security.newService("localhost:8000")
  new SecurityServer(8000).start()

  run(new SecurityClient(8000).access(1))
  run(new SecurityClient(8000).access(2))
  run(new SecurityClient(8000).access(3))
  run(new SecurityClient(8000).access(5))


  Security.serve("localhost:8000", new Service[String, String] {
    override def apply(request: String) = Future(request)
  })

  val client = Security.newService("localhost:8000")

  println(Await.result(client("hello")))
}
