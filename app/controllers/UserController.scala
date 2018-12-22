package controllers

import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import models.{User, UserId}
import services.UserService
import json._

@Singleton
class UserController @Inject()(userService: UserService,
                               authAction: AuthAction,
                               cc: ControllerComponents)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with UserWrites {

  def index() = Action.async {
    userService.getAll().map { users =>
      Ok(Json.toJson(users))
    }
  }

  def show(id: String) = Action.async {
    userService.get(UserId(id)).map {
      _.fold[Result](NotFound)(user => Ok(Json.toJson(user)))
    }
  }
}
