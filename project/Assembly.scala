import sbt.Keys._
import sbt._
import sbtassembly.AssemblyKeys._
import sbtassembly._

object Assembly {

  def projectSettings: Seq[Setting[_]] =
    Seq(
      assembly / assemblyMergeStrategy := {
        case PathList("META-INF", xs @ _*) => MergeStrategy.discard
        case x =>
          val oldStrategy = (assembly / assemblyMergeStrategy).value
          oldStrategy(x)
      },
      // JAR file settings
      assembly / assemblyJarName := {
        s"${name.value}_${scalaBinaryVersion.value}.jar"
      }
    )

}
