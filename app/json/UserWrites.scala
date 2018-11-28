package json

import play.api.libs.json.{Json, Writes, JsString}
import models.{User, UserId}

object UserWrites {
  implicit val userIdWrites = Writes[UserId](id => JsString(id.value))
  implicit val defaultUserWrites = Json.writes[User]
}
