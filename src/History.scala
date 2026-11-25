import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.time.Instant

object History:
  private val Header = "timestamp,site,up,status,millis"

  // Adds one row per site to the end of a CSV file. Creates the file (with a header) the first time.
  def append(path: String, results: Seq[Result]): Unit =
    val file = Paths.get(path)
    val timestamp = Instant.now().toString
    val rows = results.map(r => s"$timestamp,${r.site.name},${r.up},${r.status.getOrElse("")},${r.millis}")
    val lines = if Files.exists(file) then rows else Header +: rows
    Files.writeString(
      file,
      lines.mkString("", "\n", "\n"),
      StandardCharsets.UTF_8,
      StandardOpenOption.CREATE,
      StandardOpenOption.APPEND
    )