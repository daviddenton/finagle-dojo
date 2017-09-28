package dojo

/**
  * Write a Finagle Filter to adapt between HTTP request/responses and the EntryAttempt/AccessResult classes:
  * a. The incoming HTTP message will have with the User id in the body.
  * b. The the result object should have an appropriate response code, and the message or the result in the body
  * c. The Filter should also return an appropriate HTTP response when the body is malformed.
  */

