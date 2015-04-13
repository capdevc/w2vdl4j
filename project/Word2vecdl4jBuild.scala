import sbt._
import sbt.Keys._

object Word2vecdl4jBuild extends Build {

  lazy val word2vecdl4j = Project(
    id = "word2vecdl4j",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Word2VecDl4j",
      organization := "com.pyaanalytics",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.4",

      libraryDependencies ++= Seq(
        "org.apache.spark" %% "spark-core" % "1.3.0" % "provided",
        "org.apache.hadoop" % "hadoop-client" % "2.4.0" % "provided",
        "com.github.scopt" %% "scopt" % "3.2.0",
        // "org.nd4j" % "nd4j-jcublas-6.0" % "0.0.3.5.5.3-SNAPSHOT",
        "org.nd4j" % "nd4j-jblas" % "0.0.3.5.5.3-SNAPSHOT",
        "org.nd4j" % "nd4j-api" % "0.0.3.5.5.3-SNAPSHOT",
        "org.deeplearning4j" % "dl4j-spark" % "0.0.3.3.3.alpha1-SNAPSHOT",
        "org.deeplearning4j" % "dl4j-spark-nlp" % "0.0.3.3.3.alpha1-SNAPSHOT"
      ),

      resolvers ++= Seq(
        "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
        Resolver.mavenLocal
      )
      // add other settings here
    )
  )
}
