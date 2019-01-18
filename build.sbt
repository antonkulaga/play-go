import sbt.Keys._

import sbt._

name := "play-go"

organization := "group.aging-research"

lazy val scala_2_13 = "2.13.0-M5"

lazy val scala_2_12 = "2.12.8"

lazy val supportedScalaVersions = List(scala_2_12, scala_2_13)

scalaVersion := scala_2_12

crossScalaVersions := supportedScalaVersions

version := "0.0.3.3"

isSnapshot := false

exportJars := true

javacOptions ++= Seq("-Xlint", "-J-Xss5M", "-encoding", "UTF-8")

javaOptions ++= Seq("-Xms512M", "-Xmx6048M", "-XX:+CMSClassUnloadingEnabled")

resolvers += Resolver.bintrayRepo("comp-bio-aging", "main")

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-collection-compat" % "0.2.1",
  "com.github.pathikrit"  %% "better-files"  % "3.7.0",
  "org.eclipse.rdf4j" % "rdf4j-rio-rdfxml" % "2.4.2",
  "org.eclipse.rdf4j" % "rdf4j-repository-sail" % "2.4.2",
  "org.eclipse.rdf4j" % "rdf4j-sail-memory" % "2.4.2",
  "org.eclipse.rdf4j" % "rdf4j-sail-nativerdf" % "2.4.2"
)

bintrayRepository := "main"

bintrayOrganization := Some("comp-bio-aging")

licenses += ("MPL-2.0", url("http://opensource.org/licenses/MPL-2.0"))





