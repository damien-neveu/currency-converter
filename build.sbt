import java.util.Properties

val appProperties = settingKey[Properties]("The application properties")

appProperties := {
  val prop = new Properties()
  IO.load(prop, new File("conf/application.common.conf"))
  prop
}

name := appProperties.value.getProperty("project.name")
version := appProperties.value.getProperty("project.version")
organization := "com.batbat"



lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  guice,
  caffeine,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.batbat.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.batbat.binders._"
