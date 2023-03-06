import VersionHelper.{versionFmt, fallbackVersion}

// Lets me depend on Maven Central artifacts immediately without waiting
resolvers ++= Resolver.sonatypeOssRepos("public")

// Makes sure to increment the version for local development
ThisBuild / version := dynverGitDescribeOutput.value
  .mkVersion(out => versionFmt(out, dynverSonatypeSnapshots.value), fallbackVersion(dynverCurrentDate.value))

ThisBuild / dynver := {
  val d = new java.util.Date
  sbtdynver.DynVer
    .getGitDescribeOutput(d)
    .mkVersion(out => versionFmt(out, dynverSonatypeSnapshots.value), fallbackVersion(d))
}

enablePlugins(ScalaJSBundlerPlugin)

scalaVersion := Versions.Scala_2_13

crossScalaVersions := Seq(Versions.Scala_3, Versions.Scala_2_13, Versions.Scala_2_12)

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % Versions.ScalaJsDom,
  "org.scalatest" %%% "scalatest" % Versions.ScalaTest
)

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

(Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) })

(Test / requireJsDomEnv) := true

(webpack / version) := Versions.Webpack

(startWebpackDevServer / version) := Versions.WebpackDevServer

(installJsdom / version) := Versions.JsDom

useYarn := true

(Test / parallelExecution) := false

(Compile / fastOptJS / scalaJSLinkerConfig) ~= { _.withSourceMap(false) }

(Compile / fullOptJS / scalaJSLinkerConfig) ~= { _.withSourceMap(false) }

