package dojo

import com.twitter.finagle.{Service, SimpleFilter}

class Audit[In, Out] extends SimpleFilter[In, Out] {
  override def apply(request: In, service: Service[In, Out]) = {
    val start = System.nanoTime()
    service(request)
      .onSuccess {
        result => println(s"Looked up $request. Got $result in ${System.nanoTime() - start}ns")
      }
      .onFailure {
        result => println(s"Request for $request failed. Got $result in ${System.nanoTime() - start}ns")
      }
  }
}