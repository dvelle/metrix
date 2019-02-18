name := "Metrix"
version := "0.1"
scalaVersion := "2.12.8"

val dropwizardVersion = "4.0.5"
val prometheusClientVersion = "0.6.0"
val akkaVersion = "2.5.20"
val akkaHttpVersion = "10.1.7"
val micrometerVersion = "1.1.2"

scalacOptions ++= Seq(
  //A -X option suggests permanence, while a -Y could disappear at any time
  "-encoding", "UTF-8", // source files are in UTF-8
  "-explaintypes", // Explain type errors in more detail.
  "-deprecation", // warn about use of deprecated APIs
  "-unchecked", // warn about unchecked type parameters
  "-feature", // warn about misused language features
  "-language:postfixOps", // allow higher kinded types without `import scala.language.postfixOps`
  "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
  "-language:implicitConversions", // Allow definition of implicit functions called views
  "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
  "-language:reflectiveCalls",
  "-Xlint", // enable handy linter warnings
  //"-Xfatal-warnings", // turn compiler warnings into errors
  "-Ypartial-unification", // allow the compiler to unify type constructors of different arities
  "-Yrangepos",
  "-Yrepl-sync"
)

libraryDependencies ++= Seq(

  "org.json4s" %% "json4s-jackson" % "3.5.3",
  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "io.dropwizard.metrics" % "metrics-core" % dropwizardVersion,
  "io.dropwizard.metrics" % "metrics-healthchecks" % dropwizardVersion,
  "io.dropwizard.metrics" % "metrics-graphite" % dropwizardVersion,
  "io.dropwizard.metrics" % "metrics-json" % dropwizardVersion,

  "io.prometheus" % "simpleclient" % prometheusClientVersion,
  "io.prometheus" % "simpleclient_hotspot" % prometheusClientVersion,
  "io.prometheus" % "simpleclient_httpserver" % prometheusClientVersion,

  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,

  "io.micrometer" % "micrometer-registry-prometheus" % micrometerVersion,
  "io.micrometer" % "micrometer-registry-graphite" % micrometerVersion

)
