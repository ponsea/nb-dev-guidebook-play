package services

import models.TaskId

abstract class ServiceError(val message: String, val args: Any*)

abstract sealed class TaskError(message: String, args: Any*) extends ServiceError(message, args: _*)
case class TaskNotFound(taskId: TaskId) extends TaskError("error.task.notFound", taskId)
case object TaskPermissionDenied extends TaskError("error.task.permissionDenied")

abstract sealed class UserError(message: String, args: Any*) extends ServiceError(message, args: _*)
case class UserEmailDuplicated(email: String) extends UserError("error.user.emailDuplicated", email)
