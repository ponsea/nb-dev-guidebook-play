package utils

import java.time.LocalDateTime
import javax.inject.Singleton

trait SystemDateTime {
  def now(): LocalDateTime
}

@Singleton()
class SystemDateTimeImpl extends SystemDateTime {
  def now() = LocalDateTime.now()
}
