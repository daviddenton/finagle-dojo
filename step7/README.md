### Step 7

- Write a Finagle Filter, ConvertUserMessage, to adapt between HTTP request/responses and the UserDirectory interface
- Implement an async domain client, UserDirectoryClient, which talks to the remote UserDirectoryServer.
- Implement a class, UserDirectoryServer, which mounts the UserDirectory into a running HTTP Finagle server.
- Adapt SecurityCheck to use the UserDirectoryClient
- Adapt SecurityServer to use the UserDirectoryClient