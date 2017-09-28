package dojo

import com.twitter.util.Await

object SecurityApp extends App {
  new SecurityServer(8000).start()

  println(Await.result(new SecurityClient(8000).access(1)))
  println(Await.result(new SecurityClient(8000).access(2)))
  println(Await.result(new SecurityClient(8000).access(5)))
}
