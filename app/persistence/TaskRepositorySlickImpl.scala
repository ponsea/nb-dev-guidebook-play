package persistence

import javax.inject.{Inject, Singleton}
import java.time.LocalDateTime
import scala.concurrent.{Future, ExecutionContext}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import models.{Task, TaskId, UserId}
import db.TasksTableConfig

import slick.jdbc.JdbcProfile
import slick.jdbc.JdbcBackend
import slick.sql.SqlAction
import slick.sql.FixedSqlAction
import slick.sql.FixedSqlStreamingAction

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

  def save(task: Task) = db.run {
    tasks.insertOrUpdate(task).map(_ => task)
  }

  def delete(taskId: TaskId) = db.run {
    tasks.filter(_.id === taskId).delete.map(_ => ())
  }

  // 型注釈つけて分かりやすくした場合 ----------------------

  def findAllVerbose() = {
    val action: FixedSqlStreamingAction[Seq[Task], Task, Effect.Read] = tasks.result
    val database: JdbcBackend#DatabaseDef = db
    val result: Future[Seq[Task]] = database.run(action)
    result
  }

  def findByIdVerbose(taskId: TaskId) = {
    val query: Query[TasksTable, Task, Seq] = tasks.filter(_.id === taskId)
    val action: FixedSqlStreamingAction[Seq[Task], Task, Effect.Read] = query.result
    val headOptionAction: SqlAction[Option[Task], NoStream, Effect.Read] = action.headOption
    val result: Future[Option[Task]] = db.run(headOptionAction)
    result
  }

  def saveVerbose(task: Task) = {
    val action: FixedSqlAction[Int, NoStream, Effect.Write] = tasks.insertOrUpdate(task)
    val result: Future[Int] = db.run(action)
    result.map(_ => task)
  }

  def deleteVerbose(taskId: TaskId) = {
    val action: FixedSqlAction[Int, NoStream, Effect.Write] = tasks.filter(_.id === taskId).delete
    val result: Future[Int] = db.run(action)
    result.map(_ => ())
  }
}
