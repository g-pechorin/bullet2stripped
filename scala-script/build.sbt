lazy val root = (project in file(".")).
  settings(
    name := "hello sbtw",
    version := "1.0",
    scalaVersion := "2.11.7"
  )

libraryDependencies += "junit" % "junit" % "4.12" % "test"
libraryDependencies += "com.lihaoyi" %% "fastparse" % "0.3.4"

//libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
//libraryDependencies += "org.scala-tools.testing" %% "scalacheck" % "1.12.5" % "test"
