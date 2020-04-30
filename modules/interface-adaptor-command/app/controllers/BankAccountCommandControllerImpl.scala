package controllers

import example.BankAccountCommandController
import javax.inject.Inject
import play.api.mvc.{ Action, AnyContent, ControllerComponents, Request }

class BankAccountCommandControllerImpl @Inject() (val controllerComponents: ControllerComponents)
    extends BankAccountCommandController {
  override def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] => Ok(views.html.index()) }
}
