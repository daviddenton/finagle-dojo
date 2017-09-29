package dojo

import com.twitter.finagle.{Http, ListeningServer}

class UserDirectoryServer(port: Int) {

  def start(): ListeningServer = {
    val httpSecurityCheck = new Audit(s"UserDirectoryServer:$port")
      .andThen(new ConvertUserMessage())
      .andThen(new UserDirectory())

    Http.serve(s"localhost:$port", httpSecurityCheck)
  }
}
