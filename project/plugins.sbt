logLevel := Level.Warn

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.20.2")

addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.11.2")

addSbtPlugin("com.github.sbt" % "sbt-git" % "2.1.0")

libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.1"
