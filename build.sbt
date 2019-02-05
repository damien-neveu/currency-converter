name := """currency-converter"""
organization := "com.batbat"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.batbat.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.batbat.binders._"
