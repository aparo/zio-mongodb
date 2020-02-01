import PlatformDependencies.ZIO
import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys._
import xerial.sbt.Sonatype.autoImport._

import scala.util.Try

object Common {
  val appName = EnvironmentGlobal.appName

  lazy val commonGeneric = Seq(
    homepage := Some(url("https://www.megl.io")),
    licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0")),
    organization := "io.megl",
    scalaVersion := Versions.scala,
    crossScalaVersions := Versions.crossScalaVersions,
    organizationName := "Paro Consulting",
    startYear := Some(2018),
    homepage := Some(url("https://github.com/aparo/zio-mongodb")),
    scmInfo := Some(
      ScmInfo(url("https://github.com/aparo/zio-mongodb"), "git@github.com:aparo/zio-mongodb.git")),
    developers := List(Developer("aparo", "Alberto Paro", "alberto.paro@gmail.com", url("https://github.com/aparo"))),
    cancelable := true,
    sourcesInBase := false,
    javaOptions +=
      s"-Dmeglio.sbt.root=${(ThisBuild / baseDirectory).value.getCanonicalFile}",
    version := version.value.replace('+', '-'),
    concurrentRestrictions := {

      val limited =
        Try(sys.env.getOrElse("SBT_TASK_LIMIT", "4").toInt).getOrElse {
          throw new IllegalArgumentException(
            "SBT_TASK_LIMIT should be an integer value"
          )
        }
      Seq(Tags.limitAll(limited))
    },
    ivyLoggingLevel := UpdateLogging.Quiet,
    // BLOCKED: https://github.com/coursier/coursier/issues/349
    // conflictManager := ConflictManager.strict,
    // makes it really easy to use a RAM disk - when the environment variable
    // exists, the SBT_VOLATILE_TARGET/target directory is created as a side
    // effect
    target := {
      sys.env.get("SBT_VOLATILE_TARGET") match {
        case None => target.value
        case Some(base) =>
          file(base) / target.value.getCanonicalPath.replace(':', '_')
      }
    },
    // When the environment variable exists, the
    // SBT_VOLATILE_TARGET/java.io.tmpdir directory is created as a side effect
    javaOptions ++= {
      sys.env.get("SBT_VOLATILE_TARGET") match {
        case None => Nil
        case Some(base) =>
          val tmpdir = s"$base/java.io.tmpdir"
          file(tmpdir).mkdirs()
          s"-Djava.io.tmpdir=$tmpdir" :: Nil
      }
    },
    javaOptions += s"-Dmeglio.sbt.name=${name.value}",
    javaOptions ++= /* JavaSpecificFlags ++ */ Seq(
      "-Xss2m",
      "-Dfile.encoding=UTF8"
    ),
    dependencyOverrides ++= Seq(
      // scala-lang is always used during transitive ivy resolution (and
      // potentially thrown out...)
      "org.scala-lang" % "scala-compiler" % scalaVersion.value,
      "org.scala-lang" % "scala-library" % scalaVersion.value,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "org.scala-lang" % "scalap" % scalaVersion.value
    ),
    resolvers ++= {
      val name = EnvironmentGlobal.appName
      val host = EnvironmentGlobal.sonatypeHost
      Seq(
        //        Opts.resolver.mavenLocalFile,
        s"$name Nexus Repository".at(s"$host/repository/maven-releases/")
      )
    },
    addCompilerPlugin(
      ("org.scalamacros" % "paradise" % "2.1.1").cross(CrossVersion.full)
    ),
    addCompilerPlugin(
      ("org.spire-math" %% "kind-projector" % "0.9.9").cross(CrossVersion.binary)
    ),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    credentials ++= (
      for {
        username <- Option(System.getenv().get("SONATYPE_USERNAME"))
        password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
      } yield
        Credentials(
          "Sonatype Nexus Repository Manager",
          "oss.sonatype.org",
          username,
          password
        )
    ).toSeq
  ) ++
    Licensing.settings

  def crossFlags(scalaVersion: String): Seq[String] =
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, 11)) => Seq("-Yinline-warnings")
      case Some((2, 12)) => Seq("-opt-warnings")
      case _             => Nil
    }

  lazy val commonJvmSettings: Seq[Def.Setting[_]] = Seq(
    cancelable in Global := true,
    fork in Test := false,
    scalacOptions ++= Seq(
      "-encoding",
      "utf8",
      "-deprecation",
      "-feature",
      "-unchecked",
      //      "-Xlint",
      "-language:postfixOps",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      //      "-Ywarn-value-discard",
      //      "-Ywarn-unused",
      "-Ywarn-unused-import",
      "-Yrangepos"
    ) ++ crossFlags(scalaVersion.value),
    scalacOptions ++= (
      if (priorTo2_13(scalaVersion.value))
        Seq(
          "-Xfuture",
          "-Yno-adapted-args",
          "-Ypartial-unification"
        )
      else
        Seq(
          "-Ymacro-annotations"
        )
    )
  )

  lazy val zioTests = Seq(
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    libraryDependencies ++= DependencyHelpers.test(ZIO.test, ZIO.testSbt)
  )

  lazy val settings = Seq(
    fork in Test := false,
    maxErrors := 1000
  ) ++ Licensing.settings

  lazy val scoverageSettings = Seq(
    coverageHighlighting := true,
    coverageExcludedPackages := "com\\.megl\\.console\\.html\\..*"
  )

  lazy val noPublishSettings = Seq(
    skip in publish := true,
    publishArtifact := false,
    publish := {},
    publishLocal := {},
    publishArtifact := false
  )

  //java options only for JVM
  lazy val javaOptionsJVM = Seq(
    "-encoding",
    "UTF-8",
    "-source",
    "1.8",
    "-target",
    "1.8",
    "-XX:+UseG1GC",
    "-XX:MaxGCPauseMillis=20",
    "-XX:InitiatingHeapOccupancyPercent=35",
    "-Dsun.jnu.encoding=UTF-8",
    "-Dfile.encoding=UTF-8",
    "-Djava.awt.headless=true",
    "-Djava.net.preferIPv4Stack=true"
  )

  //scala options only for JVM
  lazy val scalacOptionsJVM = Seq(
    "-encoding",
    "UTF-8",
    "-target:jvm-1.8"
  )

  lazy val publicationSettings = Seq(
    publishTo := sonatypePublishToBundle.value,
  )

  def priorTo2_13(scalaVersion: String): Boolean =
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, minor)) if minor < 13 => true
      case _                              => false
    }

}
