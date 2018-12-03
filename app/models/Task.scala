package models

import java.time.LocalDateTime
import utils.IdGenerator

case class Task(id: TaskId,
                name: String,
                isFinished: Boolean,
                userId: UserId,
                deadline: Option[LocalDateTime],
                createdAt: LocalDateTime,
                updatedAt: LocalDateTime) {
  def canEditBy(editorId: UserId) = this.userId == editorId
}

case class TaskId(value: String) extends AnyVal

object TaskId {
  def newId()(implicit idGen: IdGenerator) = TaskId(idGen.generateId())
}
