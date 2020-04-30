package example

import play.api.mvc.{ Action, AnyContent, BaseController }

trait BankAccountQueryController extends BaseController {
  def index(): Action[AnyContent]
}
