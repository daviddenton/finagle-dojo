/**
  * 1. Implement a async Service, DirectoryService, using the core Finagle API, which takes an Int and looks up a corresponding name.
  * If there is no name found, throw an UnknownId
  *
  * The known users are:
  * id 1 -> Rita
  * id 2 -> Sue
  * id 3 -> Bob
  */

case class UnknownId(id: Int) extends Exception(s"the user $id is unknown")
