package di

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import models.ExchangeRateSource
import ws.ExchangeRateWebService
import ws.impl.FreeCurrencyConverterApiWebService

class WsModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[ExchangeRateWebService]).annotatedWith(Names.named(ExchangeRateSource.`free-currency-converter-api`)
      ).to(classOf[FreeCurrencyConverterApiWebService])
  }

}
