import sbt._
import sbt.Keys._

object Dependencies {
  import PlatformDependencies._

  lazy val zioMongoDB = Def.settings {
    libraryDependencies ++= DependencyHelpers.compile(
      Circe.derivation,
      Circe.parser,
      Enumeratum.circe,
      ZIO.core,
      ZIO.streams,
      MongoDB.driver,
      ZIO.interopReactiveStreams) ++
      DependencyHelpers.test(
        ScalaTest.test,
        "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "2.2.0"
      )
  }

}
