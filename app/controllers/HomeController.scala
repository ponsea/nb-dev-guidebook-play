package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def param(id: String) = Action {
    Ok(id)
  }

  def typedParam(id: Long) = Action {
    val result = id * 2
    Ok(result.toString)
  }

  def queryParam(id: Option[String]) = Action {
    Ok(id.toString)
  }

  def queryParamDefault(id: String) = Action {
    Ok(id)
  }

  def reverseRouter() = Action {
    Redirect(routes.HomeController.param("abc"))
  }

  def setSession() = Action { implicit request =>
    val id = "[user id]"
    Ok(s"session set. (id -> $id)").addingToSession("id" -> id)
  }

  def showSession() = Action { request =>
    Ok(request.session.get("id").toString)
  }

  def discardSession() = Action { implicit request =>
    Ok("session discarded.").removingFromSession("id")
  }
}
