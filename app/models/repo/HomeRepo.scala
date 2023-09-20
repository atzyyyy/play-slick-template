package models.repo

import javax.inject._
import scala.concurrent.ExecutionContext
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.Future

//import type from domain
import models.domain.Home

import java.util.UUID


@Singleton
final class HomeRepo @Inject()
(protected val dbConfigProvider: DatabaseConfigProvider)
(implicit ex: ExecutionContext) 
extends HasDatabaseConfigProvider[JdbcProfile] {
    import profile.api._

     protected class Homes (tag: Tag) extends Table[Home](tag, "HOMES") {
        def id = column[UUID]("ID", O.PrimaryKey)
        def name = column[String]("name")

        // def * = (id, name) <> (Account.tupled, Account.unapply)
        def * = (id, name).mapTo[Home]
    }
    val homes = TableQuery[Homes]

    def createSchema = db.run(homes.schema.createIfNotExists)

    def create(home: Home): Future[Int] = db.run(homes += home)
    def find(id: UUID): Future[Option[Home]] = db.run(homes.filter(_.id === id).result.headOption)
    //search for name through substring
    def getOne(name: String): Future[Seq[Home]] = db.run(homes.filter(_.name like s"%$name%").result)
    def getAll(): Future[Seq[Home]] = db.run(homes.result)    
    def update(id: UUID, updatedInfo: Home): Future[Int] = db.run(homes.filter(_.id === id).update(updatedInfo))
    def delete(id: UUID): Future[Int] = db.run(homes.filter(_.id === id).delete)
}
