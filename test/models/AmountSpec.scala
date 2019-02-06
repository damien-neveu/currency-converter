package models

import org.scalatest.{FlatSpec, Matchers}

class AmountSpec extends FlatSpec with Matchers {

  // scalastyle:off magic.number
  private val amount = Amount(
    cents = 15008L
  )

  "toNumberStr" should "convert cents into a XX.XX decimal formatted number" in {
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

  it should "reject non-decimal or too-long-decimal inputs" in {
    an[IllegalArgumentException] should be thrownBy Amount.apply("bla bla")
    an[IllegalArgumentException] should be thrownBy Amount.apply("123.456")
  }

  "multiplyBy" should "return a new Amount with a multiplied number of cents" in {
    val newAmount = amount.multiplyBy(1.13857) // 150.08 * 1.13857 = 170.87659
    newAmount should equal(Amount(
      cents = 17088L
    ))
  }

  "divideBy" should "return a new Amount with a divided number of cents" in {
    val newAmount = amount.divideBy(0.878015) // 150.08 / 0.878015 = 170.93102...
    newAmount should equal(Amount(
      cents = 17093L
    ))
  }
  // scalastyle:on magic.number

}
