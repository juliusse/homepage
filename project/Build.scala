import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "JuliusSeltenheim-Play2"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      javaCore, javaJdbc, javaEbean,
      "joda-time" % "joda-time" % "2.1",
      "commons-io" % "commons-io" % "1.3.2",
      "org.ektorp" % "org.ektorp" % "1.3.0"
            
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
