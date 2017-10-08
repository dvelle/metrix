import versions._

name := "Metrix"
version := "0.1"
scalaVersion := "2.12.3"

libraryDependencies ++= Seq(

  "org.json4s" %% "json4s-jackson" % "3.5.3",

  "io.dropwizard.metrics" % "metrics-core" % dropwizardVersion,
  "io.dropwizard.metrics" % "metrics-healthchecks" % dropwizardVersion,
  "io.dropwizard.metrics" % "metrics-graphite" % dropwizardVersion,
  "io.dropwizard.metrics" % "metrics-json" % dropwizardVersion,

  "io.prometheus" % "simpleclient" % prometheusClientVersion,
  "io.prometheus" % "simpleclient_httpserver" % prometheusClientVersion,

  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion

)
