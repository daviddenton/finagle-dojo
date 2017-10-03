###Stage 4

Write a Finagle Filter, ConvertMessage, to adapt between HTTP request/responses and the EntryAttempt/AccessResult classes:
  - The incoming HTTP message will have with the User id in the body.
  - The the result object should have an appropriate response code, and the message or the result in the body
  - The Filter should also return an appropriate HTTP response when the body is malformed.