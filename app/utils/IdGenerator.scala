package utils

import java.util.UUID
import javax.inject.Singleton

trait IdGenerator {
  def generateId(): String
}

@Singleton()
class IdGeneratorImpl extends IdGenerator {
  def generateId() = UUID.randomUUID().toString
}
