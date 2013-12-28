package com.xorcyst

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import ApplicationJsonProtocol._

class AppStoreServiceSpec extends Specification with Specs2RouteTest with AppStoreService {
  def actorRefFactory = system

  "The AppStoreService" should {

    "return a greeting for GET requests to the root path" in {
      Get() ~> serviceRoutes ~> check { responseAs[String] must contain("app-store API") }
    }

    "return an app for GET requests to the resource's path" in {
      Get("/app/1") ~> serviceRoutes ~> check {
        status === OK
        responseAs[String] must contain("name")
      }
    }

    "update an existing app" in {
      Put("/app/2", Application("NewName","NewDescription","NewAuthor",1.00)) ~> serviceRoutes ~> check {
        status === NoContent
      }
      Get("/app/2") ~> serviceRoutes ~> check {
        status === OK
        responseAs[String] must contain("NewName")
      }
    }

    "create a new app" in {
      Post("/apps", Application("NewApp","Description","Author",1.00)) ~> serviceRoutes ~> check {
        status === Created
      }
      Get("/app/4") ~> serviceRoutes ~> check {
        status === OK
        responseAs[String] must contain("NewApp")
      }
    }

    "delete an app" in {
      Delete("/app/3") ~> serviceRoutes ~> check {
        status === NoContent
      }
    }

    "return a 404 for GET requests to a non-existent resource's path" in {
      Get("/app/666") ~> serviceRoutes ~> check {
        status === NotFound
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/w00t") ~> serviceRoutes ~> check { handled must beFalse }
    }

    "return a MethodNotAllowed error for PUT request to the root path" in {
      Put() ~> sealRoute(serviceRoutes) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }

    "return a MethodNotAllowed error for POST request to the root path" in {
      Post() ~> sealRoute(serviceRoutes) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }

  }
}