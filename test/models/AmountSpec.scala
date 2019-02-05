package models

import org.scalatest.{FlatSpec, Matchers}

class AmountSpec extends FlatSpec with Matchers {

  // scalastyle:off magic.number
  "toNumberStr" should "convert cents into a XX.XX decimal formatted number" in {
    val amount = Amount(
      cents = 15008L
    )
    amount.toNumberStr shouldBe "150.08"
  }

  "Amount's cents constructor" should "reject negative values" in {
    an[IllegalArgumentException] should be thrownBy Amount(-1L)
  }

  "Amount's String constructor" should "accept XX.XX decimal inputs" in {
    Amount.apply("178.01") should equal(Amount(cents = 17801L))
    Amount.apply("1") should equal(Amount(cents = 100L))
    Amount.apply("0.7") should equal(Amount(cents = 70L))
  }
  // scalastyle:on magic.number

  it should "reject non-decimal or too-long-decimal inputs" in {
    an[IllegalArgumentException] should be thrownBy Amount.apply("bla bla")
    an[IllegalArgumentException] should be thrownBy Amount.apply("123.456")
  }
}
