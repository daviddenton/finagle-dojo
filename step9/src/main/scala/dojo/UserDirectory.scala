package dojo

import com.twitter.finagle.Service
import com.twitter.util.Future

case class UnknownId(id: Int) extends Exception(s"the user $id is unknown")

class UserDirectory extends Service[Int, String] {

  private val idToName = Map(1 -> "rita", 2 -> "bob", 3 -> "sue")

  override def apply(request: Int): Future[String] = idToName.get(request)
    .map(name => Future(name))
    .getOrElse(Future.exception(UnknownId(request)))
}