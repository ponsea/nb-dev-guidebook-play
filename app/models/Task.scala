package models

import java.time.LocalDateTime
import utils.{IdGenerator, SystemDateTime}

case class Task(id: TaskId, // タスクのID
                name: String, // タスク名
                isFinished: Boolean, // タスクが終了したかどうか
                userId: UserId, // どのユーザーに属するか
                deadline: Option[LocalDateTime], // タスクの期限 (任意)
                createdAt: LocalDateTime, // 作成日時
                updatedAt: LocalDateTime) { // 最終更新日時
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
