package models

case class Amount(
  cents: Long
) {
  require(cents > 0)

  def toNumberStr: String =
    s"${cents / 100}." + f"${cents % 100}%02d"
}

object Amount {

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

}
