import sbt._

object Library {

  object http4s {
    lazy val emberServer: ModuleID = "org.http4s" %% "http4s-ember-server" % Version.Http4s
    lazy val emberClient: ModuleID = "org.http4s" %% "http4s-ember-client" % Version.Http4s
    lazy val circe: ModuleID       = "org.http4s" %% "http4s-circe"        % Version.Http4s
    lazy val dsl: ModuleID         = "org.http4s" %% "http4s-dsl"          % Version.Http4s
  }

  object circe {
    lazy val generic: ModuleID = "io.circe" %% "circe-generic" % Version.Circe
    lazy val parser: ModuleID  = "io.circe" %% "circe-parser"  % Version.Circe
  }

  object spark {
    lazy val sqlApi           = "org.apache.spark" %% "spark-sql-api"            % "3.5.3"
    lazy val connectClientJvm = "org.apache.spark" %% "spark-connect-client-jvm" % "3.5.3"

  }
  object teckel {
    lazy val api = "com.eff3ct" %% "teckel-api" % Version.Teckel
  }

  object jwtScala {
    lazy val jwtCirce: ModuleID = "com.github.jwt-scala" %% "jwt-circe" % "9.1.0"
  }

  object log4cats {
    lazy val core: ModuleID = "org.typelevel" %% "log4cats-core" % Version.Log4cats
    lazy val slf4j: ModuleID = "org.typelevel" %% "log4cats-slf4j" % Version.Log4cats
    lazy val slf4jApi: ModuleID = "org.slf4j" % "slf4j-api" % Version.Slf4j
  }

  object logback {
    lazy val classic: ModuleID = "ch.qos.logback" % "logback-classic" % Version.Logback
    lazy val jansi = "org.fusesource.jansi" % "jansi" % "2.4.0"
  }

  object pureconfig {
    lazy val pureconfig: ModuleID = "com.github.pureconfig" %% "pureconfig" % Version.Pureconfig
  }

  object Testing {
    lazy val scalaMeta: ModuleID        = "org.scalameta"     %% "munit"       % Version.Munit
    lazy val scalaTestMockito: ModuleID = "org.scalatestplus" %% "mockito-3-4" % Version.Mockito
    lazy val munit: ModuleID = "org.typelevel" %% "munit-cats-effect" % Version.MunitCatsEffect
    lazy val munitScalacheck: ModuleID = "org.typelevel" %% "munit-scalacheck" % Version.Munit

  }

}
