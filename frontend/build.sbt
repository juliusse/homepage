import de.johoop.jacoco4sbt.JacocoPlugin._
import com.typesafe.config._
import aether.Aether._

val conf = ConfigFactory.parseFile(new File("conf/app.version")).resolve()
val appVersion = conf.getString("application.version")

name := "js-homepage-frontend"

organization := "info.seltenheim"

organizationName  := "Julius Seltenheim"

version := appVersion

libraryDependencies ++= Seq(
	//misc
	"joda-time" % "joda-time" % "2.6"
	, "commons-io" % "commons-io" % "2.4"
	, "commons-codec" % "commons-codec" % "1.10"
	//belongs together
	, "org.springframework" % "spring-context" % "3.2.5.RELEASE"
	, "cglib" % "cglib" % "2.2.2"
	//mongo  
	, "info.schleichardt" %% "play-embed-mongo" % "0.2.1"
	, "org.mongodb" % "mongo-java-driver" % "2.12.4"
	//jacoco
    , "org.jacoco" % "org.jacoco.core" % "0.7.2.201409121644" artifacts(Artifact("org.jacoco.core", "jar", "jar"))
	, "org.jacoco" % "org.jacoco.report" % "0.7.2.201409121644" artifacts(Artifact("org.jacoco.report", "jar", "jar"))
)     

seq(aetherSettings: _*)

seq(aetherPublishSettings: _*)

play.Project.playJavaSettings ++ Seq(jacoco.settings:_*)

parallelExecution in jacoco.Config := false

jacoco.excludes in jacoco.Config := Seq("views*","*Routes*","*Reverse*") 