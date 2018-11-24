package repositories

import models.{Task, TaskId}

trait TaskRepository {
  def findById(taskId: TaskId): Option[Task]
}
