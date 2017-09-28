package dojo

import com.twitter.finagle.{Http, ListeningServer}

class SecurityServer(port: Int, userDirectoryPort: Int) {

  private val userDirectory = Http.newService(s"localhost$userDirectoryPort")
  private val client = new UserDirectoryClient(userDirectoryPort)

  def start(): ListeningServer = {
    val httpSecurityCheck = new Audit()
      .andThen(new ConvertMessage())
      .andThen(new SecurityCheck(client))

    Http.serve(s"localhost:$port", httpSecurityCheck)
  }
}