package utils

import java.util.UUID

trait IdGenerator {
  def generateId(): String
}

object IdGenerator {
  implicit val defaultImpl = new IdGenerator {
    def generateId() = UUID.randomUUID().toString
  }
}
