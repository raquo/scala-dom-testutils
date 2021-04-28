name := "Scala DOM Test Utils"

normalizedName := "domtestutils"

organization := "com.raquo"

scalaVersion := Versions.Scala_2_13

crossScalaVersions := Seq(Versions.Scala_3_RC3, Versions.Scala_3_RC2, Versions.Scala_2_13, Versions.Scala_2_12)

homepage := Some(url("https://github.com/raquo/scala-dom-testutils"))

licenses += ("MIT", url("https://github.com/raquo/scala-dom-testutils/blob/master/LICENSE.md"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/raquo/scala-dom-testutils"),
    "scm:git@github.com/raquo/scala-dom-testutils.git"
  )
)

developers := List(
  Developer(
    id = "raquo",
    name = "Nikita Gazarov",
    email = "nikita@raquo.com",
    url = url("http://raquo.com")
  )
)

releaseProcess := {
  import ReleaseTransformations._
  Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    releaseStepCommandAndRemaining("+publishSigned"),
    releaseStepCommand("sonatypeBundleRelease"),
    setNextVersion,
    commitNextVersion,
    pushChanges
  )
}

sonatypeProfileName := "com.raquo"

publishTo := sonatypePublishToBundle.value

publishMavenStyle := true

(Test / publishArtifact) := false

releaseCrossBuild := true

pomIncludeRepository := { _ => false }

releasePublishArtifactsAction := PgpKeys.publishSigned.value
