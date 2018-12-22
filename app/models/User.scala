package models

import java.time.LocalDateTime
import utils.IdGenerator

case class User(id: UserId,
                name: String,
                email: String,
                hashedPassword: String,
                salt: String,
                createdAt: LocalDateTime,
                updatedAt: LocalDateTime)

case class UserId(value: String) extends AnyVal

object UserId {
  def newId()(implicit idGen: IdGenerator) = UserId(idGen.generateId())
}
