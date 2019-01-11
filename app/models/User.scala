package models

import java.time.LocalDateTime
import utils.{IdGenerator, Crypto}

case class User(id: UserId,
                name: String,
                email: String,
                hashedPassword: String,
                salt: String,
                createdAt: LocalDateTime,
                updatedAt: LocalDateTime) {
  def canEditBy(editorId: UserId): Boolean = this.id == editorId

  def passwordEquals(password: String)(implicit crypto: Crypto): Boolean = {
    this.hashedPassword == crypto.digest(password + this.salt)
  }
}

case class UserId(value: String) extends AnyVal

object UserId {
  def newId()(implicit idGen: IdGenerator) = UserId(idGen.generateId())
}
