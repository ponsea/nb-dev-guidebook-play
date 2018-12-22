package services

import javax.inject.{Inject, Singleton}
import java.time.LocalDateTime
import scala.concurrent.{Future, ExecutionContext}
import utils.{IdGenerator, SystemDateTime, Crypto}
import persistence.UserRepository
import models.{User, UserId}

@Singleton()
class UserService @Inject()(userRepository: UserRepository)(implicit idGen: IdGenerator,
                                                            sdt: SystemDateTime,
                                                            crypto: Crypto,
                                                            ec: ExecutionContext) {
  def getAll(): Future[Seq[User]] = userRepository.findAll()

  def get(userId: UserId): Future[Option[User]] = {
    userRepository.findById(userId)
  }

  def create(name: String, email: String, password: String): Future[Either[UserError, User]] = {
    userRepository.emailExists(email).flatMap {
      _ match {
        case true => Future.successful(Left(UserEmailDuplicated(email)))
        case false => {
          val salt = crypto.randomString(20)
          val hashedPassword = crypto.digest(password + salt)
          val newUser =
            User(UserId.newId(), name, email, hashedPassword, salt, sdt.now(), sdt.now())
          userRepository.save(newUser).map(Right(_))
        }
      }
    }
  }

  def delete(userId: UserId, deleterId: UserId): Future[Either[UserError, Unit]] = {
    val notFound: Future[Either[UserError, Unit]] =
      Future.successful(Left(UserNotFound(userId)))
    val permissionDenied: Future[Either[UserError, Unit]] =
      Future.successful(Left(UserPermissionDenied))

    userRepository.findById(userId).flatMap { maybeUser =>
      maybeUser.fold(notFound) { user =>
        if (user.canEditBy(deleterId)) {
          userRepository.delete(userId).map(Right(_))
        } else {
          permissionDenied
        }
      }
    }
  }
}
