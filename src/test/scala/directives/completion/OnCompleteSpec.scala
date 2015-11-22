package directives.completion

/**
 * Created by joshr on 21/11/2015.
 */

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import StatusCodes._
import scala.concurrent.Future
import scala.util.{Failure, Success}

class OnCompleteSpec extends WordSpec with Matchers with ScalatestRouteTest {
  def divide(a: Int, b: Int): Future[Int] = Future {
    a / b
  }

  val route =
    path("divide" / IntNumber / IntNumber) { (a, b) =>
      onComplete(divide(a, b)) {
        case Success(value) => complete(s"The result was $value")
        case Failure(ex)    => complete((InternalServerError, s"An error occurred: ${ex.getMessage}"))
      }
    }

  // tests:
  Get("/divide/10/2") ~> route ~> check {
    responseAs[String] shouldEqual "The result was 5"
  }
  Get("/divide/10/0") ~> Route.seal(route) ~> check {
    status shouldEqual InternalServerError
    responseAs[String] shouldEqual "An error occurred: / by zero"
  }
}
