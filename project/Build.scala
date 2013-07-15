import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "js_homepage"
    val appVersion      = "3.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
      javaCore
      , javaJdbc
      , "joda-time" % "joda-time" % "2.1"
      , "commons-io" % "commons-io" % "1.3.2"
      , "org.ektorp" % "org.ektorp" % "1.3.0"
      
      //belongs together
      , "org.springframework" % "spring-context" % "3.1.2.RELEASE"
      , "cglib" % "cglib" % "2.2.2"
      
      
      , "info.schleichardt" %% "play-embed-mongo" % "0.2.1"
      , "org.mongodb" % "mongo-java-driver" % "2.10.1"
            
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
