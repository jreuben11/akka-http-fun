package directives.completion

/**
 * Created by joshr on 21/11/2015.
 */

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import spray.json.DefaultJsonProtocol
import akka.http.scaladsl.model.MediaTypes._

class CompleteWithSpec extends WordSpec with Matchers with ScalatestRouteTest {

  case class Person(name: String, favoriteNumber: Int)

  object PersonJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
    implicit val PortofolioFormats = jsonFormat2(Person)
  }

  val findPerson = (f: Person => Unit) => {
    //... some processing logic...
    //complete the request
    f(Person("Jane", 42))
  }

  import PersonJsonSupport._

  //expose implicit
  val route = get {
    completeWith(instanceOf[Person]) { completionFunction => findPerson(completionFunction) }
  }

  // tests:
  Get("/") ~> route ~> check {
    mediaType shouldEqual `application/json`
    responseAs[String] should include( """"name": "Jane"""")
    responseAs[String] should include( """"favoriteNumber": 42""")
  }
}

