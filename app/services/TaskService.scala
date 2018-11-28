package services

import javax.inject.{Inject, Singleton}
import java.time.LocalDateTime
import models.{Task, TaskId, UserId}
import repositories.TaskRepository
import utils.{IdGenerator, SystemDateTime}

@Singleton()
class TaskService @Inject()(taskRepository: TaskRepository) {
  def getAll() = taskRepository.findAll()

  def get(id: String) = taskRepository.findById(TaskId(id))

  def create(name: String, deadline: Option[LocalDateTime], userId: UserId)(
      implicit idGen: IdGenerator,
      sdt: SystemDateTime) = {
    val newTask = Task(TaskId.newId(), name, false, userId, deadline, sdt.now(), sdt.now())
    taskRepository.save(newTask)
  }
}
