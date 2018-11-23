package models

import java.time.LocalDateTime

case class Task(id: TaskId,
                name: String,
                isFinished: Boolean,
                userId: UserId,
                deadline: LocalDateTime,
                createdAt: LocalDateTime,
                updatedAt: LocalDateTime)

case class TaskId(value: Long) extends AnyVal
