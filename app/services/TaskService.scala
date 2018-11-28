package services

import javax.inject.{Inject, Singleton}
import models.TaskId
import repositories.TaskRepository

@Singleton()
class TaskService @Inject()(taskRepository: TaskRepository) {
  def getAll() = taskRepository.findAll()

  def get(id: String) = taskRepository.findById(TaskId(id))
}

