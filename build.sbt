enablePlugins(ScalaJSBundlerPlugin)

resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "com.raquo" %%% "domtypes" % "0.9.6",
  "org.scalatest" %%% "scalatest" % "3.0.8"
)

requiresDOM in Test := true

useYarn := true

emitSourceMaps in fastOptJS := false

emitSourceMaps in fullOptJS := false
