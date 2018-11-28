package json

import java.time.LocalDateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

case class TaskCreatingInput(name: String, deadline: Option[LocalDateTime])

object TaskCreatingInput {
  implicit val reads = (
    (JsPath \ "name").read[String](minLength[String](1) keepAnd maxLength[String](256)) and
      (JsPath \ "deadline").readNullable[LocalDateTime]
  )(apply _)
}
