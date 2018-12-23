package persistence.db

import play.api.db.slick.HasDatabaseConfig
import slick.jdbc.JdbcProfile
import java.time.LocalDateTime
import models.{User, UserId}

trait UsersTableConfig extends ColumnTypeMappings {
  self: HasDatabaseConfig[JdbcProfile] =>
  import profile.api._

  class UsersTable(tag: Tag) extends Table[User](tag, "users") {
    def id = column[UserId]("id", O.PrimaryKey)
    def name = column[String]("name")
    def email = column[String]("email", O.Unique)
    def hashedPassword = column[String]("hashed_password")
    def salt = column[String]("salt")
    def createdAt = column[LocalDateTime]("created_at")
    def updatedAt = column[LocalDateTime]("updated_at")

    def * =
      (id, name, email, hashedPassword, salt, createdAt, updatedAt) <> (User.tupled, User.unapply)
  }
}
