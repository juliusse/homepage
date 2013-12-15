name := "js-homepage"

version := "3.0.0"

libraryDependencies ++= Seq(
	javaJdbc
	,cache
	//misc
	, "joda-time" % "joda-time" % "2.1"
	, "commons-io" % "commons-io" % "1.3.2"
	, "org.ektorp" % "org.ektorp" % "1.3.0"
	//belongs together
	, "org.springframework" % "spring-context" % "3.2.5.RELEASE"
	, "cglib" % "cglib" % "2.2.2"
	//mongo  
	, "info.schleichardt" %% "play-embed-mongo" % "0.2.1"
	, "org.mongodb" % "mongo-java-driver" % "2.10.1"
)     

play.Project.playJavaSettings
