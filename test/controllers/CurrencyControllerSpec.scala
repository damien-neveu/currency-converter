package controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.libs.ws.WSClient
import play.api.test.Injecting
import play.api.test.Helpers._

class CurrencyControllerSpec extends PlaySpec with GuiceOneServerPerTest with Injecting {

  "CurrencyController GET /v1/amount" must {

    lazy val wsClient = inject[WSClient]
    lazy val url = s"http://localhost:$port/v1/amount"

    "return BadRequest 400 when mandatory query string params are missing or malformed" in {
      val response = await(wsClient.url(url).addQueryStringParameters("from" -> "SGD", "to" -> "ZZZ", "amount" -> "100.50").get())
//      print(s"response=${response.body}")
      response.status mustBe BAD_REQUEST
      response.body must include("Unable to bind currencies SGD and ZZZ")
    }

    "return Ok 200 when all params are present and downstream web services respond" ignore {
      val response = await(wsClient.url(url).addQueryStringParameters("from" -> "SGD", "to" -> "EUR", "amount" -> "100.50").get())
//      print(s"response=${response.body}")
      response.status mustBe OK
    }

  }

}
