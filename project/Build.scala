import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "JuliusSeltenheim-Play2"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
      javaCore
      , javaJdbc
      , "joda-time" % "joda-time" % "2.1"
      , "commons-io" % "commons-io" % "1.3.2"
      , "org.ektorp" % "org.ektorp" % "1.3.0"
      , "org.springframework" % "spring-context" % "3.1.2.RELEASE"
            
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
