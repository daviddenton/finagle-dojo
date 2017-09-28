package dojo

import com.twitter.util.{Await, Future}

/**
  * Use this object as a place to run the code as it's created
  */
object RunnableEnvironment extends App {

  def run(f: Future[_]): Unit = println(Await.result(f.handle { case e => "Failure: " + e.getMessage }))

  new UserDirectoryServer(9000).start()
  new SecurityServer(8000, 9000).start()

  run(new SecurityClient(8000).access(1))
  run(new SecurityClient(8000).access(2))
  run(new SecurityClient(8000).access(3))
  run(new SecurityClient(8000).access(5))
}
