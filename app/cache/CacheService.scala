package cache

import models.{CurrencyConversion, ExchangeRate}

import scala.concurrent.Future

trait CacheService {

  def saveExchangeRate(exchangeRate: ExchangeRate): Future[Unit]

  def getExchangeRate(currencyConversion: CurrencyConversion): Future[Option[ExchangeRate]]

}
