package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Configuration
import play.api.test._
import play.api.test.Helpers._

class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  private val conf = Configuration.from(Map("project.name" -> "currency-converter"))

  "HomeController health endpoint" should {

    "return 200" in {
      val controller = new HomeController(conf, stubControllerComponents())
      val fRes = controller.health().apply(FakeRequest(GET, "/"))

      status(fRes) mustBe OK
      contentAsString(fRes) mustBe "currency-converter is healthy"
    }
  }
}
