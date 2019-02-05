package logic.impl

import logic.CurrencyConversionService
import models.dtos.ConvertedAmount
import models.{Amount, CurrencyConversion}

import scala.concurrent.Future

class CurrencyConversionServiceImpl extends CurrencyConversionService {

  override def convert(amount: Amount, currencyConversion: CurrencyConversion): Future[Either[String, ConvertedAmount]] = ???

}
