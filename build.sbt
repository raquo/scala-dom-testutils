enablePlugins(ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "com.raquo" %%% "domtypes" % Versions.ScalaDomTypes,
  "org.scalatest" %%% "scalatest" % Versions.ScalaTest,
)

scalacOptions ++= Seq("-deprecation", "-feature", "-language:implicitConversions")

requireJsDomEnv in Test := true

version in installJsdom := Versions.JsDom

useYarn := true

parallelExecution in Test := false

scalaJSLinkerConfig in (Compile, fastOptJS) ~= { _.withSourceMap(false) }

scalaJSLinkerConfig in (Compile, fullOptJS) ~= { _.withSourceMap(false) }
