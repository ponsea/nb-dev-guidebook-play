package json

import java.time.LocalDateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

case class TokenCreatingInput(email: String, password: String)

object TokenCreatingInput {
  implicit val reads = (
    (JsPath \ "email").read[String](email)
      and (JsPath \ "password").read[String]
  )(apply _)
}
