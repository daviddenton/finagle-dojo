package dojo

/**
  * Implement an object with a function which creates an HTTP Client that is configured to:
  * 1. Load balance between multiple HTTP server instances.
  * 2. Retries HTTP 500 requests at backoff periods of 500ms and then 1s.
  *
  * Use this client in the UserDirectoryClient.
  */
