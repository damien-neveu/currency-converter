package logic

import models.dtos.ConvertedAmount
import models.{Amount, CurrencyConversion, Google}
import utils.Rates._
import utils.Currencies._
import org.scalatest.{EitherValues, FlatSpec, Matchers}

import scala.concurrent.Future

class CurrencyConversionServiceSpec extends FlatSpec with Matchers with EitherValues with CurrencyConversionService {

  override def convert(amount: Amount, currencyConversion: CurrencyConversion): Future[Either[String, ConvertedAmount]] =
    Future.successful(Left("Not Implemented"))

  "convertAmount" should "return a Left if the input ExchangeRate is a Left" in {
    val eitherAmt = convertAmount(
      amount = Amount.apply("100.00"),
      conv = conversionEuroToUSDollar,
      eitherRate = Left("ExchangeRate could not be collected")
    )
    eitherAmt.left.value shouldBe "ExchangeRate could not be collected"
  }

  it should "return a Left if the input CurrencyConversion and ExchangeRate are incompatible" in {
    val eitherAmt = convertAmount(
      amount = Amount.apply("100.00"),
      conv = conversionBritishPoundToCanadianDollar,
      eitherRate = Right(rateEuroToSingaporeDollar)
    )
    eitherAmt.left.value should include("Cannot convert")
  }

  it should "successfully convert an amount with the proper ExchangeRate" in {
    val eitherAmt = convertAmount(
      amount = Amount.apply("100.00"),
      conv = conversionEuroToUSDollar,
      eitherRate = Right(rateEuroToUSDollar)
    )
    eitherAmt.right.value shouldBe ConvertedAmount(
      amount = Amount.apply("114.00"),
      currency = USD,
      source = Google
    )
  }

  it should "successfully convert an amount even with an inverted ExchangeRate" in {
    val eitherAmt = convertAmount(
      amount = Amount.apply("100.00"),
      conv = conversionEuroToUSDollar,
      eitherRate = Right(rateUSDollarToEuro)
    )
    eitherAmt.right.value shouldBe ConvertedAmount(
      amount = Amount.apply("114.00"),
      currency = USD,
      source = Google
    )
  }

}
