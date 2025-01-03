/** Compiler */
addSbtPlugin("org.typelevel" % "sbt-tpolecat" % "0.5.2")
addSbtPlugin("io.spray"      % "sbt-revolver" % "0.10.0")

/** Build */
addSbtPlugin("com.eed3si9n"      % "sbt-assembly" % "2.3.0")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt" % "2.5.2")
addSbtPlugin("de.heikoseeberger" % "sbt-header"   % "5.10.0")

/** Publish */
addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.11.0")
addSbtPlugin("com.github.sbt"    % "sbt-dynver" % "5.1.0")
