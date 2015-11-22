import de.johoop.jacoco4sbt.JacocoPlugin._
import com.typesafe.config._
import aether.Aether._

val conf = ConfigFactory.parseFile(new File("src/main/resources/app.version")).resolve()
val appVersion = conf.getString("application.version")

name := "js-homepage-frontend"

organization := "info.seltenheim"

organizationName  := "Julius Seltenheim"

version := appVersion

scalaVersion := "2.11.6"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
	//misc
	"joda-time" % "joda-time" % "2.6"
	, "commons-io" % "commons-io" % "2.4"
	, "commons-codec" % "commons-codec" % "1.10"
	, "org.apache.commons" % "commons-lang3" % "3.4"
	// javascript libs
	, "org.webjars" % "angularjs" % "1.4.7"
  , "org.webjars" % "requirejs" % "2.1.20"

	//mongo  
	//, "info.schleichardt" %% "play-2-embed-mongo" % "0.5.0"
	, "org.mongodb" % "mongo-java-driver" % "2.12.4"
	//jacoco
    , "org.jacoco" % "org.jacoco.core" % "0.7.2.201409121644" artifacts(Artifact("org.jacoco.core", "jar", "jar"))
	, "org.jacoco" % "org.jacoco.report" % "0.7.2.201409121644" artifacts(Artifact("org.jacoco.report", "jar", "jar"))
)     

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

disablePlugins(PlayLayoutPlugin)

seq(aetherSettings: _*)

seq(aetherPublishSettings: _*)

jacoco.settings

javaOptions in Test += "-Dlogger.resource=logger-test.xml"

parallelExecution in jacoco.Config := false

jacoco.excludes in jacoco.Config := Seq("*views*","*Routes*","*Reverse*")

pipelineStages := Seq(rjs)