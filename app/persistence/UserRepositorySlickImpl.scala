package persistence

import javax.inject.{Singleton, Inject}
import java.time.LocalDateTime
import scala.concurrent.{Future, ExecutionContext}
import slick.jdbc.JdbcProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import models.{User, UserId}
import db.UsersTableConfig

@Singleton()
class UserRepositorySlickImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext)
    extends UserRepository
    with HasDatabaseConfigProvider[JdbcProfile]
    with UsersTableConfig {
  import profile.api._

  private val users = TableQuery[UsersTable]

  def findAll() = db.run {
    users.result
  }

  def findById(userId: UserId) = db.run {
    users.filter(_.id === userId).result.headOption
  }

  def findByEmail(email: String) = db.run {
    users.filter(_.email === email).result.headOption
  }

  def emailExists(email: String) = db.run {
    users.filter(_.email === email).exists.result
  }

  def save(user: User) = db.run {
    users.insertOrUpdate(user).map(_ => user)
  }

  def delete(userId: UserId) = db.run {
    users.filter(_.id === userId).delete.map(_ => ())
  }
}
