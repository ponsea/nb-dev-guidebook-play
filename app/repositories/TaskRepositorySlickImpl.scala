package repositories

import javax.inject.{Inject, Singleton}
import java.time.LocalDateTime
import scala.concurrent.{Future, ExecutionContext}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import models.{Task, TaskId, UserId}
import db.TasksTableConfig
import slick.jdbc.JdbcProfile

@Singleton()
class TaskRepositorySlickImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(
    implicit ec: ExecutionContext)
    extends TaskRepository
    with HasDatabaseConfigProvider[JdbcProfile]
    with TasksTableConfig {
  import profile.api._

  private val tasks = TableQuery[TasksTable]

  def findAll() = db.run {
    tasks.result
  }

  def findById(taskId: TaskId) = db.run {
    tasks.filter(_.id === taskId).result.headOption
  }

  def save(task: Task) = {
    val action = tasks.insertOrUpdate(task)
    db.run(action).map(_ => task)
  }

  def delete(taskId: TaskId) = {
    val action = tasks.filter(_.id === taskId).delete
    db.run(action).map(_ => ())
  }
}
