package ws.impl

import models.FreeCurrencyConverter
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{EitherValues, FlatSpec, Matchers}
import play.api.Configuration
import ws.impl.FreeCurrencyConverterApiWebService.confPath
import utils.Rates.{conversionBritishPoundToCanadianDollar, conversionUSDollarToEuro}

import scala.concurrent.ExecutionContext

class FreeCurrencyConverterApiWebServiceSpec extends FlatSpec
      with Matchers with ScalaFutures with EitherValues with FakeFreeCurrencyConverterApiWebService {

  override val ctx: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  private val conf: Configuration = Configuration.from(Map(
    s"$confPath.host" -> "",
    s"$confPath.convert-url-format" -> "/api/v6/convert?q=%s_%s&compact=ultra",
    s"$confPath.timeout-in-millis" -> 10000
  ))

  // scalastyle:off magic.number
  implicit val defaultPatience: PatienceConfig =
    PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))
  // scalastyle:on magic.number

  "getExchangeRate" should "return an exchange rate when the API responds successfully" in {
    withFakeFreeCurrencyConverterApiWebService(conf) { service =>
      whenReady(service.getExchangeRate(conversionUSDollarToEuro)) { res =>
        res.right.value.source shouldBe FreeCurrencyConverter
      }
    }
  }

  it should "return a Left when the API responds unsuccessfully" in {
    withFakeFreeCurrencyConverterApiWebService(conf) { service =>
      whenReady(service.getExchangeRate(conversionBritishPoundToCanadianDollar)) { res =>
        res.left.value should include("No exchange rate returned by")
      }
    }
  }

}
