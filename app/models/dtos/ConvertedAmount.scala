package models.dtos

import java.util.Currency
import models.{Amount, ExchangeRateSource}

case class ConvertedAmount(
  amount: Amount,
  currency: Currency,
  source: ExchangeRateSource
)
