package services

import models.TaskId

abstract class ServiceError(val message: String, val args: Any*)

case class TaskNotFound(taskId: TaskId) extends ServiceError("error.taskNotFound", taskId)
case object UnauthorizedAction extends ServiceError("error.unauthorizedAction")
