package repositories

import models.{Task, TaskId}

trait TaskRepository {
  def find(taskId: TaskId): Option[Task]
}
