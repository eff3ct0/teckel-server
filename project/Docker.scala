import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport.assembly
import sbtdocker.DockerKeys._
import sbtdocker._

object Docker {

  lazy val dockerBaseImage = "amazoncorretto:11-alpine"

  def projectSettings: Seq[Setting[_]] = Seq(
    docker / imageNames := Seq(
      // Sets the latest tag
      ImageName(s"eff3ct/${name.value}:${version.value}"),
      ImageName(s"eff3ct/${name.value}:latest")
    ),
    docker / dockerfile := {
      // The assembly task generates a fat JAR file
      val artifact: File             = assembly.value
      val artifactTargetPath: String = s"/app/${artifact.name}"
      new Dockerfile {
        from(dockerBaseImage)
        add(artifact, artifactTargetPath)
        entryPoint("java", "-jar", artifactTargetPath)
      }
    }
  )

}
