package dojo

import com.twitter.finagle.{Service, SimpleFilter}

class Audit[In, Out](id: String) extends SimpleFilter[In, Out] {
  override def apply(request: In, service: Service[In, Out]) = {
    val start = System.currentTimeMillis()
    service(request)
      .onSuccess {
        result => println(s"$id: Looked up $request. Got $result in ${System.currentTimeMillis() - start}ns")
      }
      .onFailure {
        result => println(s"$id: Request for $request failed. Got $result in ${System.currentTimeMillis() - start}ms")
      }
  }
}