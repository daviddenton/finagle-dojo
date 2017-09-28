package dojo

import com.twitter.util.{Await, Future}

/**
  * Use this object as a place to run the code as it's created
  */
object RunnableEnvironment extends App {

  def run(f: Future[_]): Unit = println(Await.result(f.handle { case e => "Failure: " + e.getMessage }))

  val securityCheck = new SecurityCheck(new UserDirectory())

  run(securityCheck(EntryAttempt(1)))
  run(securityCheck(EntryAttempt(2)))
  run(securityCheck(EntryAttempt(3)))
  run(securityCheck(EntryAttempt(5)))
}
