package ws.impl

import java.time.ZonedDateTime

import javax.inject.Inject
import models._
import org.slf4j.{Logger, LoggerFactory}
import play.api.Configuration
import play.api.http.Status
import play.api.libs.json._
import play.api.libs.ws.{WSClient, WSResponse}
import ws.ExchangeRateWebService

import scala.concurrent.duration._
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}
import scala.math.BigDecimal.RoundingMode

class EuropeanCentralBankWebService @Inject()(
    conf: Configuration,
    ws: WSClient
  )(implicit val ctx: ExecutionContext) extends ExchangeRateWebService {

  import EuropeanCentralBankWebService._

  private val log: Logger = LoggerFactory.getLogger(classOf[EuropeanCentralBankWebService])

  private val timeout: Duration = conf.get[Int](s"$confPath.timeout-in-millis").millis

  override val urlTemplate: String = s"${conf.get[String](s"$confPath.host")}${
                                         conf.get[String](s"$confPath.convert-url-format")}"

  override def getExchangeRate(conv: CurrencyConversion): Future[Either[String, ExchangeRate]] = {
    val url = buildUrlWithoutEuro(conv)
    log.info(s"About to GET $url")
    ws.url(url)
      .withRequestTimeout(timeout)
      .get()
      .map { handleResponse(url, conv) }
      .recoverWith { handleException(url, conv) }
  }

  private def handleResponse(url: String, conv: CurrencyConversion): WSResponse => Either[String, ExchangeRate] = response =>
    response.status match {
      case Status.OK =>
        extractConversion(conv, response.json) match {
          case Right(exchangeRate) =>
            log.info(s"GET $url successfully returned ${response.json}")
            Right(ExchangeRate(
              from = conv.from,
              to = conv.to,
              rate = exchangeRate,
              source = EuropeanCentralBank,
              lastUpdatedAt = extractDate(response.json)
            ))
          case _ =>
            log.warn(s"GET $url returned OK but rates could not be extracted from the response ${response.json}")
            Left(s"Cannot extract exchange rates from $EuropeanCentralBank response ${response.json}")
        }
      case _ =>
        log.warn(s"GET $url returned ${response.status} with body ${response.body}")
        Left(s"No exchange rate returned by $EuropeanCentralBank in response ${response.body}")
    }

  private def handleException(url: String, conv: CurrencyConversion): PartialFunction[Throwable, Future[Either[String, ExchangeRate]]] = {
    case throwable: Throwable =>
      log.warn(s"GET $url failed", throwable)
      Future.successful(Left(
        s"Cannot get exchange rate ${conv.desc(separator)} from $EuropeanCentralBank because of exception ${throwable.getMessage}"
      ))
  }

  private def extractConversion(conv: CurrencyConversion, json: JsValue): Either[String, Double] = {
    val jsRates: JsLookupResult = json \ "rates"
    (conv.from.getCurrencyCode, conv.to.getCurrencyCode) match {
      case (from, to) if !EUR.equals(from) && !EUR.equals(to) =>
        (jsRates \ from, jsRates \ to) match {
          case (JsDefined(JsNumber(frRate)), JsDefined(JsNumber(toRate))) =>
            Right((toRate / frRate).setScale(scale, RoundingMode.HALF_EVEN).toDouble)
          case _ =>
            Left(s"Cannot extract $from and $to from response body ${json.toString}")
        }
      case (EUR, to) if !EUR.equals(to) =>
        jsRates \ to match {
          case JsDefined(JsNumber(toRate)) =>
            Right(toRate.toDouble)
          case _ =>
            Left(s"Cannot extract $to from response body ${json.toString}")
        }
      case (from, EUR) if !EUR.equals(from) =>
        jsRates \ from match {
          case JsDefined(JsNumber(frRate)) =>
            Right((1 / frRate).setScale(scale, RoundingMode.HALF_EVEN).toDouble)
          case _ =>
            Left(s"Cannot extract $from from response body ${json.toString}")
        }
    }
  }

  def extractDate(json: JsValue): ZonedDateTime =
    json \ "date" match {
      case JsDefined(JsString(dateStr)) => ZonedDateTime.parse(s"${dateStr}T16:00:00.000+01:00")
      case _ => ZonedDateTime.now
    }

  private def buildUrlWithoutEuro(conv: CurrencyConversion): String =
    buildUrl(conv).replaceAll(s",?$EUR,?", "")

}

object EuropeanCentralBankWebService {

  val confPath: String = s"exchange-rate-providers.${ExchangeRateSource.`european-central-bank`}"
  val EUR: String = "EUR"
  val separator: String = ","
  val scale: Int = 9

}
