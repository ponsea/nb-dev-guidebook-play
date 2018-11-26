package models

import java.time.LocalDateTime

case class Task(id: TaskId,
                name: String,
                isFinished: Boolean,
                userId: UserId,
                deadline: Option[LocalDateTime],
                createdAt: LocalDateTime,
                updatedAt: LocalDateTime)

case class TaskId(value: String) extends AnyVal
