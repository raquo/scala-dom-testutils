name := "Scala DOM Test Utils"

normalizedName := "domtestutils"

organization := "com.raquo"

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
    url = url("https://github.com/raquo")
  )
)

(Test / publishArtifact) := false

pomIncludeRepository := { _ => false }

sonatypeCredentialHost := "s01.oss.sonatype.org"

sonatypeRepository := "https://s01.oss.sonatype.org/service/local"
