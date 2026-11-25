import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import scala.util.Try
import upickle.default.*

object State:
  // Last known state of each site: name -> was it up?
  // Returns an empty map on the very first run, or if the file can't be read.
  def load(path: String): Map[String, Boolean] =
    val file = Paths.get(path)
    Try(read[Map[String, Boolean]](Files.readString(file))).getOrElse(Map.empty)

  // Overwrites the file with the newest state of every site.
  def save(path: String, results: Seq[Result]): Unit =
    val state = results.map(r => r.site.name -> r.up).toMap
    Files.writeString(Paths.get(path), write(state), StandardCharsets.UTF_8)