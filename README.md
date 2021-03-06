Finagle Dojo
============

## Introduction

This is a set of exercises designed to teach the way in which Finagle works. The project consists of a number of "step" 
modules. 

1. The steps follow on from each other - each is an independent source tree which contains the answer to the previous step.
1. Each folder contains the instructions for that step - they are scattered around the various files, so check them all!
1. In the test folder, there is a `RunnableEnvironment` app which is designed to be used as an exercise yard for the code you've written. Feel free to download Scalatest instead an exercise it that way! :)

The problem space for this dojo is to implement a distributed system which provides a simple Security mechanism. The requirements for the system are simply to look up a 
username for a provided user id. If the name can be found in a directory service, then access is granted. If the name cannot be found, access is denied. 

## Finagle Concepts

There are 3 main concepts used by Finagle:

### Future

The abstract class `com.twitter.util.Future[A]` represents the result of an asynchronous operation, generified by it's result type. Note that Twitter Futures are NOT Scala Futures and have a different API. 
[Here](https://stackoverflow.com/questions/32987855/what-are-advantages-of-a-twitter-future-over-a-scala-future) is a SO answer detailing the differences.

A Future has 2 end states - Value, and Exception
`Future` contains the following interesting methods:
1. `Future.apply[T]() : Future[T]` - wrap a value in a successful Future
1. `Future.exception() : Future[T]` - wrap a exception in a failed Future
1. `futureT.map[Y](fn: T => Y): Future[Y]` - transform the value inside the Future when fn returns a value
1. `futureT.flatMap[Y](fn: T => Future[Y]): Future[Y]` - transform the value inside the Future when fn returns a Future
1. `futureT.handle[Y](fn: PartialFunction[Throwable, Y): Future[Y]` - perform an operation on a failed future when fn returns a value
1. `futureT.rescue[Y](fn: PartialFunction[Throwable, Future[Y]): Future[Y]` - perform an operation on a failed future when fn returns a Future
1. `futureT.onSuccess(fn: A => Unit): Future[T]` - attach an on-success listener to Future 
1. `futureT.onFailure(fn: A => Unit): Future[T]` - attach an on-failure listener to Future

Example:
```scala
val result: String = Await.result(
  Future.value("content")
    .map(v => v + v)
    .flatMap(v => Future(v))
    .handle {
      case _: Exception => "failed"
    }
)
```

### Service

The abstract class `com.twitter.finagle.Service[Req, Resp]` represents a service providing an asynchronous operation, generified by it's input and output types.

Here is the only method that needs to be implemented when extending `Service`:
```scala
def apply(request: Req): Future[Rep]
```

Example:
```scala
val service = new Service[Request, Response] {
  override def apply(request: Request): Future[Response] = {
    val response = Response(Status.Ok)
    response.content = request.content
    Future(response)
  }
}
```

### Filter

The abstract class `com.twitter.finagle.Filter[ReqIn, RepOut, ReqOut, RepIn]` is a layer that wraps a `Service` to provide pre and post operation processing. This is used for:
1. Message transformation - from `ReqIn` to `ReqOut` and from `RepIn` to `RepOut`
<img src="https://prismic-io.s3.amazonaws.com/lunatech/458f8af9cbc1cacaddc01c6cbe80b347cb05de28_yue2.png"/>

2. Application agnostic concerns such as retrying/circuits/auditing & latency monitoring

Here is the only method that needs to be implemented when extending `Filter`:
```scala
def apply(request: ReqIn, service: Service[ReqOut, RepIn]): Future[RepOut]
```
    
Example:
```scala
val addType = new Filter[Request, Response, Request, Response] {
  override def apply(request: Request, service: Service[Request, Response]) = {
    service(request).map(resp => {
      resp.headerMap("Content-type") = "application/json"
      resp
    })
  }
}
```
## Other resources

- [This](https://lunatech.com/blog/WDwEjiUAACQAdhdw/an-introduction-of-finagle-by-example) blog post is a very good introduction.
- [Finagle guide](https://twitter.github.io/finagle/guide/)