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

  // HACK: もっと分かりやすい書き方ないかな...
  def update(taskId: TaskId,
             name: Option[String],
             isFinished: Option[Boolean],
             deadline: Option[Option[LocalDateTime]],
             updaterId: UserId): Future[Either[ServiceError, Task]] = {
    val notFound: Future[Either[ServiceError, Task]] =
      Future.successful(Left(TaskNotFound(taskId)))
    val unauthorized: Future[Either[ServiceError, Task]] =
      Future.successful(Left(UnauthorizedAction))

    taskRepository.findById(taskId).flatMap { maybeTask =>
      maybeTask.fold(notFound) { task =>
        if (task.canEditBy(updaterId)) {
          val newTask = task.copy(name = name.getOrElse(task.name),
                                  isFinished = isFinished.getOrElse(task.isFinished),
                                  deadline = deadline.getOrElse(task.deadline),
                                  updatedAt = sdt.now())
          taskRepository.save(newTask).map(Right(_))
        } else {
          unauthorized
        }
      }
    }
  }
}
