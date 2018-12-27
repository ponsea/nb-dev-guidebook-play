package controllers

import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import models.{User, UserId}
import services._
import json._

@Singleton
class UserController @Inject()(userService: UserService,
                               authAction: AuthAction,
                               cc: ControllerComponents)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with UserWrites
    with ServiceErrorWrites {

  def index() = Action.async {
    userService.getAll().map { users =>
      Ok(Json.toJson(users))
    }
  }

  def show(id: String) = Action.async {
    userService.get(UserId(id)).map {
      _.fold(
        error => appropriateErrorResponseOf(error),
        user => Ok(Json.toJson(user))
      )
    }
  }

  def showMyself() = authAction.async { request =>
    userService.get(request.userId).map {
      _.fold(
        error => appropriateErrorResponseOf(error),
        user => Ok(Json.toJson(user)(privateUserWrites))
      )
    }
  }

  def create() = Action.async(parse.json) { implicit request =>
    validatedJson[UserCreatingInput] { input =>
      userService.create(input.name, input.email, input.password).map {
        _.fold(
          error => BadRequest(Json.toJson(error)),
          user => Created(Json.toJson(user))
        )
      }
    }
  }

  def delete(id: String) = authAction.async { implicit request =>
    userService.delete(UserId(id), request.userId).map {
      _.fold(
        error => appropriateErrorResponseOf(error),
        _ => NoContent
      )
    }
  }

  private def appropriateErrorResponseOf(error: UserError) = {
    error match {
      case _: UserNotFound              => NotFound(Json.toJson(error))
      case _: UserPermissionDenied.type => Forbidden(Json.toJson(error))
      case _: UserEmailDuplicated       => BadRequest(Json.toJson(error))
    }
  }
}
