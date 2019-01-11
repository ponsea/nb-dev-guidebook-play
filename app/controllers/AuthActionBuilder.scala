package controllers

import javax.inject.Inject
import scala.concurrent.{Future, ExecutionContext}
import play.api.mvc._
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import models.UserId
import services.AuthService

trait AuthRequestHeader extends MessagesRequestHeader with PreferredMessagesProvider {
  def userId: UserId
}

class AuthRequest[A](val userId: UserId, val messagesApi: MessagesApi, request: Request[A])
    extends WrappedRequest[A](request)
    with AuthRequestHeader

class AuthActionBuilder @Inject()(
    val parser: BodyParsers.Default,
    messagesApi: MessagesApi,
    authService: AuthService)(implicit val executionContext: ExecutionContext)
    extends ActionBuilder[AuthRequest, AnyContent]
    with ActionRefiner[Request, AuthRequest]
    with Results {

  def refine[A](request: Request[A]) = Future.successful {
    request.session
      .get(authService.SESSION_KEY)
      .map(uid => new AuthRequest(UserId(uid), messagesApi, request))
      .toRight(Unauthorized(Json.obj("message" -> "authentication required")))
  }
}
