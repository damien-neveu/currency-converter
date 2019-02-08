package ws.impl

import java.time.ZonedDateTime

import javax.inject.{Inject, Singleton}
import models.{CurrencyConversion, ExchangeRate, ExchangeRateSource, FreeCurrencyConverter}
import org.slf4j.{Logger, LoggerFactory}
import play.api.Configuration
import play.api.http.Status
import play.api.libs.json.{JsDefined, JsNumber}
import play.api.libs.ws.{WSClient, WSResponse}
import ws.ExchangeRateWebService

import scala.concurrent.duration._
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FreeCurrencyConverterApiWebService @Inject()(
    conf: Configuration,
    ws: WSClient
  )(implicit val ctx: ExecutionContext) extends ExchangeRateWebService {

  import FreeCurrencyConverterApiWebService._

  private val log: Logger = LoggerFactory.getLogger(classOf[FreeCurrencyConverterApiWebService])

  private val urlTemplate: String = s"${conf.get[String](s"$confPath.host")}${
                                        conf.get[String](s"$confPath.convert-url-format")}"
  private val timeout: Duration = conf.get[Int](s"$confPath.timeout-in-millis").millis

  override def getExchangeRate(conv: CurrencyConversion): Future[Either[String, ExchangeRate]] = {
    val url: String = urlTemplate.format(conv.from.getCurrencyCode, conv.to.getCurrencyCode)
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
        response.json \ getConversionKey(conv) match {
          case JsDefined(JsNumber(exchangeRate)) =>
            log.info(s"GET $url successfully returned ${response.json}")
            Right(ExchangeRate(
              from = conv.from,
              to = conv.to,
              rate = exchangeRate.toDouble,
              source = FreeCurrencyConverter,
              lastUpdatedAt = ZonedDateTime.now
            ))
          case _ =>
            log.warn(s"GET $url returned OK but field ${getConversionKey(conv)} could not be extracted from the response ${response.json}")
            Left(s"Cannot extract exchange rate from $FreeCurrencyConverter response ${response.json}")
        }
      case _ =>
        log.warn(s"GET $url returned ${response.status} with body ${response.body}")
        Left(s"No exchange rate returned by $FreeCurrencyConverter in response ${response.body}")
    }

  private def handleException(url: String, conv: CurrencyConversion): PartialFunction[Throwable, Future[Either[String, ExchangeRate]]] = {
    case throwable: Throwable =>
      log.warn(s"GET $url failed", throwable)
      Future.successful(Left(
        s"Cannot get exchange rate ${getConversionKey(conv)} from $FreeCurrencyConverter because of exception ${throwable.getMessage}"
      ))
  }

  private def getConversionKey(conv: CurrencyConversion): String =
    s"${conv.from.getCurrencyCode}_${conv.to.getCurrencyCode}"

}

object FreeCurrencyConverterApiWebService {

  val confPath: String = s"exchange-rate-providers.${ExchangeRateSource.`free-currency-converter-api`}"

}
