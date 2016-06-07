package com.github.uryyyyyyy.akka.http.websocket

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.util.{ Success, Failure }

object Boot extends App {
  implicit val system = ActorSystem()
  import system.dispatcher
  implicit val materializer = ActorMaterializer()

  val service = new Webservice

  val binding = Http().bindAndHandle(service.route, interface = "localhost", port = 8080)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  binding.onComplete {
    case Success(binding) ⇒
      val localAddress = binding.localAddress
      println(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
    case Failure(e) ⇒
      println(s"Binding failed with ${e.getMessage}")
      system.terminate()
  }
}
