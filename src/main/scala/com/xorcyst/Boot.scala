package com.xorcyst

import akka.actor.{ActorSystem, Props}
import com.typesafe.config._
import akka.io.IO
import spray.can.Http

object Boot extends App {
  val config = ConfigFactory.load()
  implicit val system = ActorSystem(config.getString("service.system"))
  val service = system.actorOf(Props[AppStoreServiceActor], config.getString("service.name"))
  IO(Http) ! Http.Bind(service, interface = config.getString("service.host"), port = config.getInt("service.port"))
}