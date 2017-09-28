package dojo

import com.twitter.finagle.Service
import com.twitter.util.Future

case class EntryAttempt(id: Int)

case class AccessResult(name: String, granted: Boolean, message: String)

/**
  * Adapt this class to use the UserDirectoryClient.
  */

class SecurityCheck(userDirectory: UserDirectory) extends Service[EntryAttempt, AccessResult] {
  override def apply(request: EntryAttempt): Future[AccessResult] = userDirectory(request.id)
    .map(AccessResult(_, true, "success"))
    .handle {
      case e => AccessResult("unknown", false, e.getMessage)
    }
}
