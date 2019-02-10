package models

import java.util.Currency

case class CurrencyConversion(
  from: Currency,
  to: Currency
) {

  def desc(separator: String): String = s"${from.getCurrencyCode}$separator${to.getCurrencyCode}"

}

object CurrencyConversion {

  import play.api.mvc.QueryStringBindable
  import scala.util.{Success, Try}

  implicit def currencyQueryStringBindable(
    implicit strBinder: QueryStringBindable[String]): QueryStringBindable[CurrencyConversion] = new QueryStringBindable[CurrencyConversion] {
    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, CurrencyConversion]] =
      for {
        eitherFrom <- strBinder.bind("from", params)
        eitherTo <- strBinder.bind("to", params)
      } yield {
        (eitherFrom, eitherTo) match {
          case (Right(from), Right(to)) =>
            (Try(Currency.getInstance(from.trim.toUpperCase)), Try(Currency.getInstance(to.trim.toUpperCase))) match {
              case (Success(fromCurrency), Success(toCurrency)) =>
                Right(CurrencyConversion(
                  from = fromCurrency,
                  to = toCurrency
                ))
              case _ =>
                Left(s"Unable to bind currencies $from and $to")
            }
          case _ =>
            Left("Unable to bind currencies 'from' and 'to'")
        }
      }

    override def unbind(key: String, currencyConversion: CurrencyConversion): String =
      strBinder.unbind("from", currencyConversion.from.getCurrencyCode) + "&" + strBinder.unbind("to", currencyConversion.to.getCurrencyCode)
  }

}
