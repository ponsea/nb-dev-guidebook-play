package controllers

import javax.inject.Inject
import scala.concurrent.{Future, ExecutionContext}
import play.api.mvc._
import play.api.libs.json.Json
import models.UserId
import services.AuthService

class AuthRequest[A](val userId: UserId, request: Request[A]) extends WrappedRequest[A](request)

class AuthAction @Inject()(val parser: BodyParsers.Default, authService: AuthService)(
    implicit val executionContext: ExecutionContext)
    extends ActionBuilder[AuthRequest, AnyContent]
    with ActionRefiner[Request, AuthRequest]
    with Results {

  def refine[A](request: Request[A]) = Future.successful {
    request.session
      .get(authService.SESSION_KEY)
      .map(uid => new AuthRequest(UserId(uid), request))
      .toRight(Unauthorized(Json.obj("message" -> "authentication required")))
  }
}
