package dojo

/**
  * Create a domain client which talks to the remote SecurityServer.
  */

sealed class Result

case class Granted(name: String) extends Result

case object Denied extends Result
