import VersionHelper.{versionFmt, fallbackVersion}

// Makes sure to increment the version for local development
ThisBuild / version := dynverGitDescribeOutput.value
  .mkVersion(out => versionFmt(out, dynverSonatypeSnapshots.value), fallbackVersion(dynverCurrentDate.value))

ThisBuild / dynver := {
  val d = new java.util.Date
  sbtdynver.DynVer
    .getGitDescribeOutput(d)
    .mkVersion(out => versionFmt(out, dynverSonatypeSnapshots.value), fallbackVersion(d))
}

enablePlugins(ScalaJSPlugin)

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
)

scalacOptions ++= sys.env.get("CI").map { _ =>
  val localSourcesPath = (LocalRootProject / baseDirectory).value.toURI
  val remoteSourcesPath = s"https://raw.githubusercontent.com/raquo/scala-dom-testutils/${git.gitHeadCommit.value.get}/"
  val sourcesOptionName = if (scalaVersion.value.startsWith("2.")) "-P:scalajs:mapSourceURI" else "-scalajs-mapSourceURI"

  s"${sourcesOptionName}:$localSourcesPath->$remoteSourcesPath"
}

jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()

(Test / parallelExecution) := false
