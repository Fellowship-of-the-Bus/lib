import com.typesafe.sbt.SbtGit.GitKeys._

def scalacOptionsVersion(scalaVersion: String) = {
  Seq(
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
    "-Ywarn-value-discard"
  ) ++ (CrossVersion.partialVersion(scalaVersion) match {
         case Some((2, scalaMajor)) =>
           if (scalaMajor < 11) Nil
           else {
             Seq("-Ywarn-unused") ++
               (if (scalaMajor == 11) Seq("-Ybackend:GenBCode") // until 2.12 to eliminate inline warnings
               else Nil)
          }
         case _ => Nil
       })
}

val deployTask = TaskKey[Unit]("deploy")

deployTask <<= Seq(compile in Compile, GhPagesKeys.pushSite, PgpKeys.publishSigned).dependOn

lazy val standardLibSettings = Seq(
  name := "fellowship-of-the-bus-lib",
  version := "0.4-SNAPSHOT"
)

lazy val slick2dLibSettings = Seq(
  name := "fellowship-of-the-bus-slick2d-lib",
  version := "0.1-SNAPSHOT",
  libraryDependencies ++= Seq(
    "org.slick2d" % "slick2d-core" % "1.0.1"
  )
)

lazy val androidPlatformTarget = "android-16"
lazy val androidLibSettings = Seq(
  name := "fellowship-of-the-bus-android-lib",
  version := "0.1-SNAPSHOT",
  crossScalaVersions := Seq("2.11.8"),
  libraryDependencies ++= Seq(
    "org.scaloid" %% "scaloid" % "4.1"
  ),
  unmanagedBase := new java.io.File(System.getenv("ANDROID_HOME")) / "platforms" / androidPlatformTarget
)

lazy val commonSettings = Seq(
  organization := "com.github.fellowship_of_the_bus",
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.10.6", "2.11.8"),
  crossVersion := CrossVersion.binary,
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
  scalacOptions ++= scalacOptionsVersion(scalaVersion.value),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.2.6" % "test",
    "junit" % "junit" % "4.12" % "test",
    "com.propensive" %% "rapture-json-jackson" % "2.0.0-M5"
  ),
  resolvers ++= Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    Resolver.url(
      "sbt-plugin-releases",
      new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/")
    )(Resolver.ivyStylePatterns)
  )
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

lazy val standardLib = (project in file("standard-lib"))
  .settings(commonSettings: _*)
  .settings(standardLibSettings: _*)
  .settings(publishSettings: _*)

lazy val slick2dLib = (project in file("slick2d-lib"))
  .dependsOn(standardLib)
  .settings(commonSettings: _*)
  .settings(slick2dLibSettings: _*)
  .settings(publishSettings: _*)

lazy val androidLib = (project in file("android-lib"))
  .dependsOn(standardLib)
  .settings(commonSettings: _*)
  .settings(androidLibSettings: _*)
  .settings(publishSettings: _*)

lazy val root = (project in file("."))
  .aggregate(standardLib, slick2dLib, androidLib)
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(unidocSettings: _*)
  .settings(site.settings ++ ghpages.settings ++ site.includeScaladoc(): _*)
  .settings(
    name := "fellowship-of-the-bus-lib-root",
    site.addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), "latest/api"),
    autoAPIMappings := true, // our scaladoc should link against our dependencies
    git.remoteRepo := "git@github.com:fellowship-of-the-bus/lib.git"
  )
