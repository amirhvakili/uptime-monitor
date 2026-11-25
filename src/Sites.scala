import java.nio.file.{Files, Paths}
import upickle.default.*

case class Site(name: String, url: String) derives ReadWriter

object Sites:
  def load(path: String): Seq[Site] =
    read[Seq[Site]](Files.readString(Paths.get(path)))