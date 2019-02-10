package ws.impl

import java.time.{ZoneOffset, ZonedDateTime}

import models.{EuropeanCentralBank, ExchangeRate}
import org.scalatest.{EitherValues, FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import play.api.Configuration
import utils.Rates._
import utils.Currencies._
import ws.impl.EuropeanCentralBankWebService.confPath

import scala.concurrent.ExecutionContext

class EuropeanCentralBankWebServiceSpec extends FlatSpec
  with Matchers with ScalaFutures with EitherValues with FakeEuropeanCentralBankWebService {

  override val ctx: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  private val conf: Configuration = Configuration.from(Map(
    s"$confPath.host" -> "",
    s"$confPath.convert-url-format" -> "/latest?symbols=%s,%s",
    s"$confPath.timeout-in-millis" -> 10000
  ))

  // scalastyle:off magic.number
  private val Feb8th4pmCET: ZonedDateTime = ZonedDateTime.of(2019,2,8,16,0,0,0,ZoneOffset.ofHours(1))

  implicit val defaultPatience: PatienceConfig =
    PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  "getExchangeRate" should "return an exchange rate when the API responds successfully" in {
    withFakeEuropeanCentralBankWebService(conf) { service =>
      whenReady(service.getExchangeRate(conversionSingaporeDollarToCanadianDollar)) { res =>
        res.right.value shouldBe ExchangeRate(
          from = SGD,
          to = CAD,
          rate = 0.981919875,
          source = EuropeanCentralBank,
          lastUpdatedAt = Feb8th4pmCET
        )
      }
    }
  }

  it should "return an exchange rate when one of the rates is EUR" in {
    withFakeEuropeanCentralBankWebService(conf) { service =>
      whenReady(service.getExchangeRate(conversionEuroToPhilippinePeso)) { res =>
        res.right.value shouldBe ExchangeRate(
          from = EUR,
          to = PHP,
          rate = 59.149,
          source = EuropeanCentralBank,
          lastUpdatedAt = Feb8th4pmCET
        )
      }
    }
  }

  it should "return a Left when the API responds unsuccessfully" in {
    withFakeEuropeanCentralBankWebService(conf) { service =>
      whenReady(service.getExchangeRate(conversionBritishPoundToCanadianDollar)) { res =>
        res.left.value should include("No exchange rate returned by")
      }
    }
  }

  // scalastyle:on magic.number
}
