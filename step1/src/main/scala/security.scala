/**
  * 1. Implement a async DirectoryService using the core Finagle API, which takes an Int and looks up a corresponding name.
  * If there is no name found, throw an UnknownId
  */

case class UnknownId(id: Int) extends Exception(s"the user $id is unknown")
