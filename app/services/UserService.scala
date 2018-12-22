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
}
