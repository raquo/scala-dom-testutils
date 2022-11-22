enablePlugins(ScalaJSBundlerPlugin)

libraryDependencies ++= Seq(
  "com.raquo" %%% "domtypes" % Versions.ScalaDomTypes,
)

libraryDependencies ++= Seq(
  "org.scalatest" %%% "scalatest" % Versions.ScalaTest,

  "com.lihaoyi" %%% "utest" % Versions.uTest,

  "io.monix" %%% "minitest" % Versions.miniTest,

  "org.wvlet.airframe" %%% "airframe-log" % Versions.AirFrame,
  "org.wvlet.airframe" %%% "airspec" % Versions.AirFrame,
  "org.scala-js" %%% "scalajs-java-securerandom" % "1.0.0",
).flatMap(dep => Seq(dep % Optional,  dep % Test))

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:higherKinds",
  "-language:implicitConversions",
  {
    val localSourcesPath = baseDirectory.value.toURI
    val remoteSourcesPath = s"https://raw.githubusercontent.com/raquo/scala-dom-testutils/${git.gitHeadCommit.value.get}/"
    val sourcesOptionName = if (scalaVersion.value.startsWith("2.")) "-P:scalajs:mapSourceURI" else "-scalajs-mapSourceURI"

    s"${sourcesOptionName}:$localSourcesPath->$remoteSourcesPath"
  }
)

// TODO: consider commenting out test frameworks or better moving test suites to separate subprojects,
//       as mix of outputs from multiple different test frameworks looks ugly
testFrameworks ++= Seq(
  "wvlet.airspec.Framework",
  "utest.runner.Framework",
  "minitest.runner.Framework",
).map(new TestFramework(_))

(Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) })

(Test / requireJsDomEnv) := true

(installJsdom / version) := Versions.JsDom

useYarn := true

(Test / parallelExecution) := false

(Compile / fastOptJS / scalaJSLinkerConfig) ~= { _.withSourceMap(false) }

(Compile / fullOptJS / scalaJSLinkerConfig) ~= { _.withSourceMap(false) }

