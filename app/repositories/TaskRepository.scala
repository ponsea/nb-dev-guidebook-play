package repositories

import models.{Task, TaskId}
import scala.concurrent.Future

trait TaskRepository {
  def findById(taskId: TaskId): Future[Option[Task]]
}
