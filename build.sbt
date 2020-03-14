enablePlugins(ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "com.raquo" %%% "domtypes" % "0.9.7",
  "org.scalatest" %%% "scalatest" % "3.1.1",
)

scalacOptions ++= Seq("-deprecation", "-feature", "-language:implicitConversions")

version in installJsdom := "16.2.0"

requireJsDomEnv in Test := true

useYarn := true

scalaJSLinkerConfig in (Compile, fastOptJS) ~= { _.withSourceMap(false) }

scalaJSLinkerConfig in (Compile, fullOptJS) ~= { _.withSourceMap(false) }

// @Warning remove this when scalajs-bundler > 0.17 is out https://github.com/scalacenter/scalajs-bundler/issues/332#issuecomment-594401804
Test / jsEnv := new tempfix.JSDOMNodeJSEnv(tempfix.JSDOMNodeJSEnv.Config((Test / installJsdom).value))
