package services

import javax.inject.{Inject, Singleton}
import scala.concurrent.{Future, ExecutionContext}
import utils.Crypto
import persistence.UserRepository
import models.User

@Singleton()
class AuthService @Inject()(userRepository: UserRepository)(implicit crypto: Crypto,
                                                            ec: ExecutionContext) {

  type SessionToken = (String, String) // SESSION_KEY -> "<< user id >>"

  val SESSION_KEY = "userId"

  def authenticate(email: String, password: String): Future[Option[SessionToken]] = {
    userRepository.findByEmail(email).map {
      _.flatMap { user =>
        if (user.passwordEquals(password))
          Some(SESSION_KEY -> user.id.value)
        else
          None
      }
    }
  }
}
