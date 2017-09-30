name := "Metrix"
version := "0.1"
scalaVersion := "2.12.3"

val metricsVersion = "3.2.4"
val akkaVersion = "2.5.6"

libraryDependencies ++= Seq(

  "org.json4s" %% "json4s-jackson" % "3.5.3",

  "io.dropwizard.metrics" % "metrics-core" % metricsVersion,
  "io.dropwizard.metrics" % "metrics-healthchecks" % metricsVersion,
  "io.dropwizard.metrics" % "metrics-graphite" % metricsVersion,
  "io.dropwizard.metrics" % "metrics-json" % metricsVersion,

  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion

)
