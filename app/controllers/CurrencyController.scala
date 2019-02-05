package controllers

import javax.inject.{Inject, Singleton}

import scala.concurrent.Future
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import models.{Amount, CurrencyConversion}

@Singleton
class CurrencyController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def convert(currencyConversion: CurrencyConversion, amount: Amount): Action[AnyContent] = Action.async {
    Future.successful(NotImplemented)
  }

}
