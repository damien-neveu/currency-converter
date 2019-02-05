package logic

import models.dtos.ConvertedAmount
import models.{Amount, CurrencyConversion}

import scala.concurrent.Future

trait CurrencyConversionService {

  def convert(amount: Amount, currencyConversion: CurrencyConversion): Future[Either[String, ConvertedAmount]]

}
