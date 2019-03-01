import javax.inject._
import scala.concurrent._
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.http._
import play.api.routing.Router
@Singleton
// class ErrorHandler extends HttpErrorHandler {
//
//   def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
//     Future.successful(
//       Status(statusCode)("A client error occurred: " + message)
//     )
//   }
//
//   def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
//     Future.successful(
//       InternalServerError("A server error occurred: " + exception.getMessage)
//     )
//   }
// }
class ErrorHandler @Inject()(
    env: Environment,
    config: Configuration,
    sourceMapper: OptionalSourceMapper,
    router: Provider[Router]
) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  override def onProdServerError(request: RequestHeader, exception: UsefulException) = {
    Future.successful(
      InternalServerError("サーバーエラーが発生しました。しばらくの後、再度お試しください。申し訳ありません。(＞人＜;)")
    )
  }
}
