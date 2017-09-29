package dojo

import com.twitter.finagle.util.DefaultTimer
import com.twitter.util.{Await, Duration, Future}

/**
  * Use this object as a place to run the code as it's created
  */
object RunnableEnvironment extends App {


  def run[A](f: => Future[A]): Unit = {
    println(
      Await.result(
        Future.sleep(Duration.fromMilliseconds(300))(DefaultTimer)
          .flatMap[A](_ => f)
          .handle { case e => "Failure: " + e.getMessage }))
  }

  new UserDirectoryServer(9000).start()
  new UserDirectoryServer(9001).start()
  new UserDirectoryServer(9002).start()
  new SecurityServer(8000, 9000, 9001, 9002).start()

  val client = new SecurityClient(8000)

  while(true) {
    run(client.access(1))
    run(client.access(2))
    run(client.access(3))
    run(client.access(5))
  }
}
