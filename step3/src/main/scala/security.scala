import com.twitter.finagle.Service
import com.twitter.util.Future

case class UnknownId(id: Int) extends Exception(s"the user $id is unknown")

class DirectoryService extends Service[Int, String] {

  private val idToName = Map(1 -> "rita", 2 -> "bob", 3 -> "sue")

  override def apply(request: Int): Future[String] = idToName.get(request)
    .map(name => Future(name))
    .getOrElse(Future.exception(UnknownId(request)))
}

case class EntryAttempt(id: Int)

case class AccessResult(name: String, granted: Boolean, message: String)

class SecurityService(directoryService: DirectoryService) extends Service[EntryAttempt, AccessResult] {
  override def apply(request: EntryAttempt): Future[AccessResult] = directoryService(request.id)
    .map(AccessResult(_, true, "success"))
    .handle {
      case e: UnknownId => AccessResult("unknown", false, e.getMessage)
    }
}

/**
  * 3. Write a Finagle Filter to adapt between HTTP request/responses and the EntryAttempt/AccessResult classes:
  * a. The incoming HTTP message will have with the User id in the body.
  * b. The the result object should have an appropriate response code, and the message or the result in the body
  * c. The Filter should also return an appropriate HTTP response when the body is malformed.
  */

