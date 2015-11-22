// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.3")

addSbtPlugin("de.johoop" % "jacoco4sbt" % "2.1.1")

addSbtPlugin("no.arktekk.sbt" % "aether-deploy" % "0.10")

addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.7")
