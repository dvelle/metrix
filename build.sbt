import mert._

name := "Metrix"
version := "0.1"
scalaVersion := "2.12.6"

val dropwizardVersion = "3.2.4"
val prometheusClientVersion = "0.4.0"
val akkaVersion = "2.5.6"
val akkaHttpVersion = "10.0.10"
val micrometerVersion = "1.0.6"

libraryDependencies ++= Seq(

  "org.json4s" %% "json4s-jackson" % "3.5.3",

  "io.dropwizard.metrics" % "metrics-core" % dropwizardVersion,
  "io.dropwizard.metrics" % "metrics-healthchecks" % dropwizardVersion,
  "io.dropwizard.metrics" % "metrics-graphite" % dropwizardVersion,
  "io.dropwizard.metrics" % "metrics-json" % dropwizardVersion,

//  "io.prometheus" % "simpleclient" % prometheusClientVersion,
  "io.prometheus" % "simpleclient_httpserver" % prometheusClientVersion,

  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,

  "io.micrometer" % "micrometer-registry-prometheus" % micrometerVersion

)
