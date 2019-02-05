package controllers

import java.util.Currency

import javax.inject.{Inject, Singleton}
import models.Amount
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class CurrencyController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def convert(from: Currency, to: Currency, amount: Amount) = ???

}
