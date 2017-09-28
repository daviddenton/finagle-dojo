package dojo

import com.twitter.finagle.Service
import com.twitter.util.Future

/**
  * Adapt this class to use the UserDirectoryClient.
  */

case class EntryAttempt(id: Int)

case class AccessResult(name: String, granted: Boolean, message: String)

class SecurityCheck(userDirectory: UserDirectory) extends Service[EntryAttempt, AccessResult] {
  override def apply(request: EntryAttempt): Future[AccessResult] = userDirectory(request.id)
    .map(AccessResult(_, true, "success"))
    .handle {
      case e: UnknownId => AccessResult("unknown", false, e.getMessage)
    }
}
