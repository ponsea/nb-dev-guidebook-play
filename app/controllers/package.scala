import scala.concurrent.Future
import play.api.mvc._
import play.api.libs.json._

package object controllers {
  // TはJsonのインプットとなる型。(Reads[T]が必要)
  // バリデーションが成功した場合はインプットを処理するブロックを呼び出す
  // バリデーションが失敗した場合はBadRequest
  def validatedJson[T](block: T => Future[Result])(implicit request: Request[JsValue],
                                                   reads: Reads[T]): Future[Result] = {
    request.body
      .validate[T]
      .fold(
        error => Future.successful(Results.BadRequest(JsError.toJson(error))),
        input => block(input)
      )
  }
}
