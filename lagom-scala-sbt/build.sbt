organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `funcdemoscala` = (project in file("."))
  .aggregate(`funcdemoscala-api`, `funcdemoscala-impl`)

lazy val `funcdemoscala-api` = (project in file("funcdemoscala-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `funcdemoscala-impl` = (project in file("funcdemoscala-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest,
      filters
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`funcdemoscala-api`)


