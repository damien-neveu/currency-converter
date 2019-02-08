package models

sealed trait ExchangeRateSource

case object FreeCurrencyConverter extends ExchangeRateSource
case object EuropeanCentralBank extends ExchangeRateSource
case object Google extends ExchangeRateSource
case object CommonSense extends ExchangeRateSource

object ExchangeRateSource {

  final val `free-currency-converter-api` = "free-currency-converter-api"
  final val `european-central-bank` = "european-central-bank"
  final val `google` = "google"

}
