enablePlugins(ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "com.raquo" %%% "domtypes" % "0.2.2-SNAPSHOT",
  "org.scala-js" %%% "scalajs-dom" % "0.9.3",
  "org.scalatest" %%% "scalatest" % "3.0.3"
)

requiresDOM in Test := true

useYarn := true

emitSourceMaps in fastOptJS := false

emitSourceMaps in fullOptJS := false
