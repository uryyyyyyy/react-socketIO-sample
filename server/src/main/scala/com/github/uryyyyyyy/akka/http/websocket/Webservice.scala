package com.github.uryyyyyyy.akka.http.websocket

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives
import akka.stream.Materializer
import akka.stream.scaladsl.Flow

class Webservice(implicit fm: Materializer, system: ActorSystem) extends Directives {
  val theChat = Chat.create(system)

  def route =
    get {
      pathSingleSlash(getFromFile("../client/public/index.html")) ~
        path("bundle.js")(getFromFile("../client/public/bundle.js")) ~
        path("sample.mp3")(getFromFile("../client/public/sample.mp3")) ~
        path("chat") {
          parameter('name) { name ⇒
            println(s"socket request: $name")
            handleWebSocketMessages(webSocketChatFlow(name))
          }
        }
    }

  def webSocketChatFlow(sender: String): Flow[Message, Message, Any] =
    Flow[Message]
      .collect { case TextMessage.Strict(msg) ⇒ msg}
      .via(theChat.chatFlow(sender)) // ... and route them through the chatFlow ...
      .collect { case msg: Protocol.Message ⇒ TextMessage.Strict(upickle.write(msg))}
}
