package controllers

import javax.inject._
import scala.concurrent.{Future, ExecutionContext}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import services.AuthService
import json.TokenCreatingInput

@Singleton
class AuthController @Inject()(authService: AuthService, cc: ControllerComponents)(
    implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def createToken = Action.async(parse.json) { implicit request =>
    val inputResult = request.body.validate[TokenCreatingInput]
    inputResult.fold(
      error => {
        Future.successful(BadRequest(JsError.toJson(error)))
      },
      input => {
        authService.authenticate(input.email, input.password).map {
          _.fold[Result](Unauthorized(Json.obj("message" -> "email or password was incorrect"))) {
            token =>
              Ok(Json.obj("message" -> "added token to Cookie")).withSession(token)
          }
        }
      }
    )
  }

  def deleteToken = Action { implicit request =>
    NoContent.withNewSession
  }
}
