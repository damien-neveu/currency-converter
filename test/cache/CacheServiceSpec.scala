package cache

import models.{CurrencyConversion, ExchangeRate}
import utils.Rates._

import org.scalatest.{FlatSpec, Matchers}
import scala.concurrent.Future

class CacheServiceSpec extends FlatSpec with Matchers with CacheService {

  override def saveExchangeRate(exchangeRate: ExchangeRate): Future[Unit] = Future.successful( () )

  override def getExchangeRate(currencyConversion: CurrencyConversion): Future[Option[ExchangeRate]] = Future.successful(None)

  "key" should "return a String formatted as <source-currency>_<target-currency>" in {
    key(rateEuroToUSDollar) shouldBe "EUR_USD"
    key(conversionSingaporeDollarToEuro) shouldBe "SGD_EUR"
  }

  "invertedKey" should "return a String formatted as <target-currency>_<source-currency>" in {
    invertedKey(rateEuroToUSDollar) shouldBe "USD_EUR"
    invertedKey(conversionSingaporeDollarToEuro) shouldBe "EUR_SGD"
  }

}
