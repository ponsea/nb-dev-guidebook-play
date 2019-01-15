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
    def doUpdate(task: Task) = {
      if (task.canEditBy(updaterId)) {
        val updatedTask = task.updated(name.getOrElse(task.name),
                                       isFinished.getOrElse(task.isFinished),
                                       deadline.getOrElse(task.deadline))
        taskRepository.save(updatedTask).map(Right(_))
      } else {
        Future.successful(Left(TaskPermissionDenied))
      }
    }

    get(taskId).flatMap {
      _.fold(
        notFound => Future.successful(Left(notFound)),
        doUpdate
      )
    }
  }

  def delete(taskId: TaskId, deleterId: UserId): Future[Either[TaskError, Unit]] = {
    def doDelete(task: Task) = {
      if (task.canEditBy(deleterId))
        taskRepository.delete(taskId).map(Right(_))
      else
        Future.successful(Left(TaskPermissionDenied))
    }

    get(taskId).flatMap {
      _.fold(
        notFound => Future.successful(Left(notFound)),
        doDelete
      )
    }
  }
}
