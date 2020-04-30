package example

import play.api.mvc._

trait BankAccountCommandController extends BaseController {
  def index(): Action[AnyContent]
}
