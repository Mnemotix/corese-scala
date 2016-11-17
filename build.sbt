import sbt.Keys._
import sbt._

name := """corese-scala"""

scalaVersion := "2.11.8"

organization := "com.mnemotix"

homepage := Some(url("http://www.mnemotix.com"))

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-Xlint")

publishMavenStyle := true

pomExtra :=
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>https://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>

publishTo := {
  val nexus = "https://repo.mnemotix.com/repository"
  if (isSnapshot.value)
    Some("MNX Nexus" at nexus + "/maven-snapshots/")
  else
    Some("MNX Nexus" at nexus + "/maven-releases/")
}

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

resolvers ++= Seq(
  Resolver.mavenLocal,
  "MNX Nexus (releases)" at "https://repo.mnemotix.com/repository/maven-releases/",
  "MNX Nexus (snapshots)" at "https://repo.mnemotix.com/repository/maven-snapshots/",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Sonatype (Snapshots)" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Typesafe (Releases)" at "http://repo.typesafe.com/typesafe/releases/"
)

updateOptions := updateOptions.value.withCachedResolution(true)

libraryDependencies ++= Dependencies.coreseScala

lazy val main = Project("corese-scala", file("."))
  .settings(libraryDependencies ++= Dependencies.coreseScala)
