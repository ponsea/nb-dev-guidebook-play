package views

import views.html.helper.FieldConstructor

object MyHelpers {
  implicit val myField = FieldConstructor(views.html.myFieldConstructor.f)
}
