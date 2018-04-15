enablePlugins(ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "com.raquo" %%% "domtypes" % "0.7",
  "org.scala-js" %%% "scalajs-dom" % "0.9.5",
  "org.scalatest" %%% "scalatest" % "3.0.5"
)

requiresDOM in Test := true

useYarn := true

emitSourceMaps in fastOptJS := false

emitSourceMaps in fullOptJS := false
