package cache

import utils.Rates._
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import org.scalatest.concurrent.ScalaFutures
import play.api.test.Injecting

import scala.concurrent.ExecutionContext

class LocalCacheServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with ScalaFutures {

  implicit val ctx: ExecutionContext = scala.concurrent.ExecutionContext.global
  private lazy val cacheService = inject[CacheService]

  "LocalCacheService implemented with caffeine" should {

    "return nothing when the requested ExchangeRate is not cached" in {
      val fCachedRate = cacheService.getExchangeRate(conversionBritishPoundToCanadianDollar)
      whenReady(fCachedRate) { optCachedRate =>
        optCachedRate mustBe empty
      }
    }

    "return a previously cached ExchangeRate" in {
      val fCachedRate = for {
        _ <- cacheService.saveExchangeRate(rateEuroToUSDollar)
        optRate <- cacheService.getExchangeRate(conversionEuroToUSDollar)
      } yield optRate
      whenReady(fCachedRate) { optCachedRate =>
        optCachedRate must equal(Some(rateEuroToUSDollar))
      }
    }

    "return a previously cached ExchangeRate even if the requested conversion is inverse to the rate" in {
      val fCachedRate = for {
        _ <- cacheService.saveExchangeRate(rateEuroToSingaporeDollar)
        optRate <- cacheService.getExchangeRate(conversionSingaporeDollarToEuro)
      } yield optRate
      whenReady(fCachedRate) { optCachedRate =>
        optCachedRate must equal(Some(rateEuroToSingaporeDollar))
      }
    }

  }

}
