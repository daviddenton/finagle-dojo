package dojo

import com.twitter.finagle.{Service, SimpleFilter}

/**
  * Change this Filter to audit the identity of the server which is running.
  */
class Audit[In, Out] extends SimpleFilter[In, Out] {
  override def apply(request: In, service: Service[In, Out]) = {
    val start = System.currentTimeMillis()
    service(request)
      .onSuccess {
        result => println(s"Looked up $request. Got $result in ${System.currentTimeMillis() - start}ms")
      }
      .onFailure {
        result => println(s"Request for $request failed. Got $result in ${System.currentTimeMillis() - start}ms")
      }
  }
}