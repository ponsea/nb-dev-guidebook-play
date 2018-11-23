package models

import java.time.LocalDateTime

case class User(id: UserId,
                name: String,
                email: String,
                hashedPassword: String,
                salt: String,
                createdAt: LocalDateTime,
                updatedAt: LocalDateTime)

case class UserId(value: Long) extends AnyVal
