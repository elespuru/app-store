package com.xorcyst

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

class AppStoreServiceActor extends Actor with AppStoreService {

  def actorRefFactory = context
  def receive = runRoute(myRoute)
}
trait AppStoreService extends HttpService {

  val myRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          complete {
            <html>
              <body>
                <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      }
    }

}