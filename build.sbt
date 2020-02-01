enablePlugins(ScalaJSBundlerPlugin)

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "com.raquo" %%% "domtypes" % "0.9.6",
  "org.scalatest" %%% "scalatest" % "3.1.0",
  "org.scala-js" %%% "scalajs-dom" % "0.9.8" // @TODO This should be in Scala DOM Types
)

scalacOptions ++= Seq("-deprecation", "-feature", "-language:implicitConversions")

version in installJsdom := "16.0.1"

requireJsDomEnv in Test := true

useYarn := true

emitSourceMaps in fastOptJS := false

emitSourceMaps in fullOptJS := false
