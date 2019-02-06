package cache

import java.util.Currency

import models.{CurrencyConversion, ExchangeRate}

import scala.concurrent.Future

trait CacheService {

  def saveExchangeRate(exchangeRate: ExchangeRate): Future[Unit]

  def getExchangeRate(currencyConversion: CurrencyConversion): Future[Option[ExchangeRate]]

  def key(exchangeRate: ExchangeRate): String = key(exchangeRate.from, exchangeRate.to)

  def invertedKey(exchangeRate: ExchangeRate): String = invertedKey(exchangeRate.from, exchangeRate.to)

  def key(cc: CurrencyConversion): String = key(cc.from, cc.to)

  def invertedKey(cc: CurrencyConversion): String = invertedKey(cc.from, cc.to)

  private def key(c1: Currency, c2: Currency): String = s"${c1.getCurrencyCode}_${c2.getCurrencyCode}"

  private def invertedKey(c1: Currency, c2: Currency): String = s"${c2.getCurrencyCode}_${c1.getCurrencyCode}"

}
