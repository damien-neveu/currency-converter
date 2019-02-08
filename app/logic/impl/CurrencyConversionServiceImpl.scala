package logic.impl

import javax.inject.{Inject, Named}
import cache.CacheService
import logic.CurrencyConversionService
import models.dtos.ConvertedAmount
import models.{Amount, CurrencyConversion, ExchangeRateSource}
import ws.ExchangeRateWebService

import scala.concurrent.{ExecutionContext, Future}

class CurrencyConversionServiceImpl @Inject()(
    cacheService: CacheService,
    @Named(ExchangeRateSource.`free-currency-converter-api`) freeCurrencyConverterApi: ExchangeRateWebService
  )(implicit val ctx: ExecutionContext) extends CurrencyConversionService {

  override def convert(amount: Amount, currencyConversion: CurrencyConversion): Future[Either[String, ConvertedAmount]] = {
//    cacheService.getExchangeRate(currencyConversion).flatMap {
//      case Some(rate) =>
//        Future.successful(convertAmount(amount, currencyConversion, Right(rate)))
//      case None =>
//        for {
//          eitherRate <- collectRateFromRandomService(currencyConversion)
//          eitherConvertedAmount <- Future.successful(convertAmount(amount, currencyConversion, eitherRate))
//          if eitherRate.isRight
//          _ <- cacheService.saveExchangeRate(eitherRate.toOption.get)
//        } yield eitherConvertedAmount
//    }
    ???
  }

}
