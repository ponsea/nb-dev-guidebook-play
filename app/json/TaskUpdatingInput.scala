package json

import java.time.LocalDateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

case class TaskUpdatingInput(name: Option[String],
                             isFinished: Option[Boolean],
                             deadline: Option[Option[LocalDateTime]])

object TaskUpdatingInput {
  implicit val reads = (
    (JsPath \ "name").readNullable[String](minLength[String](1) keepAnd maxLength[String](255))
      and (JsPath \ "isFinished").readNullable[Boolean]
      and (JsPath \ "deadline").readOptional[LocalDateTime]
  )(apply _)
}
