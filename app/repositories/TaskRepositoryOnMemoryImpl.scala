package repositories

import javax.inject.Singleton
import java.time.LocalDateTime
import scala.concurrent.Future
import models.{Task, TaskId, UserId}

@Singleton()
class TaskRepositoryOnMemoryImpl extends TaskRepository {
  private val dateTime = LocalDateTime.parse("2019-04-01T00:00:00")
  private val tasks = Set(
    Task(TaskId("1"), "Scalaの勉強", true, UserId("1"), Some(dateTime), dateTime, dateTime),
    Task(TaskId("2"), "Playの勉強", false, UserId("1"), Some(dateTime), dateTime, dateTime),
    Task(TaskId("3"), "英語の勉強", false, UserId("1"), None, dateTime, dateTime),
    Task(TaskId("4"), "たまご買う", true, UserId("2"), Some(dateTime), dateTime, dateTime),
    Task(TaskId("5"), "ねぎ買う", false, UserId("2"), Some(dateTime), dateTime, dateTime)
  )

  def findById(taskId: TaskId) = {
    Future.successful(tasks.find(_.id == taskId))
  }
}
