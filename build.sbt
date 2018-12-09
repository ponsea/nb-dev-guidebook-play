name := """nb-dev-guidebook-play"""
organization := "net.nextbeat"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "com.typesafe.play" %% "play-slick" % "3.0.3",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3",
  "com.h2database" % "h2" % "1.4.197",
  "mysql" % "mysql-connector-java" % "5.1.47"
 )

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "net.nextbeat.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "net.nextbeat.binders._"
