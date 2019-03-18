package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

case class Person(id: Long, name: String, age: Int)
case class PersonCreatingInput(name: String, age: Int)

@Singleton
class PersonController @Inject()(mcc: MessagesControllerComponents)
    extends MessagesAbstractController(mcc) {
  var people = Map(
    1L -> Person(1L, "相生 葵", 20),
    2L -> Person(2L, "阿江 愛", 21),
    3L -> Person(3L, "相川 愛華", 22)
  )
  val personCreatingForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "age" -> number(min = 0)
    )(PersonCreatingInput.apply)(PersonCreatingInput.unapply)
  )

  def index() = Action {
    Ok(views.html.person.index(people.values.toSeq))
  }

  def show(id: Long) = Action { implicit request =>
    Ok(views.html.person.show(people(id)))
  }

  def createForm() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.person.create(personCreatingForm))
  }

  def create() = Action { implicit request =>
    personCreatingForm.bindFromRequest.fold(
      (formWithErrors: Form[PersonCreatingInput]) => {
        BadRequest(views.html.person.create(formWithErrors))
      },
      (input: PersonCreatingInput) => {
        val newId = people.keys.max + 1L
        val newPerson = Person(newId, input.name, input.age)
        people += (newId -> newPerson)
        Redirect(routes.PersonController.show(newId)).flashing("notice" -> "Personを作成しました。")
      }
    )
  }

  def updateForm(id: Long) = Action { implicit request =>
    val filledForm = personCreatingForm.fill(PersonCreatingInput("池", 22))
    Ok(views.html.person.update(filledForm))
  }

  def update(id: Long) = TODO
  def delete(id: Long) = TODO
}
