package my

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit._

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.codahale.metrics._
import com.codahale.metrics.graphite._
import com.codahale.metrics.json.MetricsModule
import com.fasterxml.jackson.databind.ObjectMapper
import org.json4s.JValue
import org.json4s.jackson.JsonMethods._

import scala.concurrent.ExecutionContextExecutor

trait JsonMetrics {
  def jsonMetrics: JValue
}

trait MyHttp {
  self: JsonMetrics ⇒
  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route =
    path("metrics") {
      get {
        complete(pretty(jsonMetrics))
      }
    }

  Http().bindAndHandle(route, "localhost", 8080)
}

trait MyGraphite extends JsonMetrics {
  val metrics = new MetricRegistry()
  val graphite = new Graphite(new InetSocketAddress("localhost", 2003))
  val reporter: GraphiteReporter = GraphiteReporter.forRegistry(metrics)
    .prefixedWith("hope")
    .convertRatesTo(SECONDS)
    .convertDurationsTo(MILLISECONDS)
    .filter(MetricFilter.ALL)
    .build(graphite)
  reporter.start(500, MILLISECONDS)

  private val mapper = new ObjectMapper().registerModule(
    new MetricsModule(SECONDS, MILLISECONDS, false)
  )
  private val writer = mapper.writerWithDefaultPrettyPrinter()

  def jsonMetrics: JValue = parse(writer.writeValueAsString(metrics))
}

object MyMeter extends App with MyGraphite with MyHttp {
  // A meter measures the rate of events over time (e.g., “requests per second”).
  // In addition to the mean rate, meters also track 1-, 5-, and 15-minute moving averages.
  val myMeter = metrics.meter("my-meter")
  while (true) {
    myMeter.mark()
    MILLISECONDS.sleep(100L)
  }
}

object MyGauge extends App with MyGraphite {
  val buffer = new collection.mutable.ArrayBuffer[Int]

  //A gauge is an instantaneous measurement of a value. For example,
  // we may want to measure the number of pending jobs in a queue:

  metrics.register("my-gauge", new Gauge[Int] {
    override def getValue = buffer.size
  })

  while (true) {
    val howMany = util.Random.nextInt(10)
    val add = util.Random.nextBoolean()

    if (add) {
      buffer.appendAll(1 to howMany)
    } else {
      buffer.remove(0, howMany min buffer.size)
    }

    MILLISECONDS.sleep(100L)
  }
}

object MyCounter extends App with MyGraphite with MyHttp {
  //A counter is just a gauge for an AtomicLong instance.
  val myCounter = metrics.counter("my-counter")
  while (true) {
    myCounter.inc()
    MILLISECONDS.sleep(100L)
  }
}

object MyHistogram extends App with MyGraphite with MyHttp {
  //A histogram measures the statistical distribution of values in a stream of data.
  val myHistogram = metrics.histogram("my-histogram")
  val myTimer = metrics.timer("my-timer")

  while (true) {
    myHistogram.update(myHistogram.getCount + 1)
    val context = myTimer.time()
    val howLong = util.Random.nextInt(10)
    MILLISECONDS.sleep(howLong * 100L)
    context.stop()
  }

}

object MyTimer extends App with MyGraphite with MyHttp {
  //A timer measures both the rate that a particular piece of code is called and the distribution of its duration.

  //A timer is basically a histogram of the duration of a type of event and a meter of the rate of its occurrence.

  val myTimer = metrics.timer("my-timer")

  while (true) {
    val context = myTimer.time()
    val howLong = util.Random.nextInt(10)
    MILLISECONDS.sleep(howLong * 100L)
    context.stop()
  }
}
