organization := "com.github.fellowship_of_the_bus"

name := "fellowship-of-the-bus-lib"

// need scalatest also as a build dependency: the build implements a custom reporter
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.5" % "test"

libraryDependencies += "junit" % "junit" % "4.12" % "test"

libraryDependencies += "org.slick2d" % "slick2d-core" % "1.0.1"

// allow others to link against our scaladoc
apiURL := Some(url("https://fellowship-of-the-bus.github.io/lib/latest/api/"))

autoAPIMappings := true  // our scaladoc should link against our dependencies

site.settings

site.includeScaladoc()

ghpages.settings

git.remoteRepo := "git@github.com:fellowship-of-the-bus/lib.git"

scalacOptions ++= Seq("-deprecation", "-feature", "-optimize", "-Yinline-warnings")

// addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.2.1")

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

GhPagesKeys.pushSite <<= GhPagesKeys.pushSite.dependsOn(compile in Compile)

PgpKeys.publishSigned <<= PgpKeys.publishSigned.dependsOn(GhPagesKeys.pushSite)

pomExtra := (
  <url>http://www.github.com/Fellowship-of-the-Bus/lib</url>
  <licenses>
    <license>
      <name>The Apache License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    </license>
  </licenses>
  <scm>
    <connection>scm:git:git@github.com:fellowship-of-the-bus/lib.git</connection>
    <developerConnection>scm:git:git@github.com:fellowship-of-the-bus/lib.git</developerConnection>
    <url>git@github.com:juven/git-demo.git</url>
  </scm>
  <developers>
    <developer>
      <name>Rob Schluntz</name>
      <email>rschlunt@uwaterloo.ca</email>
      <url>http://www.cs.uwaterloo.ca/~rschlunt</url>
    </developer>
  </developers>)

