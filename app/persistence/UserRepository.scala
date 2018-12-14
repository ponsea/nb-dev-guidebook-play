package persistence

import models.{User, UserId}
import scala.concurrent.Future

trait UserRepository {
  def findByEmail(email: String): Future[Option[User]]
}
