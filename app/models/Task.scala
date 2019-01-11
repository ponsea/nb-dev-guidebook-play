package models

import java.time.LocalDateTime
import utils.{IdGenerator, SystemDateTime}

case class Task(id: TaskId,
                name: String,
                isFinished: Boolean,
                userId: UserId,
                deadline: Option[LocalDateTime],
                createdAt: LocalDateTime,
                updatedAt: LocalDateTime) {
  def canEditBy(editorId: UserId): Boolean = this.userId == editorId

  def updated(
      name: String = this.name,
      isFinished: Boolean = this.isFinished,
      deadline: Option[LocalDateTime] = this.deadline)(implicit sdt: SystemDateTime): Task = {
    this.copy(name = name, isFinished = isFinished, deadline = deadline, updatedAt = sdt.now())
  }
}

case class TaskId(value: String) extends AnyVal

object TaskId {
  def newId()(implicit idGen: IdGenerator) = TaskId(idGen.generateId())
}
