package ws.impl

import play.api.Configuration
import play.core.server.Server
import play.api.routing.sird._
import play.api.libs.json._
import play.api.test.WsTestClient

import scala.concurrent.ExecutionContext

trait FakeFreeCurrencyConverterApiWebService {

  implicit def ctx: ExecutionContext

  def withFakeFreeCurrencyConverterApiWebService[T](conf: Configuration)(block: FreeCurrencyConverterApiWebService => T): T = {
    Server.withRouterFromComponents() { components =>
      import play.api.mvc.Results._
      import components.{ defaultActionBuilder => Action }
      {
        case GET(p"/api/v6/convert") => Action { implicit request => {
            val optQ = request.getQueryString("q")
            if (optQ.contains("USD_EUR")) {
              Ok(Json.obj("USD_EUR" -> 0.882695))
            }
            else {
//              println(s"FORBIDS calling /api/v6/convert with q=${optQueryStringQ.getOrElse("")}")
              Forbidden(s"FakeFreeCurrencyConverterApiWebService rejects request for ${optQ.getOrElse("")}")
            }
          }
        }
        case otherCall => Action {
//          println(s"NOT_FOUND endpoint $otherCall to withFakeFreeCurrencyConverterApiWebService")
          NotFound(s"FakeFreeCurrencyConverterApiWebService has no such endpoint $otherCall")
        }
      }
    } { implicit port =>
      WsTestClient.withClient { wsClient =>
        block(new FreeCurrencyConverterApiWebService(conf, wsClient))
      }
    }
  }

}
