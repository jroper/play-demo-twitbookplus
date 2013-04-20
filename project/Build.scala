import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "twitbookplus"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,

    "com.google.inject" % "guice" % "3.0",

    "org.mongojack" %% "play-mongojack" % "2.0.0-RC2",
    "org.mongojack" % "mongojack" % "2.0.0-RC5",
    "org.mindrot" % "jbcrypt" % "0.3m",

    "org.webjars" % "webjars-play" % "2.1.0-1",
    "org.webjars" % "bootstrap" % "2.1.1",
    "org.webjars" % "knockout" % "2.2.1",
    "org.webjars" % "requirejs" % "2.1.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(

  )

}
