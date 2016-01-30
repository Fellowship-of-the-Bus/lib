val deployTask = TaskKey[Unit]("deploy")

deployTask <<= Seq(compile in Compile, GhPagesKeys.pushSite, PgpKeys.publishSigned).dependOn

lazy val commonSettings = Seq(
  name := "fellowship-of-the-bus-lib",
  version := "0.3-SNAPSHOT",
  organization := "com.github.fellowship_of_the_bus",
  scalaVersion := "2.11.7",
  crossScalaVersions := Seq("2.10.6", "2.11.7"),
  crossVersion := CrossVersion.binary,
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-encoding", "UTF-8",
    "-target:jvm-1.6",
    "-optimize",
    "-Xlint",
    "-Yinline-warnings",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-value-discard",
    "-Ywarn-unused"
  ),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.2.6" % "test",
    "junit" % "junit" % "4.12" % "test",
    "org.slick2d" % "slick2d-core" % "1.0.1",
    "com.propensive" %% "rapture-json-jackson" % "2.0.0-M3"
  ),
  resolvers ++= Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    Resolver.url(
      "sbt-plugin-releases",
      new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/")
    )(Resolver.ivyStylePatterns)
  )
)

lazy val docSettings = site.settings ++ ghpages.settings ++ site.includeScaladoc() ++ Seq(
  autoAPIMappings := true, // our scaladoc should link against our dependencies
  git.remoteRepo := "git@github.com:fellowship-of-the-bus/lib.git"
)

lazy val publishSettings = Seq(
  homepage := Some(url("https://github.com/fellowship-of-the-bus/lib")),
  scmInfo := Some(ScmInfo(url("https://github.com/fellowship-of-the-bus/lib"), "scm:git:git@github.com:fellowship-of-the-bus/lib.git")),
  licenses += "The Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"),
  // allow others to link against our scaladoc
  apiURL := Some(url("https://fellowship-of-the-bus.github.io/lib/latest/api/")),
  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  publishMavenStyle := true,
  pomIncludeRepository := { _ => false },
  publishArtifact in Test := false,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <developers>
      <developer>
        <name>Rob Schluntz</name>
        <email>rschlunt@uwaterloo.ca</email>
        <url>http://www.cs.uwaterloo.ca/~rschlunt</url>
      </developer>
    </developers>)
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(docSettings: _*)
  .settings(publishSettings: _*)

