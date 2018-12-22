package persistence

import javax.inject.Singleton
import java.time.LocalDateTime
import scala.concurrent.Future
import models.{User, UserId}

@Singleton()
class UserRepositoryOnMemoryImpl extends UserRepository {
  private val dateTime = LocalDateTime.parse("2019-04-01T00:00:00")
  private var users = Set(
    User(
      UserId("1"),
      "相生 葵",
      "aoi@example.com",
      // ↓SHA256("nextbeat" + salt)
      "b0b1f71190c0ea7018dbbd96a930c3fab63be447d98349de78a82cc6a5964076",
      "3kRPLNNnyUBI4QLFV9b4", // salt
      dateTime,
      dateTime
    ),
    User(
      UserId("2"),
      "阿江 愛",
      "ai@example.com",
      // ↓SHA256("nextbeat" + salt)
      "2fdc314e91c6a00394885dba26c6fed19adf9adb367e653a9849d12bdbc908fc",
      "Sh72PWhmjIdw79G7h9CS", // salt
      dateTime,
      dateTime
    ),
  )

  def findAll() = Future.successful(users.toSeq)

  def findByEmail(email: String) = {
    Future.successful(users.find(_.email == email))
  }
}
