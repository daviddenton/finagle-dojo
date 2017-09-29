package dojo

import com.twitter.finagle.{Http, ListeningServer}

class SecurityServer(port: Int, userDirectoryPort: Int) {

  private val userDirectory = new UserDirectoryClient(userDirectoryPort)

  def start(): ListeningServer = {
    val httpSecurityCheck = new Audit()
      .andThen(new ConvertMessage())
      .andThen(new SecurityCheck(userDirectory))

    Http.serve(s"localhost:$port", httpSecurityCheck)
  }
}