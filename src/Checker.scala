import scala.util.control.NonFatal

case class Result(site: Site, up: Boolean, status: Option[Int], millis: Long, error: Option[String])

object Checker:
  def check(site: Site): Result =
    val start = System.nanoTime()
    def elapsed = (System.nanoTime() - start) / 1_000_000
    try
      val response = requests.get(site.url, readTimeout = 10000, connectTimeout = 10000, check = false)
      Result(site, response.statusCode < 400, Some(response.statusCode), elapsed, None)
    catch
      case NonFatal(e) =>
        Result(site, false, None, elapsed, Some(e.getMessage))