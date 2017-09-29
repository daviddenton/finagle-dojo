package dojo

/**
  * A class to create an HTTP client that:
  * 1. Load balance between multiple server instances.
  * 2. Retries failed requests up to "n" times.
  * 3. Mark the node as down after 2 consecutive failed requests.
  *
  * Use this client in the UserDirectoryClient.
  */
