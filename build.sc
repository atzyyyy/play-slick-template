import mill._
import $ivy.`com.lihaoyi::mill-contrib-playlib:`,  mill.playlib._

object playslicktemplate extends PlayModule with SingleModule {

  def scalaVersion = "2.12.10"
  def playVersion = "2.8.18"
  def twirlVersion = "1.6.0-RC2"

  object test extends PlayTests
}
