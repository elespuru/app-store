package com.xorcyst

import spray.json.DefaultJsonProtocol
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import spray.http._
import HttpCharsets._
import MediaTypes._

case class Application(val name:String, val description:String, val author:String, val price:Double)

object ApplicationJsonProtocol extends DefaultJsonProtocol {
  implicit val applicationFormat = jsonFormat4(Application)
}
