package utils

import java.time.LocalDateTime

trait SystemDateTime {
  def now(): LocalDateTime
}

object SystemDateTime {
  implicit val defaultImpl = new SystemDateTime {
    def now() = LocalDateTime.now()
  }
}
