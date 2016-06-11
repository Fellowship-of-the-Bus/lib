package com.github.fellowship_of_the_bus
package lib.slick2d

import org.newdawn.slick.{Graphics,AppGameContainer, Color}

import scala.language.implicitConversions

package object ui {
  /** draw string s centered horizontally at height h */
  def drawCentred(s: String, h: Float, g: Graphics): Unit = {
    g.drawString(s, lib.game.GameConfig.Width/2 - s.length()*5, h)
  }

  implicit def slickColor2XColor(color: Color) = SomeColor(color)
}
