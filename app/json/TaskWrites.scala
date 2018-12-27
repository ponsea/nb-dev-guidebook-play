package json

import play.api.libs.json.{Json, Writes, JsString}
import models.{Task, TaskId}

trait TaskWrites extends UserWrites {
  implicit val taskIdWrites = Writes[TaskId](id => JsString(id.value))
  implicit val defaultTaskWrites = Json.writes[Task]
}
