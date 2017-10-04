### Step 8

- Implement an Http Filter to randomly return an HTTP 500 from any request.
- Introduce the BadBehaviourFilter to UserDirectoryServer.
- Implement an object ResilientHttpClient with a function which creates an HTTP Client that is configured to:
      * Load balance between multiple HTTP server instances.
      * Retries HTTP 500 requests at backoff periods of 500ms and then 1s.
      * Use this client in the UserDirectoryClient.   
      
- Use the new ResilientHttpClient in UserDirectoryClient.   