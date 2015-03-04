organization := "com.github.fellowship_of_the_bus"

name := "fellowship-of-the-bus-lib"

version := "0.1-SNAPSHOT"

// need scalatest also as a build dependency: the build implements a custom reporter
libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

scalacOptions ++= Seq("-deprecation", "-feature", "-optimize", "-Yinline-warnings")

// This resolver declaration is added by default in SBT 0.12.x
resolvers += Resolver.url(
  "sbt-plugin-releases",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/")
)(Resolver.ivyStylePatterns)

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.2.1")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8.5")

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

