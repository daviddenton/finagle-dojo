package dojo

import com.twitter.finagle.Http
import com.twitter.finagle.http.{Method, Request}
import com.twitter.util.{Await, Future}

/**
  * Use this object as a place to run the code as it's created
  */
object RunnableEnvironment extends App {

  def run(f: Future[_]): Unit = println(Await.result(f.handle { case e => "Failure: " + e.getMessage }))

  def requestFor(id: Int) = {
    val request = Request(Method.Post, "/")
    request.contentString = id.toString
    request
  }

  new SecurityServer(8000).start()

  val client = Http.newService("localhost:8000")

  run(client(requestFor(1)).map(_.contentString))
  run(client(requestFor(2)).map(_.contentString))
  run(client(requestFor(3)).map(_.contentString))
  run(client(requestFor(5)).map(_.contentString))
}
