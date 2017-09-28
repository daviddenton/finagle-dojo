package dojo

import com.twitter.finagle.{Http, ListeningServer}

class SecurityServer(port: Int, userDirectoryPorts: Int*) {

  private val userDirectory = new UserDirectoryClient(userDirectoryPorts: _*)

  def start(): ListeningServer = {
    val httpSecurityCheck = new Audit(s"SecurityServer:$port")
      .andThen(new ConvertMessage())
      .andThen(new SecurityCheck(userDirectory))

    Http.serve(s"localhost:$port", httpSecurityCheck)
  }
}