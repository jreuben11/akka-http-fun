
/**
 * Created by joshr on 20/11/2015.
 */
import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

object Main extends App  {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val config = ConfigFactory.load()
  val logger = Logging(system, getClass)

  val route =
    path("hello") {
      get {
        complete {
          "hello from akka-http"
        }
      }
    }

  Http().bindAndHandle(route, config.getString("http.interface"), config.getInt("http.port"))
}
