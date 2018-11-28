package controllers

import javax.inject._
import scala.concurrent.ExecutionContext
import play.api._
import play.api.mvc._
import play.api.libs.json._
import models.Task
import services.TaskService

@Singleton
class TaskController @Inject()(taskService: TaskService, cc: ControllerComponents)(
    implicit ec: ExecutionContext)
    extends AbstractController(cc) {
  import json.TaskWrites._
  def show(id: String) = Action.async {
    taskService.get(id).map { maybeTask: Option[Task] =>
      maybeTask.fold[Result](NotFound) { task: Task =>
        Ok(Json.toJson(task))
      }
    }
  }
}
