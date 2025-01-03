import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._
import sbtassembly.AssemblyPlugin.autoImport.MergeStrategy
import sbtassembly._

object Assembly {

  def projectSettings: Seq[Setting[_]] =
    Seq(
      assembly / assemblyMergeStrategy := {
        case "META-INF/services/org.apache.spark.sql.sources.DataSourceRegister" =>
          MergeStrategy.concat
        case PathList("META-INF", xs @ _*) => MergeStrategy.discard
        case "application.conf"            => MergeStrategy.concat
        case x                             => MergeStrategy.first
      },
      // JAR file settings
      assembly / assemblyJarName := {
        s"${name.value}_${scalaBinaryVersion.value}.jar"
      }
    )

}
