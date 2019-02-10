package ws.impl

import play.api.Configuration
import play.api.libs.json.Json
import play.api.test.WsTestClient
import play.core.server.Server
import play.api.routing.sird._

import scala.concurrent.ExecutionContext

trait FakeEuropeanCentralBankWebService {

  implicit def ctx: ExecutionContext

  def withFakeEuropeanCentralBankWebService[T](conf: Configuration)(block: EuropeanCentralBankWebService => T): T = {
    Server.withRouterFromComponents() { components =>
      import play.api.mvc.Results._
      import components.{ defaultActionBuilder => Action }
    {
      case GET(p"/latest") => Action { implicit request => {
        val optSymbols = request.getQueryString("symbols")
        if (optSymbols.contains("SGD,CAD")) {
          Ok(Json.obj(
            "base" -> "EUR",
            "date" -> "2019-02-08",
            "rates" -> Json.obj(
              "SGD" -> 1.5376,
              "CAD" -> 1.5098
            ))
          )
        }
        else if (optSymbols.contains("PHP")) {
          Ok(Json.obj(
            "base" -> "EUR",
            "date" -> "2019-02-08",
            "rates" -> Json.obj(
              "PHP" -> 59.149
            ))
          )
        }
        else {
          Forbidden(s"FakeEuropeanCentralBankWebService rejects request for ${optSymbols.getOrElse("")}")
        }
      }
      }
      case otherCall => Action {
        NotFound(s"FakeEuropeanCentralBankWebService has no such endpoint $otherCall")
      }
    }
    } { implicit port =>
      WsTestClient.withClient { wsClient =>
        block(new EuropeanCentralBankWebService(conf, wsClient))
      }
    }
  }

}
