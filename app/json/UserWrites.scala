package json

import java.time.LocalDateTime
import play.api.libs.json.{JsPath, Json, Writes, JsString}
import play.api.libs.functional.syntax._
import models.{User, UserId}

trait UserWrites {
  implicit val userIdWrites = Writes[UserId](id => JsString(id.value))

  implicit def defaultUserWrites: Writes[User] = publicUserWrites

  val publicUserWrites: Writes[User] = (
    (JsPath \ "id").write[UserId]
      and (JsPath \ "name").write[String]
      and (JsPath \ "createdAt").write[LocalDateTime]
      and (JsPath \ "updatedAt").write[LocalDateTime]
  )(user => (user.id, user.name, user.createdAt, user.updatedAt))

  val privateUserWrites: Writes[User] = (
    (JsPath \ "id").write[UserId]
      and (JsPath \ "name").write[String]
      and (JsPath \ "email").write[String]
      and (JsPath \ "createdAt").write[LocalDateTime]
      and (JsPath \ "updatedAt").write[LocalDateTime]
  )(user => (user.id, user.name, user.email, user.createdAt, user.updatedAt))
}
