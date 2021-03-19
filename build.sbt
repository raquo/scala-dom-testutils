enablePlugins(ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "com.raquo" %%% "domtypes" % Versions.ScalaDomTypes,
  "org.scalatest" %%% "scalatest" % Versions.ScalaTest,
)

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:implicitConversions",
  {
    val localSourcesPath = baseDirectory.value.toURI
    val remoteSourcesPath = s"https://raw.githubusercontent.com/raquo/scala-dom-testutils/${git.gitHeadCommit.value.get}/"
    val sourcesOptionName = if (scalaVersion.value.startsWith("2.")) "-P:scalajs:mapSourceURI" else "-scalajs-mapSourceURI"

    s"${sourcesOptionName}:$localSourcesPath->$remoteSourcesPath"
  }
)

requireJsDomEnv in Test := true

version in installJsdom := Versions.JsDom

useYarn := true

parallelExecution in Test := false

scalaJSLinkerConfig in (Compile, fastOptJS) ~= { _.withSourceMap(false) }

scalaJSLinkerConfig in (Compile, fullOptJS) ~= { _.withSourceMap(false) }

