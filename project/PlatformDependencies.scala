import sbt._

object PlatformDependencies {

  object Cats {
    val core = "org.typelevel" %% "cats-core" % Versions.cats
    val laws = "org.typelevel" %% "cats-laws" % Versions.cats
    val all = "org.typelevel" %% "cats" % Versions.cats
    val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
  }

  object Circe {
    lazy val core = "io.circe" %% "circe-core" % Versions.circe
    lazy val generic =
      "io.circe" %% "circe-generic" % Versions.circe
    lazy val genericExtras =
      "io.circe" %% "circe-generic-extras" % Versions.circe
    lazy val jawn = "io.circe" %% "circe-jawn" % Versions.circe
    lazy val refined =
      "io.circe" %% "circe-refined" % Versions.circe
    lazy val parser =
      "io.circe" %% "circe-parser" % Versions.circe
    lazy val testing =
      "io.circe" %% "circe-testing" % Versions.circe
    lazy val derivation = "io.circe" %% "circe-derivation-annotations" % Versions.circeDerivation

    lazy val yaml = "io.circe" %% "circe-yaml" % "0.8.0"
  }

  object Enumeratum {
    lazy val core =
      "com.beachape" %% "enumeratum" % Versions.enumeratum
    lazy val circe =
      "com.beachape" %% "enumeratum-circe" % Versions.enumeratumCirce
  }

  object MongoDB {
    lazy val driver = "org.mongodb" % "mongodb-driver-reactivestreams" % Versions.mongodb
  }

  object ScalaTest {
    lazy val test =
      "org.scalatest" %% "scalatest" % Versions.scalaTest
    lazy val scalactic =
      "org.scalactic" %% "scalactic" % Versions.scalaTest
  }
  object Specs2 {
    lazy val core = "org.specs2" %% "specs2-core" % Versions.specs2
    lazy val mock = "org.specs2" %% "specs2-mock" % Versions.specs2
    lazy val junit =
      "org.specs2" %% "specs2-junit" % Versions.specs2
    lazy val scalaCheck =
      "org.specs2" %% "specs2-scalacheck" % Versions.specs2
  }
  object ZIO {
    lazy val core = "dev.zio" %% "zio" % Versions.zio
    lazy val streams = "dev.zio" %% "zio-streams" % Versions.zio
    lazy val test = "dev.zio" %% "zio-test" % Versions.zio
    lazy val testSbt = "dev.zio" %% "zio-test-sbt" % Versions.zio

    lazy val interopReactiveStreams = "dev.zio" %% "zio-interop-reactivestreams" % "1.0.3.5-RC2"
  }
}
