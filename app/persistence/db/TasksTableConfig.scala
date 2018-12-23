package persistence.db

import play.api.db.slick.HasDatabaseConfig
import slick.jdbc.JdbcProfile
import java.time.LocalDateTime
import models.{Task, TaskId, UserId}

trait TasksTableConfig extends ColumnTypeMappings {
  self: HasDatabaseConfig[JdbcProfile] =>
  import profile.api._

  class TasksTable(tag: Tag) extends Table[Task](tag, "tasks") {
    def id = column[TaskId]("id", O.PrimaryKey)
    def name = column[String]("name")
    def isFinished = column[Boolean]("is_finished")
    def userId = column[UserId]("user_id")
    def deadline = column[Option[LocalDateTime]]("deadline")
    def createdAt = column[LocalDateTime]("created_at")
    def updatedAt = column[LocalDateTime]("updated_at")

    def * =
      (id, name, isFinished, userId, deadline, createdAt, updatedAt) <> (Task.tupled, Task.unapply)
  }
}
