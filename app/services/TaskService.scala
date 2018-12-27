package services

import javax.inject.{Inject, Singleton}
import java.time.LocalDateTime
import scala.concurrent.{Future, ExecutionContext}
import models.{Task, TaskId, UserId}
import persistence.TaskRepository
import utils.{IdGenerator, SystemDateTime}

@Singleton()
class TaskService @Inject()(taskRepository: TaskRepository)(implicit idGen: IdGenerator,
                                                            sdt: SystemDateTime,
                                                            ec: ExecutionContext) {
  def getAll(): Future[Seq[Task]] = taskRepository.findAll()

  def get(taskId: TaskId): Future[Either[TaskError, Task]] = {
    taskRepository.findById(taskId).map(_.toRight(TaskNotFound(taskId)))
  }

  def create(name: String, deadline: Option[LocalDateTime], userId: UserId): Future[Task] = {
    val newTask = Task(TaskId.newId(), name, false, userId, deadline, sdt.now(), sdt.now())
    taskRepository.save(newTask)
  }

  // HACK: もっと分かりやすい書き方ないかな...
  def update(taskId: TaskId,
             name: Option[String],
             isFinished: Option[Boolean],
             deadline: Option[Option[LocalDateTime]],
             updaterId: UserId): Future[Either[TaskError, Task]] = {
    val notFound: Future[Either[TaskError, Task]] =
      Future.successful(Left(TaskNotFound(taskId)))
    val permissionDenied: Future[Either[TaskError, Task]] =
      Future.successful(Left(TaskPermissionDenied))

    taskRepository.findById(taskId).flatMap { maybeTask =>
      maybeTask.fold(notFound) { task =>
        if (task.canEditBy(updaterId)) {
          val newTask = task.copy(name = name.getOrElse(task.name),
                                  isFinished = isFinished.getOrElse(task.isFinished),
                                  deadline = deadline.getOrElse(task.deadline),
                                  updatedAt = sdt.now())
          taskRepository.save(newTask).map(Right(_))
        } else {
          permissionDenied
        }
      }
    }
  }

  def delete(taskId: TaskId, deleterId: UserId): Future[Either[TaskError, Unit]] = {
    val notFound: Future[Either[TaskError, Unit]] =
      Future.successful(Left(TaskNotFound(taskId)))
    val permissionDenied: Future[Either[TaskError, Unit]] =
      Future.successful(Left(TaskPermissionDenied))

    taskRepository.findById(taskId).flatMap { maybeTask =>
      maybeTask.fold(notFound) { task =>
        if (task.canEditBy(deleterId)) {
          taskRepository.delete(taskId).map(Right(_))
        } else {
          permissionDenied
        }
      }
    }
  }
}
