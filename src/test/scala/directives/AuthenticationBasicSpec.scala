package directives

/**
 * Created by joshr on 20/11/2015.
 */

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{BasicHttpCredentials, HttpChallenge, `WWW-Authenticate`}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class AuthenticationBasicSpec extends WordSpec with Matchers with ScalatestRouteTest {

  def myUserPassAuthenticator(credentials: Credentials): Option[String] =
    credentials match {
      case p @ Credentials.Provided(id) if p.verify("p4ssw0rd") => Some(id)
      case _ => None
    }

  val route =
    Route.seal {
      path("secured") {
        authenticateBasic(realm = "secure site", myUserPassAuthenticator) { userName =>
          complete(s"The user is '$userName'")
        }
      }
    }

  // tests:
  Get("/secured") ~> route ~> check {
    status shouldEqual StatusCodes.Unauthorized
    responseAs[String] shouldEqual "The resource requires authentication, which was not supplied with the request"
    header[`WWW-Authenticate`].get.challenges.head shouldEqual HttpChallenge("Basic", "secure site")
  }

  val validCredentials = BasicHttpCredentials("John", "p4ssw0rd")
  Get("/secured") ~> addCredentials(validCredentials) ~> // adds Authorization header
    route ~> check {
    responseAs[String] shouldEqual "The user is 'John'"
  }

  val invalidCredentials = BasicHttpCredentials("Peter", "pan")
  Get("/secured") ~>
    addCredentials(invalidCredentials) ~> // adds Authorization header
    route ~> check {
    status shouldEqual StatusCodes.Unauthorized
    responseAs[String] shouldEqual "The supplied authentication is invalid"
    header[`WWW-Authenticate`].get.challenges.head shouldEqual HttpChallenge("Basic", "secure site")
  }
}


