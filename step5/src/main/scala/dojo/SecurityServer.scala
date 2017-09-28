package dojo

import com.twitter.finagle.{Http, ListeningServer}

class SecurityServer(port: Int) {
  def start(): ListeningServer = {
    val service = new ConvertMessage().andThen(
      new SecurityCheck(new UserDirectory())
    )

    Http.serve("localhost:8000", service)
  }
}