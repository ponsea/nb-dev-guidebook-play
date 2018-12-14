package controllers

import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import models.{Task, TaskId, UserId}
import services.{TaskService, TaskError, TaskNotFound, TaskPermissionDenied}
import json._

@Singleton
class TaskController @Inject()(taskService: TaskService,
                               authAction: AuthAction,
                               cc: ControllerComponents)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with UserWrites
    with TaskWrites
    with ServiceErrorWrites {

  def index() = Action.async {
    taskService.getAll().map { tasks =>
      Ok(Json.toJson(tasks))
    }
  }

  def show(id: String) = Action.async {
    taskService.get(TaskId(id)).map { maybeTask: Option[Task] =>
      maybeTask.fold[Result](NotFound) { task: Task =>
        Ok(Json.toJson(task))
      }
    }
  }

  def create() = authAction.async(parse.json) { request: AuthRequest[JsValue] =>
    val inputResult: JsResult[TaskCreatingInput] = request.body.validate[TaskCreatingInput]
    inputResult match {
      case JsSuccess(input, _) => {
        taskService.create(input.name, input.deadline, request.userId).map { task =>
          Created(Json.toJson(task))
        }
      }
      case JsError(errors) => {
        Future.successful(BadRequest(JsError.toJson(errors)))
      }
    }
  }

  def update(id: String) = authAction.async(parse.json) { request =>
    val inputResult = request.body.validate[TaskUpdatingInput]
    inputResult match {
      case JsSuccess(input, _) => {
        taskService
          .update(TaskId(id), input.name, input.isFinished, input.deadline, request.userId)
          .map { result =>
            result match {
              case Right(task) => Ok(Json.toJson(task))
              case Left(error) => appropriateErrorStatusOf(error)
            }
          }
      }
      case JsError(errors) => {
        Future.successful(BadRequest(JsError.toJson(errors)))
      }
    }
  }

  def delete(id: String) = authAction.async { request =>
    taskService.delete(TaskId(id), request.userId).map { result =>
      result match {
        case Right(_)    => NoContent
        case Left(error) => appropriateErrorStatusOf(error)
      }
    }
  }

  private def appropriateErrorStatusOf(error: TaskError) = {
    error match {
      case _: TaskNotFound              => NotFound(Json.toJson(error))
      case _: TaskPermissionDenied.type => Forbidden(Json.toJson(error))
    }
  }
}
