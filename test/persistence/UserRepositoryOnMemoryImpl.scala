package persistence

import javax.inject.Singleton
import scala.concurrent.Future
import models.{User, UserId}

@Singleton()
class UserRepositoryOnMemoryImpl(initialData: Set[User] = Set.empty) extends UserRepository {
  private var users = initialData

  def findAll() = Future.successful(users.toSeq)

  def findById(userId: UserId) = {
    Future.successful(users.find(_.id == userId))
  }

  def findByEmail(email: String) = {
    Future.successful(users.find(_.email == email))
  }

  def emailExists(email: String) = {
    Future.successful(users.exists(_.email == email))
  }

  def save(newUser: User) = {
    users.find(_.id == newUser.id).foreach(old => users -= old)
    users += newUser
    Future.successful(newUser)
  }

  def delete(userId: UserId) = {
    users.find(_.id == userId).foreach(users -= _)
    Future.successful(())
  }
}
