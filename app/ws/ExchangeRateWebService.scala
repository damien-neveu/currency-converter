package ws

import models.{CurrencyConversion, ExchangeRate}

import scala.concurrent.Future

trait ExchangeRateWebService {

  def urlTemplate: String

  def buildUrl(conv: CurrencyConversion): String =
    urlTemplate.format(conv.from.getCurrencyCode, conv.to.getCurrencyCode)

  def getExchangeRate(conv: CurrencyConversion): Future[Either[String, ExchangeRate]]

}
