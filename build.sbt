name := """technical-test"""
organization := "com.meetime"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.16"

libraryDependencies ++= Seq(
  guice,
  javaWs,
  "org.playframework" %% "play-json" % "3.0.4",
  caffeine
)
