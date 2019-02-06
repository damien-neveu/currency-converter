package logic

import models.dtos.ConvertedAmount
import models.{Amount, CurrencyConversion, ExchangeRate}

import scala.concurrent.Future

trait CurrencyConversionService {

  def convert(amount: Amount, currencyConversion: CurrencyConversion): Future[Either[String, ConvertedAmount]]

  protected def convertAmount(
    amount: Amount, conv: CurrencyConversion, eitherRate: Either[String, ExchangeRate]): Either[String, ConvertedAmount] =
    eitherRate match {
      case Left(err) =>
        Left(err)
      case Right(rate) =>
        if (areCompatible(conv, rate)) {
          Right(buildCompatibleConvertedAmount(amount, conv, rate))
        }
        else {
          Left(getIncompatibleConversionAndRateMessage(conv, rate))
        }
    }

  private def areCompatible(conv: CurrencyConversion, rate: ExchangeRate): Boolean =
    haveSameFromAndTo(conv, rate) || haveOppositeFromAndTo(conv, rate)

  private def haveSameFromAndTo(conv: CurrencyConversion, rate: ExchangeRate): Boolean =
    conv.from == rate.from && conv.to == rate.to

  private def haveOppositeFromAndTo(conv: CurrencyConversion, rate: ExchangeRate): Boolean =
    conv.from == rate.to && conv.to == rate.from

  private def buildCompatibleConvertedAmount(amount: Amount, conv: CurrencyConversion, rate: ExchangeRate): ConvertedAmount =
    ConvertedAmount(
      amount = if (haveSameFromAndTo(conv, rate)) {
        amount.multiplyBy(rate.rate)
      }
      else {
        amount.divideBy(rate.rate)
      },
      currency = conv.to,
      source = rate.source
    )

  private def getIncompatibleConversionAndRateMessage(conv: CurrencyConversion, rate: ExchangeRate): String =
    s"Cannot convert from ${conv.from.getCurrencyCode} to ${conv.to.getCurrencyCode} with " +
      s"exchange rate ${rate.from.getCurrencyCode} to ${rate.to.getCurrencyCode}"
}
