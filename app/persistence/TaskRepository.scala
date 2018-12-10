package persistence

import models.{Task, TaskId}
import scala.concurrent.Future

trait TaskRepository {
  def findAll(): Future[Seq[Task]]
  def findById(taskId: TaskId): Future[Option[Task]]
  def save(task: Task): Future[Task]
  def delete(taskId: TaskId): Future[Unit]
}
