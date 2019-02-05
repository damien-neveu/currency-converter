package models

case class Amount(
  cents: Long
) {
  require(cents > 0)

  def toNumberStr: String =
    s"${cents / 100}." + f"${cents % 100}%02d"
}

object Amount {
  import play.api.mvc.QueryStringBindable
  import scala.util.{Success, Try}

  private val moneyRx = "([0-9]+)\\.?([0-9]{0,2})".r

  def apply(moneyAmount: String): Amount = moneyAmount.trim match {
    case moneyRx(integer, fractional) => Amount(
      cents = integer.toLong * 100 + toFractionalValue(fractional)
    )
    case _ => throw new IllegalArgumentException(s"invalid value $moneyAmount for Amount")
  }

  private def toFractionalValue(fractional: String): Long =
    if (fractional.isEmpty) {
      0L
    }
    else if (fractional.length == 1) {
      s"${fractional}0".toLong
    }
    else {
      fractional.toLong
    }

  implicit def amountQueryStringBindable(implicit strBinder: QueryStringBindable[String]): QueryStringBindable[Amount] = new QueryStringBindable[Amount] {
    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, Amount]] = {
      strBinder.bind("amount", params).map {
        case Right(amt) =>
          Try(Amount.apply(amt)) match {
            case Success(amount) =>
              Right(amount)
            case _ =>
              Left(s"Unable to bind amount $amt")
          }
        case _ =>
          Left(s"Unable to bind amount from query string")
      }
    }
    override def unbind(key: String, amount: Amount): String =
      strBinder.unbind("amount", amount.toNumberStr)
  }

}
