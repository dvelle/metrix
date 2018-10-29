package all

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.stream.ActorMaterializer
import com.codahale.metrics.MetricRegistry
import io.micrometer.core.instrument._
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.micrometer.core.instrument.Metrics
import io.micrometer.graphite.{GraphiteConfig, GraphiteHierarchicalNameMapper, GraphiteMeterRegistry}
import io.prometheus.client.CollectorRegistry

import scala.concurrent.ExecutionContextExecutor

trait JsonMetrics {
  def jsonMetrics: String
}
trait MyHttp {
  self: JsonMetrics ⇒
  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val route =
    path("metrics") {
      get {
        complete(jsonMetrics)
      }
    }
  Http().bindAndHandle(route, "localhost", 8080)
}

object TheirApp extends App with MyHttp with JsonMetrics {
  val host = "127.0.0.1"
  val port = 8080
  val graphite = new GraphiteMeterRegistry(GraphiteConfig.DEFAULT, Clock.SYSTEM,
    new GraphiteHierarchicalNameMapper(GraphiteConfig.DEFAULT.prefix()), new MetricRegistry)
  val prometheus = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT, new CollectorRegistry, Clock.SYSTEM)
  Metrics.addRegistry(prometheus)
  Metrics.addRegistry(graphite)
  val counter = Counter
    .builder("instance")
    .description("indicates instance count of the object")
    .tags("dev", "performance")
    .register(Metrics.globalRegistry)

  val counter1 = Counter
    .builder("instance")
    .description("indicates instance count of the object")
    .tags("dev", "performance")
    .register(Metrics.globalRegistry)

  counter.increment()
  counter1.increment()

  override def jsonMetrics = prometheus.scrape()
}
