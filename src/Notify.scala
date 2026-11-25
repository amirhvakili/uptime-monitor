import java.nio.charset.StandardCharsets

object Notify:
  // Sends a push notification through ntfy.sh. The topic comes from the NTFY_TOPIC environment variable.
  def send(message: String, title: String = "Uptime alert"): Unit =
    val topic = sys.env.getOrElse(
      "NTFY_TOPIC",
      throw new IllegalStateException("NTFY_TOPIC is not set")
    )
    requests.post(
      s"https://ntfy.sh/$topic",
      data = message.getBytes(StandardCharsets.UTF_8),
      headers = Map("Title" -> title),
      readTimeout = 10000,
      connectTimeout = 10000
    )