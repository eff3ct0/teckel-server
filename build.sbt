import Extension._
import Library._
import sbt.Keys._
import sbt._

lazy val root =
  (project in file("."))
    .settings(
      organization      := "com.eff3ct.teckel",
      name              := "teckel-server",
      scalaVersion      := Version.Scala,
      scalafmtOnCompile := true,
      Compile / mainClass := Some("com.eff3ct.teckel.app.Main"),
      javacOptions := Seq(
        "-g:none",
        "--add-exports=java.base/sun.nio.ch=ALL-UNNAMED"
      ),
      libraryDependencies ++= Seq(
        http4s.emberClient,
        http4s.emberServer,
        http4s.circe,
        http4s.dsl,
        circe.generic,
        teckel.api,
        spark.sqlApi,
        spark.connectClientJvm,
        log4cats.core,
        log4cats.slf4j,
        logback.classic,
//        logback.jansi,
        pureconfig.pureconfig,
        Testing.munit,
        Testing.scalaMeta
      )
    )
    .withHeader
    .withAssembly
    .withDocker
    .withKindProjector
    .withBetterMonadicFor
