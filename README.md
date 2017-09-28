Finagle Dojo
============

#### Introduction
This is a set of exercises designed to teach the way in which Finagle works. The project consists of a number of "step" 
modules. 

1. The steps follow on from each other - each is an independent source tree which contains the answer to the previous step.
1. Each folder contains the instructions for that step - they are scattered around the various files, so check them all!
1. In the test folder, there is a RunnableEnvironment app which is designed to be used as an exercise yard for the code you've written. Feel free to download Scalatest instead an exercise it that way! :)

#### Finagle Concepts
There are 3 main concepts used by Finagle:

##### Service
The abstract class `com.twitter.finagle.Service[Req, Resp]` represents an asynchronous operation, generified by it's input and output types.


##### Filter
The abstract class `com.twitter.finagle.Filter[ReqIn, RepOut, ReqOut, RepIn]` is a layer that wraps a `Service` to provide pre and post operation processing. This is used for:
1. Message transformation - from `ReqIn` to `ReqOut` and from `RepIn` to `RepOut`
<img src="https://prismic-io.s3.amazonaws.com/lunatech/458f8af9cbc1cacaddc01c6cbe80b347cb05de28_yue2.png"/>

2. Application agnostic concerns such as retrying/circuits/auditing & latency monitoring
    
