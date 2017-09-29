package dojo

import com.twitter.finagle.{Http, ListeningServer}

/**
  * Introduce the BadBehaviourFilter to this server.
  */
class UserDirectoryServer(port: Int) {

  def start(): ListeningServer = {
    val httpSecurityCheck = new Audit()
      .andThen(new ConvertUserMessage())
      .andThen(new UserDirectory())

    Http.serve(s"localhost:$port", httpSecurityCheck)
  }
}
