import scala.util.control.NonFatal

object Main:
  def main(args: Array[String]): Unit =
    val sites = Sites.load("/app/sites.json")
    val results = sites.map(Checker.check)
    val previous = State.load("/app/output/state.json")

    results.foreach { r =>
      val state = if r.up then "UP  " else "DOWN"
      val status = r.status.map(_.toString).getOrElse("no response")
      println(s"$state ${r.site.name}: $status in ${r.millis} ms")
      r.error.foreach(e => println(s"      reason: $e"))
    }

    val changes = results.filter(r => previous.get(r.site.name).exists(_ != r.up))
    changes.foreach { r =>
      val what = if r.up then "is back UP" else "went DOWN"
      println(s"CHANGE: ${r.site.name} $what")
    }

    History.append("/app/output/history.csv", results)
    State.save("/app/output/state.json", results)

    if changes.nonEmpty then
      val message = changes
        .map(r => if r.up then s"${r.site.name} is back UP" else s"${r.site.name} went DOWN")
        .mkString("\n")
      try Notify.send(message)
      catch case NonFatal(e) => println(s"Could not send notification: ${e.getMessage}")