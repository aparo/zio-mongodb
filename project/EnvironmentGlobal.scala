import scala.collection.mutable
import scala.util.Try
import better.files._

object EnvironmentGlobal {
  def appName: String = sys.env.getOrElse("APP_NAME", "megl")

  lazy val CI_MEGL: Boolean = sys.env.contains("CI_MEGL")

  def sonatypeHost: String = {
    val ssl =
      if (sys.env.getOrElse("SONATYPE_USE_SSL", "true") == "true") "s" else ""
    val server = sys.env.getOrElse("SONATYPE_HOST", "maven.megl.io")
    s"http$ssl://$server"
  }
}
