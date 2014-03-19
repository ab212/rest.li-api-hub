import sbt._
import sbt.Keys._
import play.Project._
import com.linkedin.sbt._

import scala.xml.transform.RewriteRule

// javax.jms#jms;1.1!jms.jar
// com.sun.jdmk#jmxtools;1.2.1!jmxtools.jar
// com.sun.jmx#jmxri;1.2.1!jmxri.jar

object ApplicationBuild extends Build with restli.All {

  val baseSettings =  super.settings ++ org.sbtidea.SbtIdeaPlugin.settings ++ Seq(
    organization := "com.linkedin.pegasus.gorestli",
    version := "0.0.1",
    name := "restli-apihub",
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
  )

  val appDependencies = Seq(
    "com.fasterxml.jackson.module" % "jackson-module-scala_2.10" % "2.2.2",
    "com.typesafe.play" % "play-cache_2.10" % "2.2.1",
    "joda-time" % "joda-time" % "2.2",
    "org.apache.zookeeper" % "zookeeper" % "3.4.2" excludeAll(
      ExclusionRule(organization = "com.sun.jdmk"),
      ExclusionRule(organization = "com.sun.jmx"),
      ExclusionRule(organization = "javax.jms")
    ),
    "org.apache.lucene" % "lucene-core" % "4.2.0",
    "org.apache.lucene" % "lucene-analyzers-common" % "4.2.0",
    "org.apache.lucene" % "lucene-queryparser" % "4.2.0",
    "com.linkedin.pegasus" % "restli-common" % "1.15.3",
    "com.linkedin.pegasus" % "restli-client" % "1.15.3",
    "com.linkedin.pegasus" % "restli-server" % "1.15.3",
    "com.linkedin.pegasus" % "restli-docgen" % "1.15.3"
  )

  lazy val main = play.Project("rest-search-frontend", path=file("frontend"))
    .dependsOn(dataTemplates)
    .settings(libraryDependencies ++= appDependencies)
    .settings(baseSettings:_*)


  lazy val dataTemplates = play.Project("rest-search-data-templates", path=file("data-templates"))
    .compilePegasus()
    .settings(libraryDependencies += "com.linkedin.pegasus" % "data" % "1.15.3")
    .settings(libraryDependencies += "com.linkedin.pegasus" % "restli-common" % "1.15.3")
    .settings(libraryDependencies += "com.linkedin.pegasus" % "d2-schemas" % "1.15.3")
    .settings(baseSettings: _*)

  override lazy val rootProject = Some(main)
}
