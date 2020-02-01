import ReleaseTransformations._

inThisBuild(
  Seq(
    organization := "io.megl",
    scalaVersion := "2.12.10",
    parallelExecution := false,
    scalafmtOnCompile := true
  )
)

lazy val root =
  project
    .in(file("."))
    .settings(Common.noPublishSettings)
    .aggregate(
      `zio-mongodb`
    )

lazy val `zio-mongodb` = ProjectUtils
  .setupJVMProject("zio-mongodb")
  .settings(
    moduleName := "zio-mongodb"
  )
  .settings(Dependencies.zioMongoDB)
  .settings(Common.zioTests)

// Releasing
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("publish"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)
