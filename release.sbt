name := "Scala DOM Test Utils"

normalizedName := "domtestutils"

organization := "com.raquo"

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.11", "2.12.4")

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

sonatypeProfileName := "com.raquo"

publishMavenStyle := true

publishArtifact in Test := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

releaseCrossBuild := true

pomIncludeRepository := { _ => false }

useGpg := false

releasePublishArtifactsAction := PgpKeys.publishSigned.value
