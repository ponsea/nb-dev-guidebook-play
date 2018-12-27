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
    taskService.get(TaskId(id)).map {
      _.fold(
        error => appropriateErrorResponseOf(error),
        task => Ok(Json.toJson(task))
      )
    }
  }

  def create() = authAction.async(parse.json) { implicit request =>
    validatedJson[TaskCreatingInput] { input =>
      taskService.create(input.name, input.deadline, request.userId).map { task =>
        Created(Json.toJson(task))
      }
    }
  }

  def update(id: String) = authAction.async(parse.json) { implicit request =>
    validatedJson[TaskUpdatingInput] { input =>
      taskService
        .update(TaskId(id), input.name, input.isFinished, input.deadline, request.userId)
        .map {
          _.fold(
            error => appropriateErrorResponseOf(error),
            task => Ok(Json.toJson(task))
          )
        }
    }
  }

  def delete(id: String) = authAction.async { request =>
    taskService.delete(TaskId(id), request.userId).map {
      _.fold(
        error => appropriateErrorResponseOf(error),
        _ => NoContent
      )
    }
  }

  private def appropriateErrorResponseOf(error: TaskError) = {
    error match {
      case _: TaskNotFound              => NotFound(Json.toJson(error))
      case _: TaskPermissionDenied.type => Forbidden(Json.toJson(error))
    }
  }
}
