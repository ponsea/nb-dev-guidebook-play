package persistence

import models.{User, UserId}
import scala.concurrent.Future

trait UserRepository {
  def findAll(): Future[Seq[User]]
  def findById(userId: UserId): Future[Option[User]]
  def findByEmail(email: String): Future[Option[User]]
  def emailExists(email: String): Future[Boolean]
  def save(user: User): Future[User]
  def delete(userId: UserId): Future[Unit]
}
