package dojo

import com.twitter.util.{Await, Future}

/**
  * Use this object as a place to run the code as it's created
  */
object RunnableEnvironment extends App {

  def run(f: Future[_]): Unit = println(Await.result(f.handle { case e => "Failure: " + e.getMessage }))

  new UserDirectoryServer(9000).start()
  new UserDirectoryServer(9001).start()
  new UserDirectoryServer(9002).start()
  new SecurityServer(8000, 9000, 9001, 9002).start()

  val client = new SecurityClient(8000)
  run(client.access(1))
  run(client.access(2))
  run(client.access(3))
  run(client.access(5))
}
