package dojo

import com.twitter.finagle.{Http, ListeningServer}

/**
  * Adapt this class to use the UserDirectoryClient.
  */
class SecurityServer(port: Int) {

  def start(): ListeningServer = {
    val httpSecurityCheck = new Audit()
      .andThen(new ConvertMessage())
      .andThen(new SecurityCheck(new UserDirectory()))

    Http.serve(s"localhost:$port", httpSecurityCheck)
  }
}