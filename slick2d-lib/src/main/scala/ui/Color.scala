package com.github.fellowship_of_the_bus.lib.slick2d.ui
import org.newdawn.slick.{Animation => SlickAnimation, Image => SlickImage, Graphics, Color}

object ColorImplicits {
  import scala.language.implicitConversions
  implicit def fourTuple2Color(rgba: (Int, Int, Int, Int)) = {
    val (r, g, b, a) = rgba
    new Color(r, g, b, a)
  }
  implicit class ColorOps(val c: Color) extends AnyVal {
    def +(other: Color): Color = c.addToCopy(other)
    def *(other: Color): Color = {
      // it's not clear from the slick docs whether multiply modifies c
      val cpy = new Color(c)
      cpy.multiply(other)
    }
    def rbg: (Int, Int, Int) = (c.getRed, c.getBlue, c.getGreen)
    def opacity(alpha: Int): Color = new Color(c.getRed, c.getBlue, c.getGreen, alpha)
    def opacity(alpha: Float): Color = new Color(c.getRed/255f, c.getBlue/255f, c.getGreen/255f, alpha)
  }
}

/** wrapper to allow passing no color simply */
trait XColor {
  def color: Color
}
case object NoColor extends XColor {
  def color: Color = throw new UnsupportedOperationException("Cannot call color on NoColor")
}
case class SomeColor(val color: Color) extends XColor

