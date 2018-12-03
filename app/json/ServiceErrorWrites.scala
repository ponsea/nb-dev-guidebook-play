package json

import play.api.libs.json.{Json, Writes, JsString, JsArray}
import services.ServiceError

trait ServiceErrorWrites {
  implicit val defaultServiceErrorWrites = Writes[ServiceError] { error =>
    Json.obj(
      "message" -> error.message,
      "args" -> JsArray(error.args.map(v => JsString(v.toString)))
    )
  }
}
