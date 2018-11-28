package json

import play.api.libs.json.{Json, Writes, JsString}
import models.{Task, TaskId}

object TaskWrites {
  import UserWrites.userIdWrites

  implicit val taskIdWrites = Writes[TaskId](id => JsString(id.value))
  implicit val defaultTaskWrites = Json.writes[Task]
}
