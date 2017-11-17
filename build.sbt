name := "order_manager"

version := "0.1"

scalaVersion := "2.12.2"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
  
libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += "com.h2database" % "h2" % "1.4.194"
libraryDependencies += "org.projectlombok" % "lombok" % "1.16.18"

libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.1.0" % Test
testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")
