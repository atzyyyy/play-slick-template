package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.concurrent.Future
import play.api.data._
import play.api.data.Forms._
import java.util.UUID
import scala.concurrent.ExecutionContext

import models.domain.Home
import models.repo.HomeRepo

@Singleton
class HomeController @Inject()
(val controllerComponents: ControllerComponents, homeRepo: HomeRepo, implicit val ec: ExecutionContext)
extends BaseController {

  val createForm = Form(
    mapping(
      "id" -> ignored(UUID.randomUUID()),
      "name" -> nonEmptyText
    )
    (Home.apply)(Home.unapply)
   )

  def index() = Action.async { implicit request =>
    homeRepo.createSchema.map(_ => Ok)
  }

  def create() = Action.async { implicit request => 
    createForm.bindFromRequest().fold(
      formsWithError => Future.successful(BadRequest),
      home => homeRepo.create(home.copy(id = UUID.randomUUID())).map(_ => Ok)
    )  
  }
  def getAcc(query: String) = Action.async { implicit request =>
    homeRepo.getOne(query).map(homes => Ok(homes.mkString("\n")))
  }

  def getAll() = Action.async{ implicit request =>
    homeRepo.getAll().map(homes => Ok(homes.mkString("\n")))  
  }

  def update(id: UUID) = Action.async { implicit request =>
    createForm.bindFromRequest().fold(
      formsWithError => Future.successful(BadRequest),
      home => homeRepo.update(id, home.copy(id = id)).map(_ => Ok)
    )
  }

  def delete(id: UUID) = Action.async { implicit request =>
    homeRepo.delete(id).map(_ => Ok)
  }

}
