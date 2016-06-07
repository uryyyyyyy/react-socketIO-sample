package com.github.uryyyyyyy.akka.http.websocket

import akka.NotUsed
import akka.actor._
import akka.stream.OverflowStrategy
import akka.stream.scaladsl._
import com.github.uryyyyyyy.akka.http.websocket.Chat.{NewParticipant, ParticipantLeft, ReceivedMessage}


object Protocol {
  sealed trait Message
  case class ChatMessage(sender: String, message: String) extends Message
  case class Joined(member: String, allMembers: Seq[String]) extends Message
  case class Left(member: String, allMembers: Seq[String]) extends Message
}

class MyActor extends Actor {

  var subscribers = Set.empty[(String, ActorRef)]

  def receive: Receive = {
    case NewParticipant(name, subscriber) ⇒
      context.watch(subscriber)
      subscribers += (name -> subscriber)
      dispatch(Protocol.Joined(name, members))
    case msg: ReceivedMessage      ⇒ dispatch(msg.toChatMessage)
    case msg: Protocol.ChatMessage ⇒ dispatch(msg)
    case ParticipantLeft(person) ⇒
      val entry @ (_, ref) = subscribers.find(_._1 == person).get
      // report downstream of completion, otherwise, there's a risk of leaking the
      // downstream when the TCP connection is only half-closed
      ref ! Status.Success(Unit)
      subscribers -= entry
      dispatch(Protocol.Left(person, members))
    case Terminated(sub) ⇒
      // clean up dead subscribers, but should have been removed when `ParticipantLeft`
      subscribers = subscribers.filterNot(_._2 == sub)
  }
  def sendAdminMessage(msg: String): Unit = dispatch(Protocol.ChatMessage("admin", msg))
  def dispatch(msg: Protocol.Message): Unit = subscribers.foreach(_._2 ! msg)
  def members = subscribers.map(_._1).toSeq
}

class Chat(chatActor: ActorRef){

  def chatFlow(sender: String): Flow[String, Protocol.Message, Any] = {
    val flow1: Flow[String, Chat.ChatEvent, NotUsed] = Flow[String].map(ReceivedMessage(sender, _))
    val sink: Sink[Chat.ChatEvent, NotUsed] = Sink.actorRef[Chat.ChatEvent](chatActor, ParticipantLeft(sender))

    // The counter-part which is a source that will create a target ActorRef per
    // materialization where the chatActor will send its messages to.
    // This source will only buffer one element and will fail if the client doesn't read
    // messages fast enough.

    val source: Source[Protocol.ChatMessage, ActorRef] = Source.actorRef[Protocol.ChatMessage](1, OverflowStrategy.fail)

    val source2 = source.mapMaterializedValue(v => chatActor ! NewParticipant(sender, v))

    Flow.fromSinkAndSource(flow1.to(sink), source2)
  }
  def injectMessage(message: Protocol.ChatMessage): Unit = chatActor ! message // non-streams interface
}

object Chat {

  def createChatActor(chatActor: ActorRef): Chat = {
    new Chat(chatActor)
  }

  def create(system: ActorSystem): Chat = {
    // The implementation uses a single actor per chat to collect and distribute
    // chat messages. It would be nicer if this could be built by stream operations
    // directly.
    val chatActor = system.actorOf(Props[MyActor])

    // Wraps the chatActor in a sink. When the stream to this sink will be completed
    // it sends the `ParticipantLeft` message to the chatActor.
    // FIXME: here some rate-limiting should be applied to prevent single users flooding the chat

    createChatActor(chatActor)
  }

  sealed trait ChatEvent
  case class NewParticipant(name: String, subscriber: ActorRef) extends ChatEvent
  case class ParticipantLeft(name: String) extends ChatEvent
  case class ReceivedMessage(sender: String, message: String) extends ChatEvent {
    def toChatMessage: Protocol.ChatMessage = Protocol.ChatMessage(sender, message)
  }
}
