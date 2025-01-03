import de.heikoseeberger.sbtheader.HeaderPlugin
import sbt._
import sbtassembly.AssemblyPlugin
import sbtdocker.DockerPlugin

object Extension {

  implicit class ProjectOps(project: Project) {
    def withDocker(dockerSettings: Seq[Setting[_]]): Project =
      project.enablePlugins(DockerPlugin).settings(dockerSettings)

    def withDocker: Project = withDocker(Docker.projectSettings)

    def withAssembly(assemblySettings: Seq[Setting[_]]): Project =
      project.enablePlugins(AssemblyPlugin).settings(assemblySettings)

    def withAssembly: Project =
      withAssembly(Assembly.projectSettings)

    def withKindProjector: Project =
      project.settings(
        Seq(
          addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.3" cross CrossVersion.full)
        )
      )

    def withBetterMonadicFor: Project =
      project.settings(
        Seq(
          addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
        )
      )

    def withHeader: Project =
      project
        .enablePlugins(HeaderPlugin)
        .settings(
          Header.projectSettings
        )
  }

}
