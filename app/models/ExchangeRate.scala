package models

import java.time.ZonedDateTime
import java.util.Currency

case class ExchangeRate(
  from: Currency,
  to: Currency,
  rate: Double,
  source: ExchangeRateSource,
  lastUpdatedAt: ZonedDateTime
)
