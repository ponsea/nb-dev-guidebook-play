package controllers

import javax.inject._
import scala.util.{Success, Failure}
import scala.concurrent.{Future, ExecutionContext}
import play.api._
import play.api.mvc._
import play.api.libs.json._
import models._

@Singleton
class ActionCompositionController @Inject()(logging: LoggingActionBuilder,
                                            maybeAuthAction: MaybeAuthActionBuilder,
                                            mustAuthFilter: MustAuthActionFilter,
                                            cc: ControllerComponents)
    extends AbstractController(cc) {
  val logger = Logger(this.getClass())
  def log() = logging { implicit request: Request[AnyContent] =>
    logger.info("[during processing] processing...")
    Ok("logged")
  }

  def maybeAuth() = (maybeAuthAction andThen mustAuthFilter) {
    request: MaybeAuthRequest[AnyContent] =>
      val userId: UserId = request.userId.get // mustAuthFilterがあるのでgetできる。型的で保証されてる方がいいんだけど。
      Ok("OK")
  }
}

class LoggingActionBuilder @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext)
    extends ActionBuilderImpl(parser) {
  val logger = Logger(this.getClass())

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    logger.info(s"[before processing] host: ${request.host}")
    block(request)
      .andThen {
        case Success(result) => logger.info(s"[after processing] status: ${result.header.status}")
        case Failure(e)      => logger.info(s"[after processing] failure!")
      }
  }
}

class MaybeAuthRequest[A](val userId: Option[UserId], request: Request[A])
    extends WrappedRequest[A](request)

class MaybeAuthActionBuilder @Inject()(val parser: BodyParsers.Default)(
    implicit val executionContext: ExecutionContext)
    extends ActionBuilder[MaybeAuthRequest, AnyContent]
    with ActionTransformer[Request, MaybeAuthRequest] {

  def transform[A](request: Request[A]) = Future.successful {
    val maybeUserId = request.session.get("userId").map(UserId(_))
    new MaybeAuthRequest(maybeUserId, request)
  }
}

class MustAuthActionFilter @Inject()(val parser: BodyParsers.Default)(
    implicit val executionContext: ExecutionContext)
    extends ActionFilter[MaybeAuthRequest]
    with Results {

  def filter[A](request: MaybeAuthRequest[A]) = Future.successful {
    if (request.userId.isDefined) None else Some(Unauthorized)
  }
}
