package com.twitter.finagle

/**
  * **WARNING: VERY CHALLENGING! :)**
  *
  * Implement the Security protocol which provides Server and Client objects. This protocol will sit
  * on top of finagle-core and will be able to take advantage of all the fault-tolerance features of the
  * platform, such as Retry mechanics, service discovery and Load Balancing.
  *
  * Hints:
  * - You will need to build the Netty pipeline and transports/listeners. (currently those classes are private in this
  * package, so the protocol needs to be in `com.twitter.finagle`).
  * - Look at the existing finagle protocol sub-projects to see how they do it.
  * - There is a very simple example protocol at: https://gist.github.com/daviddenton/30c07ab21efc11691a75ddd77c4015ef
  */

