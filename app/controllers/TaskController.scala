package controllers

import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import models.{Task, UserId}
import services.TaskService
import json.TaskCreatingInput

@Singleton
class TaskController @Inject()(taskService: TaskService, cc: ControllerComponents)(
    implicit ec: ExecutionContext)
    extends AbstractController(cc) {
  import json.TaskWrites._

  def index() = Action.async {
    taskService.getAll().map { tasks =>
      Ok(Json.toJson(tasks))
    }
  }

  def show(id: String) = Action.async {
    taskService.get(id).map { maybeTask: Option[Task] =>
      maybeTask.fold[Result](NotFound) { task: Task =>
        Ok(Json.toJson(task))
      }
    }
  }

  def create() = Action.async(parse.json) { request: Request[JsValue] =>
    val inputResult: JsResult[TaskCreatingInput] = request.body.validate[TaskCreatingInput]
    inputResult match {
      case JsSuccess(input, _) => {
        // TODO: UserIdをセッション情報から取得する
        taskService.create(input.name, input.deadline, UserId("1")).map { task =>
          Ok(Json.toJson(task))
        }
      }
      case JsError(errors) => {
        Future.successful(BadRequest(JsError.toJson(errors)))
      }
    }
  }
}
