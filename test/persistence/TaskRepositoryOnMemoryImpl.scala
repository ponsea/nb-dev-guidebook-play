package persistence

import javax.inject.Singleton
import scala.concurrent.Future
import models.{Task, TaskId}

@Singleton()
class TaskRepositoryOnMemoryImpl(initialData: Set[Task] = Set.empty) extends TaskRepository {
  private var tasks = initialData

  def findAll() = Future.successful(tasks.toSeq)

  def findById(taskId: TaskId) = {
    Future.successful(tasks.find(_.id == taskId))
  }

  def save(newTask: Task) = {
    tasks.find(_.id == newTask.id).foreach(old => tasks -= old)
    tasks += newTask
    Future.successful(newTask)
  }

  def delete(taskId: TaskId) = {
    tasks.find(_.id == taskId).foreach(tasks -= _)
    Future.successful(())
  }
}
