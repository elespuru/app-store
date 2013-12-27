package com.xorcyst

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class AppStoreServiceSpec extends Specification with Specs2RouteTest with AppStoreService {
  def actorRefFactory = system

  "The AppStoreService" should {

    "return a greeting for GET requests to the root path" in {
      Get() ~> serviceRoutes ~> check { responseAs[String] must contain("app-store API") }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/w00t") ~> serviceRoutes ~> check { handled must beFalse }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(serviceRoutes) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET, POST"
      }
    }
  }
}