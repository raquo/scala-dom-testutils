enablePlugins(ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "com.raquo" %%% "domtypes" % "0.11.0",
  "org.scalatest" %%% "scalatest" % "3.2.2",
)

scalacOptions ++= Seq("-deprecation", "-feature", "-language:implicitConversions")

requireJsDomEnv in Test := true

version in installJsdom := "16.4.0"

useYarn := true

parallelExecution in Test := false

scalaJSLinkerConfig in (Compile, fastOptJS) ~= { _.withSourceMap(false) }

scalaJSLinkerConfig in (Compile, fullOptJS) ~= { _.withSourceMap(false) }

// @Warning remove this when scalajs-bundler > 0.17 is out https://github.com/scalacenter/scalajs-bundler/issues/332#issuecomment-594401804
Test / jsEnv := new tempfix.JSDOMNodeJSEnv(tempfix.JSDOMNodeJSEnv.Config((Test / installJsdom).value))
