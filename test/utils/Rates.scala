package utils

import java.time.ZonedDateTime

import models.{CurrencyConversion, ExchangeRate, Google}
import utils.Currencies._

object Rates {

  val rateEuroToUSDollar = ExchangeRate(
    from = EUR,
    to = USD,
    rate = 1.14,
    source = Google,
    lastUpdatedAt = ZonedDateTime.now
  )

  val conversionEuroToUSDollar = CurrencyConversion(
    from = EUR,
    to = USD
  )

  val rateEuroToSingaporeDollar = ExchangeRate(
    from = EUR,
    to = SGD,
    rate = 1.54,
    source = Google,
    lastUpdatedAt = ZonedDateTime.now
  )

  val conversionSingaporeDollarToEuro = CurrencyConversion(
    from = SGD,
    to = EUR
  )

  val conversionBritishPoundToCanadianDollar = CurrencyConversion(
    from = GBP,
    to = CAD
  )

}
