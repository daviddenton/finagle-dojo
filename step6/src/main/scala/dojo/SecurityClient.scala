package dojo

/**
  * Implement an async domain client, SecurityClient, which talks to the remote SecurityServer using the Result
  * classes to indicate the result
  */

sealed class Result

case class Granted(name: String) extends Result

case object Denied extends Result
