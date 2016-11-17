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
  // Scala
//  val scalaReflect = "org.scala-lang" % "scala-reflect" % Version.scala
//  val commonsLang3 = "org.apache.commons" % "commons-lang3" % "3.4"
  // Testing
  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest


  val config = "com.typesafe" % "config" % Version.typesafeConfig
  val playJson = "com.typesafe.play" %% "play-json" % Version.playJson
//  val scalaXml = "org.scala-lang.modules" %% "scala-xml" % Version.scalaXml

  // Logging
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Version.scalaLogging
  val logbackClassic = "ch.qos.logback" % "logback-classic" % Version.logback

  // Semengine
  val corese = "fr.inria.wimmics" % "kgtool" % Version.corese
}

object Dependencies {

  import Library._

  val testing = List(
    scalaTest % "test",
    logbackClassic % "test"
  )

  val logging = List(
    scalaLogging,
    logbackClassic % "test"
  )

  val coreseScala = testing ++ logging ++ List(config, playJson, corese)
}
