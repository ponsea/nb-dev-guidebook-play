package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class I18nController @Inject()(mcc: MessagesControllerComponents)
    extends MessagesAbstractController(mcc) {
  def greet() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.greet())
  }
}
