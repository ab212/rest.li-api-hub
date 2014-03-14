import sbt._
import sbt.Keys._
import play.Project._
import com.linkedin.sbt._

import scala.xml.transform.RewriteRule

object ApplicationBuild extends Build with restli.All {

  val baseSettings =  super.settings ++ org.sbtidea.SbtIdeaPlugin.settings ++ Seq(
    organization := "com.linkedin.pegasus.gorestli",
    version := "0.0.1",
    resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome + "/.ivy2/local"))(Resolver.ivyStylePatterns),
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
  )

  val appDependencies = Seq(
    "com.fasterxml.jackson.module" % "jackson-module-scala_2.10" % "2.2.2",
    "com.typesafe.play" % "play-cache_2.10" % "2.2.1",
    "joda-time" % "joda-time" % "2.1",
    "org.apache.zookeeper" % "zookeeper" % "3.4.6",
    "org.apache.lucene" % "lucene-core" % "4.7.0",
    "org.apache.lucene" % "lucene-analyzers-common" % "4.7.0",
    "org.apache.lucene" % "lucene-queryparser" % "4.7.0",
    "com.linkedin.pegasus" % "restli-common" % "1.15.0",
    "com.linkedin.pegasus" % "restli-client" % "1.15.0",
    "com.linkedin.pegasus" % "restli-server" % "1.15.0",
    "com.linkedin.pegasus" % "restli-docgen" % "1.15.0"
  )

  lazy val main = play.Project("rest-search-frontend", path=file("frontend"))
    .dependsOn(dataTemplates)
    .settings(libraryDependencies ++= appDependencies)
    .settings(baseSettings:_*)


  lazy val dataTemplates = play.Project("rest-search-data-templates", path=file("data-templates"))
    .compilePegasus()
    .settings(libraryDependencies += "com.linkedin.pegasus" % "data" % "1.15.0")
    .settings(libraryDependencies += "com.linkedin.pegasus" % "restli-common" % "1.15.0")
    .settings(baseSettings: _*)

  override lazy val rootProject = Some(main)
}
