import sbt._

object Version {
  // Versions
  val corese = "3.2.1"
  val playJson = "2.5.9"
  val typesafeConfig = "1.3.0"
  val scalaLogging = "3.5.0"
  val scalaXml = "1.0.5"
  val logback = "1.1.7"
  val scalaTest = "3.0.0"
  val scala = "2.11.8"
//  val slf4j = "1.7.21"
}

object Library {
  // Config
  val config = "com.typesafe" % "config" % Version.typesafeConfig
  val playJson = "com.typesafe.play" %% "play-json" % Version.playJson
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Version.scalaLogging

  // Reasoner
  val corese = "fr.inria.wimmics" % "kgtool" % Version.corese

  // Testing
  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest
  val logbackClassic = "ch.qos.logback" % "logback-classic" % Version.logback
}

object Dependencies {

  import Library._

  val testing = Seq(
    scalaTest % "test",
    logbackClassic % "test"
  )
  val coreseScala = testing ++ List(scalaLogging, config, playJson, corese)
}
