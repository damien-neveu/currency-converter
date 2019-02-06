package cache.impl

import cache.CacheService
import javax.inject.{Inject, Singleton}
import models.{CurrencyConversion, ExchangeRate}
import play.api.Configuration
import play.api.cache.AsyncCacheApi

import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LocalCacheService @Inject()(cache: AsyncCacheApi, conf: Configuration)(implicit ctx: ExecutionContext) extends CacheService {

  private val ttl: Duration = conf.get[Int]("cache.ttl-in-minutes").minutes

  override def saveExchangeRate(exchangeRate: ExchangeRate): Future[Unit] =
    cache.set(key = key(exchangeRate), value = exchangeRate, expiration = ttl).map(_ => ())

  override def getExchangeRate(currencyConversion: CurrencyConversion): Future[Option[ExchangeRate]] =
    cache.get[ExchangeRate](key(currencyConversion)).flatMap {
      case Some(rate) =>
        Future.successful(Some(rate))
      case None =>
        cache.get(invertedKey(currencyConversion))
    }

}
