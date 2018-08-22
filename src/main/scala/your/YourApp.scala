package your

import java.util.concurrent.TimeUnit._
import io.prometheus.client._

trait HTTPServer {
  def host: String
  def port: Int
  def startServer = new io.prometheus.client.exporter.HTTPServer(host, port)
}
object MyGauge extends App with HTTPServer {
  val host = "127.0.0.1"
  val port = 8080
  startServer
  val requests = Gauge.build.name("inprogress_requests").help("Inprogress requests.").register
  while (true) {
    requests.inc()
    MILLISECONDS.sleep(100L)
  }
}
object MyCounter extends App with HTTPServer {
  val host = "127.0.0.2"
  val port = 8080
  startServer
  //Counters go up, and reset when the process restarts
  val requests1 = Counter.build.name("requests_total1").labelNames("src", "dest").help("Total requests.").register
  val requests2 = Counter.build.name("requests_total2").labelNames("src", "dest").help("Total requests.").register
  while (true) {
    requests1.labels("x", "y").inc()
    requests2.labels("m", "n").inc()
    MILLISECONDS.sleep(100L)
  }
}
object MyHistogram extends App with HTTPServer {
  val host = "127.0.0.3"
  val port = 8080
  startServer
  val requestLatency = Histogram.build.name("requests_latency_seconds")
    .help("Request latency in seconds.").register
  while (true) {
    val requestTimer = requestLatency.startTimer
    MILLISECONDS.sleep(100L)
    requestTimer.observeDuration
  }

}
object MyTimer extends App with HTTPServer {
  val host = "127.0.0.4"
  val port = 8080
  startServer
  //val receivedBytes = Summary.build.name("requests_size_bytes").help("Request size in bytes.").register
  val requestLatency = Summary.build.name("requests_latency_seconds")
    .help("Request latency in seconds.")
    .quantile(.001, 0.0)
    .quantile(.1, 0.0)
    .quantile(.2, 0.0)
    .quantile(.3, 0.0)
    .quantile(.4, 0.0)
    .ageBuckets(2)
    .register
  while (true) {
    val howLong = util.Random.nextInt(10)
    val requestTimer = requestLatency.startTimer
    MILLISECONDS.sleep(1 * 100L)
    //receivedBytes.observe(howLong)
    requestTimer.observeDuration
  }
}
