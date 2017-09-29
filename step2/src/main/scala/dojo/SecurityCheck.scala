package dojo

/**
  * Implement a async Service, SecurityCheck, using the core Finagle API, which uses the DirectoryService to lookup the
  * name of the user from the incoming EntryAttempt and then grant them access by returning an AccessResult. In the
  * case of a failure, rescue the exception and return a appropriate result.
  */

case class EntryAttempt(id: Int)

case class AccessResult(name: String, granted: Boolean, message: String)

