import com.twitter.finagle.Service
import com.twitter.util.Future

case class UnknownId(id: Int) extends Exception(s"the user $id is unknown")

class DirectoryService extends Service[Int, String] {

  private val idToName = Map(1 -> "rita", 2 -> "bob", 3 -> "sue")

  override def apply(request: Int): Future[String] = idToName.get(request)
    .map(name => Future(name))
    .getOrElse( Future.exception(UnknownId(request)))
}

/**
  * 2. Implement a async SecurityService using the core Finagle API, which uses the DirectoryService to lookup the
  * name of the user from the incoming EntryAttempt and then grant them access by returning an AccessResult. In the
  * case of a failure, rescue the exception and return a appropriate result.
  */

case class EntryAttempt(id: Int)

case class AccessResult(name: String, granted: Boolean, message: String)

