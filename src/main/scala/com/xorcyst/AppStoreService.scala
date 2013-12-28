package com.xorcyst

import scala.concurrent.duration._
import com.typesafe.config._
import akka.actor._
import akka.pattern.ask
import spray.routing.{HttpService, RequestContext}
import spray.routing.directives.CachingDirectives
import spray.can.Http._
import spray.http.HttpHeaders._
import spray.http.ContentTypes._
import spray.httpx.marshalling.Marshaller
import spray.util._
import spray.http._
import spray.json._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import DefaultJsonProtocol._
import ApplicationJsonProtocol._

class AppStoreServiceActor extends Actor with AppStoreService {
  def actorRefFactory = context
  def receive = runRoute(serviceRoutes)

}

trait AppStoreService extends HttpService {
  implicit def executionContext = actorRefFactory.dispatcher
  val nextAppId = { var i = 3; () => { i += 1; i } }
  val testOne = Application("AppOne", "TestAppOne", "SomePerson", 1.00)
  val testTwo = Application("AppTwo", "TestAppTwo", "SomeOtherPerson", 2.99)
  val testThree = Application("AppThree", "TestAppThree", "SomeNewPerson", 2.00)
  val binaryCatalog = collection.mutable.HashMap(1L -> "abcd", 2L -> "efgh")
  val applicationCatalog = collection.mutable.HashMap(1L -> testOne, 2L -> testTwo, 3L -> testThree)
  val protectedApplications = collection.immutable.List(1L, 2L)
  lazy val config = ConfigFactory.load()

  val serviceRoutes = {
    pathSingleSlash {
      get { getFromResource(config.getString("service.index")) }
    } ~
    path("apps") {
      post   { entity(as[Application]) { app => complete(postApp(app)) }} ~
      put    { entity(as[Application]) { app => complete(postApp(app)) }}
    } ~
    path("app" / LongNumber) { appId =>
      get    { complete(getApp(appId))    } ~
      put    { entity(as[Application]) { app => complete(putApp(appId, app)) }} ~
      delete { complete(deleteApp(appId)) }
    } ~
    path("app" / LongNumber / "binary") { appId =>
      get    { complete(downloadApp(appId)) } ~
      post   { complete(uploadApp(appId))   } ~
      put    { complete(uploadApp(appId))   }
    }
  }

  def downloadApp (appId:Long) : HttpResponse = {
    if (applicationCatalog.contains(appId)) {
      if (binaryCatalog.contains(appId)) {
        // TBD, not yet implemented
        return HttpResponse(status = 405)
      }
    }

    return HttpResponse(status = 404)
  }

  def uploadApp (appId:Long) : HttpResponse = {
    if (applicationCatalog.contains(appId)) {
        binaryCatalog(appId) = "TBD, not yet implemented"
        return HttpResponse(status = 405)
    } else {
      return HttpResponse(status = 404)
    }
  }

  def getApp (appId:Long) : HttpResponse = {
    if (applicationCatalog.contains(appId)) {
      return HttpResponse(status = 200,
                          entity = applicationCatalog.get(appId).get.toJson.toString)
    } else {
      return HttpResponse(status = 404)
    }
  }

  def putApp (appId:Long, app:Application) : HttpResponse = {
    if (!applicationCatalog.contains(appId)) {
      return HttpResponse(status = 404)
    } else {
      applicationCatalog(appId) = app
      return HttpResponse(status = 204)
    }
  }

  def postApp (app:Application) : HttpResponse = {
    val appId = nextAppId()
    // limit to 100 so the hosted in-mem collections can't get out of hand
    if (appId >= 100) {
      return HttpResponse(status = 403)
    }
    applicationCatalog(appId) = app
    return HttpResponse(status = 201,
                        entity = s"""{ "appId" : ${appId} }""")
  }

  def deleteApp (appId:Long) : HttpResponse = {
    if (applicationCatalog.contains(appId)) {
      if (protectedApplications.contains(appId)) {
        return HttpResponse(status = 403)

      } else {
        if (binaryCatalog.contains(appId)) {
          binaryCatalog.remove(appId)
        }
        applicationCatalog.remove(appId)
        return HttpResponse(status = 204)
      }

    } else {
      return HttpResponse(status = 404)
    }
  }
}