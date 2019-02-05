package models

sealed trait ExchangeRateSource
case object Google extends ExchangeRateSource
case object DevisesZone extends ExchangeRateSource
case object FreeCurrencyConverter extends ExchangeRateSource
