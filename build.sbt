enablePlugins(ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "com.raquo" %%% "domtypes" % "0.4.2",
  "org.scala-js" %%% "scalajs-dom" % "0.9.4",
  "org.scalatest" %%% "scalatest" % "3.0.4"
)

requiresDOM in Test := true

useYarn := true

emitSourceMaps in fastOptJS := false

emitSourceMaps in fullOptJS := false
