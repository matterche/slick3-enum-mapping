package models

import javax.inject.{Inject, Singleton}

import models.Bar.Bar
import models.Color.Color
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext

object Color extends Enumeration {
  type Color = Value
  val Blue = Value("Blue")
  val Red = Value("Red")
  val Green = Value("Green")
}

object Bar extends Enumeration {
  type Bar = Value
  val b1 = Value
  val b2 = Value
  val b3 = Value
}

case class Sample(name:String, id:Int, c:Color, b:Bar)

// Schemas
@Singleton
class ColorDao @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import driver.api._

  class SampleTable(tag: Tag) extends Table[Sample](tag, "Sample") {
    def name  = column[String]("NAME")
    def id    = column[Int]("ID")
    def color = column[Color]("COLOR")
    def bar   = column[Bar]("BAR")
    def * = (name, id, color, bar) <> (Sample.tupled, Sample.unapply)
  }
  implicit val colorMapper = MappedColumnType.base[Color, String](
    e => e.toString,
    s => Color.withName(s)
  )
  implicit val barMapper = MappedColumnType.base[Bar, Int](
    e => e.id,
    i => Bar.apply(i)
  )
}