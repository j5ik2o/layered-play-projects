package controllers

import example.BankAccountQueryController
import javax.inject.Inject
import play.api.mvc.{ Action, AnyContent, ControllerComponents, Request }

class BankAccountQueryControllerImpl @Inject() (val controllerComponents: ControllerComponents)
    extends BankAccountQueryController {
  override def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] => Ok(views.html.index()) }
}
