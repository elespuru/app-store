package com.xorcyst

import scala.concurrent.duration._
import akka.actor._
import akka.pattern.ask
import spray.routing.{HttpService, RequestContext}
import spray.routing.directives.CachingDirectives
import spray.can.server.Stats
import spray.can.Http
import spray.httpx.marshalling.Marshaller
import spray.httpx.encoding.Gzip
import spray.util._
import spray.http._
import MediaTypes._
import CachingDirectives._

class AppStoreServiceActor extends Actor with AppStoreService {
  def actorRefFactory = context
  def receive = runRoute(serviceRoutes)
}

trait AppStoreService extends HttpService {
  implicit def executionContext = actorRefFactory.dispatcher

  val serviceRoutes = {
    get {
      pathSingleSlash {
        complete(index)
      } ~
      path("stats") {
        complete {
          actorRefFactory.actorSelection("/user/IO-HTTP/listener-0")
            .ask(Http.GetStats)(1.second)
            .mapTo[Stats]
        }
      }
    } ~
    (post | parameter('method ! "post")) {
      path("stop") {
        complete(index)
      }
    }
  }

  lazy val index =
    <html>
      <body>
        <h1>app-store API</h1>
        <p>Defined resources:</p>
        <ul>
          <li><a href="/stats">/stats</a></li>
        </ul>
      </body>
    </html>

  implicit val statsMarshaller: Marshaller[Stats] =
    Marshaller.delegate[Stats, String](ContentTypes.`text/plain`) { stats =>
      "Uptime                : " + stats.uptime.formatHMS + '\n' +
      "Total requests        : " + stats.totalRequests + '\n' +
      "Open requests         : " + stats.openRequests + '\n' +
      "Max open requests     : " + stats.maxOpenRequests + '\n' +
      "Total connections     : " + stats.totalConnections + '\n' +
      "Open connections      : " + stats.openConnections + '\n' +
      "Max open connections  : " + stats.maxOpenConnections + '\n' +
      "Requests timed out    : " + stats.requestTimeouts + '\n'
    }

  def in[U](duration: FiniteDuration)(body: => U): Unit =
    actorSystem.scheduler.scheduleOnce(duration)(body)
}