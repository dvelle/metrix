package your

import java.io.StringWriter
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.stream.ActorMaterializer
import io.prometheus.client.{CollectorRegistry, Gauge}
import io.prometheus.client.exporter.common.TextFormat

import scala.concurrent.{Await, ExecutionContextExecutor}

trait MyHttp {
  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val counter = new AtomicLong()
  val route =
    path("metrics") {
      get {
        val writer: StringWriter = new StringWriter
        TextFormat.write004(writer, CollectorRegistry.defaultRegistry.metricFamilySamples())
        if (counter.incrementAndGet() == 1) {
          new Thread(() => {
            TimeUnit.MILLISECONDS.sleep(100L)
            Await.result(system.terminate(), 100 milli)
          }).start()
        }
        println(s"scraped #${counter.get()} @${LocalDateTime.now()}")
        complete(writer.toString)
      }
    }
  Http().bindAndHandle(route, "0.0.0.0", 8080)
}

object YourApp1 extends App with MyHttp {
  val host = "127.0.0.1"
  val port = 8080
  val gauge = Gauge.build.name("some_metric").help("some metric.").register
}
