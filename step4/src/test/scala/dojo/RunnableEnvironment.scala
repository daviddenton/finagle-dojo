package dojo

import com.twitter.finagle.http.{Method, Request}
import com.twitter.util.{Await, Future}

/**
  * Use this object as a place to run the code as it's created
  */
object RunnableEnvironment extends App {

  def run(f: Future[_]): Unit = println(Await.result(f.handle { case e => "Failure: " + e.getMessage }))

  val httpSecurityCheck = new ConvertMessage()
    .andThen(new SecurityCheck(new UserDirectory()))

  def requestFor(id: Int) = {
    val request = Request(Method.Post, "/")
    request.contentString = id.toString
    request
  }

  run(httpSecurityCheck(requestFor(1)).map(_.contentString))
  run(httpSecurityCheck(requestFor(2)).map(_.contentString))
  run(httpSecurityCheck(requestFor(3)).map(_.contentString))
  run(httpSecurityCheck(requestFor(5)).map(_.contentString))
}
