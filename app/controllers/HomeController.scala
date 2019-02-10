package controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class HomeController @Inject()(conf: Configuration, cc: ControllerComponents) extends AbstractController(cc) {

  def health() = Action {
    Ok(s"${conf.get[String]("project.name")} is healthy")
  }

}
