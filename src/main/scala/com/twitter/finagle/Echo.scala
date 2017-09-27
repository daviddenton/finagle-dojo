package com.twitter.finagle

import java.net.SocketAddress

import com.twitter.finagle.client.{StackClient, StdStackClient, Transporter}
import com.twitter.finagle.dispatch.{SerialClientDispatcher, SerialServerDispatcher}
import com.twitter.finagle.netty4.{Netty4Listener, Netty4Transporter}
import com.twitter.finagle.server.{Listener, StackServer, StdStackServer}
import com.twitter.finagle.transport.Transport
import io.netty.channel.ChannelPipeline
import io.netty.handler.codec.string.{StringDecoder, StringEncoder}

case object EchoPipeline {
  def apply(channelPipeline: ChannelPipeline): Unit = channelPipeline
    .addLast(new StringEncoder())
    .addLast(new StringDecoder())
}

object Echo extends Client[String, String] with Server[String, String] {

  case class Client(stack: Stack[ServiceFactory[String, String]] = StackClient.newStack, params: Stack.Params = StackClient.defaultParams)
    extends StdStackClient[String, String, Client] {

    protected type In = String
    protected type Out = String

    protected def copy1(stack: Stack[ServiceFactory[String, String]], params: Stack.Params): Client = copy(stack, params)

    protected def newTransporter(addr: SocketAddress): Transporter[String, String] =
      Netty4Transporter.raw[String, String](EchoPipeline(_), addr, StackClient.defaultParams)

    protected def newDispatcher(transport: Transport[String, String]) = new SerialClientDispatcher(transport)
  }

  def newService(dest: Name, label: String): Service[String, String] = Client().newService(dest, label)

  def newClient(dest: Name, label: String): ServiceFactory[String, String] = Client().newClient(dest, label)

  case class Server(
                     stack: Stack[ServiceFactory[String, String]] = StackServer.newStack,
                     params: Stack.Params = StackServer.defaultParams
                   ) extends StdStackServer[String, String, Server] {
    protected type In = String
    protected type Out = String

    protected def copy1(
                         stack: Stack[ServiceFactory[String, String]] = this.stack,
                         params: Stack.Params = this.params
                       ): Server = copy(stack, params)

    protected def newListener(): Listener[String, String] = new Netty4Listener(EchoPipeline(_), params)

    protected def newDispatcher(transport: Transport[String, String], service: Service[String, String]) = new SerialServerDispatcher(transport, service)
  }

  def serve(addr: SocketAddress, service: ServiceFactory[String, String]): ListeningServer = Server().serve(addr, service)
}
