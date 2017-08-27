
name := "Scala DOM Test Utils"

// @TODO[API] I don't think this will need a cross project anytime soon, consider simplifying

scalaVersion in ThisBuild := "2.11.11" // "in ThisBuild" also applies this setting to JS and JVM projects

lazy val root = project.in(file("."))
  .aggregate(js, jvm)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val domtestutils = crossProject.in(file("."))
  .settings(
    organization := "com.raquo",
    normalizedName := "domtestutils",
    version := "0.1-SNAPSHOT",
    crossScalaVersions := Seq("2.11.11", "2.12.3"),
    homepage := Some(url("https://github.com/raquo/scala-dom-test-utils")),
    licenses += ("MIT", url("https://github.com/raquo/scala-dom-test-utils/blob/master/LICENSE.txt"))
  )
  .jsConfigure(_.enablePlugins(ScalaJSBundlerPlugin))
  .jsSettings(
    requiresDOM in Test := true,
    useYarn := true,
    emitSourceMaps in fastOptJS := false,
    emitSourceMaps in fullOptJS := false,
    libraryDependencies ++= Seq(
      "com.raquo" %%% "domtypes" % "0.1-SNAPSHOT",
      "org.scala-js" %%% "scalajs-dom" % "0.9.3",
      "org.scalatest" %%% "scalatest" % "3.0.3"
    )
  )
  .jvmSettings()

lazy val js = domtestutils.js
lazy val jvm = domtestutils.jvm
