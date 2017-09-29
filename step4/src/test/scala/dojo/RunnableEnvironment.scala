package dojo

import com.twitter.util.{Await, Future}

/**
  * Use this object as a place to run the code as it's created
  */
object RunnableEnvironment extends App {

  def run(f: Future[_]): Unit = println(Await.result(f.handle { case e => "Failure: " + e.getMessage }))

  val auditingSecurityCheck = new Audit().andThen(new SecurityCheck(new UserDirectory()))

  run(auditingSecurityCheck(EntryAttempt(1)))
  run(auditingSecurityCheck(EntryAttempt(2)))
  run(auditingSecurityCheck(EntryAttempt(3)))
  run(auditingSecurityCheck(EntryAttempt(5)))
}
