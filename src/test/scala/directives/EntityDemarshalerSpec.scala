package directives

/**
 * Created by joshr on 21/11/2015.
 */

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import StatusCodes._
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.model.MediaTypes._

class EntityDemarshalerSpec extends WordSpec with Matchers with ScalatestRouteTest {
  case class Person(name: String, favoriteNumber: Int)

  object PersonJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
    implicit val PortofolioFormats = jsonFormat2(Person)
  }
  import PersonJsonSupport._

  val route = post {
    entity(as[Person]) { person =>
      complete(s"Person: ${person.name} - favorite number: ${person.favoriteNumber}")
    }
  }

  // tests:
  Post("/", HttpEntity(`application/json`, """{ "name": "Jane", "favoriteNumber" : 42 }""")) ~>
    route ~> check {
    responseAs[String] shouldEqual "Person: Jane - favorite number: 42"
  }

}
