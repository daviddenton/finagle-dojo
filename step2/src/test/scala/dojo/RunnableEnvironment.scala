package dojo

import com.twitter.util.{Await, Future}

/**
  * Use this object as a place to run the code as it's created
  */
object RunnableEnvironment extends App {

  def run(f: Future[_]): Unit = println(Await.result(f.handle { case e => "Failure: " + e.getMessage }))

  val userDirectory = new UserDirectory()

  run(userDirectory(1))
  run(userDirectory(2))
  run(userDirectory(3))
  run(userDirectory(5)) // blows up
}
