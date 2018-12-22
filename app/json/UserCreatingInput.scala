package json

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

case class UserCreatingInput(name: String, email: String, password: String)

object UserCreatingInput {
  implicit val reads = (
    (JsPath \ "name").read[String](minLength[String](1) keepAnd maxLength[String](255))
      and (JsPath \ "email").read[String](email)
      and (JsPath \ "password").read[String](minLength[String](8) keepAnd maxLength[String](255))
  )(apply _)
}
