package services

import javax.inject.{Inject, Singleton}
import java.time.LocalDateTime
import scala.concurrent.{Future, ExecutionContext}
import models.{Task, TaskId, UserId}
import repositories.TaskRepository
import utils.{IdGenerator, SystemDateTime}

@Singleton()
class TaskService @Inject()(taskRepository: TaskRepository)(implicit idGen: IdGenerator,
                                                            sdt: SystemDateTime,
                                                            ec: ExecutionContext) {
  def getAll(): Future[Seq[Task]] = taskRepository.findAll()

  def get(taskId: TaskId): Future[Option[Task]] = taskRepository.findById(taskId)

  def create(name: String, deadline: Option[LocalDateTime], userId: UserId): Future[Task] = {
    val newTask = Task(TaskId.newId(), name, false, userId, deadline, sdt.now(), sdt.now())
    taskRepository.save(newTask)
  }

  def update(taskId: TaskId,
             name: Option[String],
             isFinished: Option[Boolean],
             deadline: Option[Option[LocalDateTime]],
             updaterId: UserId): Future[Either[ServiceError, Task]] = {
    // HACK: もっと分かりやすい書き方ないかな...
    def f[T](v: T) = Future.successful(v)
    taskRepository.findById(taskId).flatMap { maybeTask =>
      maybeTask.fold(f[Either[ServiceError, Task]](Left(TaskNotFound(taskId)))) { task =>
        if (task.canUpdate(updaterId)) {
          val newTask = task.copy(name = name.getOrElse(task.name),
                                  isFinished = isFinished.getOrElse(task.isFinished),
                                  deadline = deadline.getOrElse(task.deadline),
                                  updatedAt = sdt.now())
          taskRepository.save(newTask).map(Right(_))
        } else {
          f(Left(UnauthorizedAction))
        }
      }
    }
  }
}
