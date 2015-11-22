package directives

/**
 * Created by joshr on 21/11/2015.
 */

import akka.event.Logging
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.{LoggingMagnet, LogEntry, DebuggingDirectives}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}


class LogRequestResponseSpec extends WordSpec with Matchers with ScalatestRouteTest {
  // different possibilities of using logRequestResponse

  // The first alternatives use an implicitly available LoggingContext for logging
  // marks with "get-user", log with debug level, HttpRequest.toString, HttpResponse.toString
  DebuggingDirectives.logRequestResult("get-user")

  // marks with "get-user", log with info level, HttpRequest.toString, HttpResponse.toString
  DebuggingDirectives.logRequestResult(("get-user", Logging.InfoLevel))

  // logs just the request method and response status at info level
  def requestMethodAndResponseStatusAsInfo(req: HttpRequest): Any => Option[LogEntry] = {
    case res: HttpResponse => Some(LogEntry(req.method + ":" + res.status, Logging.InfoLevel))
    case _                 => None // other kind of responses
  }
  DebuggingDirectives.logRequestResult(requestMethodAndResponseStatusAsInfo _)

  // This one doesn't use the implicit LoggingContext but uses `println` for logging
  def printRequestMethodAndResponseStatus(req: HttpRequest)(res: Any): Unit =
    println(requestMethodAndResponseStatusAsInfo(req)(res).map(_.obj.toString).getOrElse(""))
  val logRequestResultPrintln = DebuggingDirectives.logRequestResult(LoggingMagnet(_ => printRequestMethodAndResponseStatus))

  // tests:
  Get("/") ~> logRequestResultPrintln(complete("logged")) ~> check {
    responseAs[String] shouldEqual "logged"
  }

}
