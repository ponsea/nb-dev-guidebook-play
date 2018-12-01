package services

import javax.inject.{Inject, Singleton}
import java.time.LocalDateTime
import scala.concurrent.Future
import models.{Task, TaskId, UserId}
import repositories.TaskRepository
import utils.{IdGenerator, SystemDateTime}

@Singleton()
class TaskService @Inject()(taskRepository: TaskRepository)(implicit idGen: IdGenerator,
                                                            sdt: SystemDateTime) {
  def getAll(): Future[Seq[Task]] = taskRepository.findAll()

  def get(taskId: TaskId): Future[Option[Task]] = taskRepository.findById(taskId)

  def create(name: String, deadline: Option[LocalDateTime], userId: UserId): Future[Task] = {
    val newTask = Task(TaskId.newId(), name, false, userId, deadline, sdt.now(), sdt.now())
    taskRepository.save(newTask)
  }
}
