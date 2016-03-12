resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.4")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.9")
addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.3.3")

// for android-lib
addSbtPlugin("com.hanhuy.sbt" % "android-sdk-plugin" % "1.5.19")

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.5"


