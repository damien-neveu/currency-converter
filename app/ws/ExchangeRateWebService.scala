package ws

import models.{CurrencyConversion, ExchangeRate}

import scala.concurrent.Future

trait ExchangeRateWebService {

  def getExchangeRate(conv: CurrencyConversion): Future[Either[String, ExchangeRate]]

}
