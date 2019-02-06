package di

import com.google.inject.AbstractModule
import logic.CurrencyConversionService
import logic.impl.CurrencyConversionServiceImpl

class LogicModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[CurrencyConversionService]).to(classOf[CurrencyConversionServiceImpl])
  }

}
