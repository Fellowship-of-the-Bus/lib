package com.github.fellowship_of_the_bus
package lib.slick2d
package game

import lib.game.GameConfig

import org.newdawn.slick.{Graphics, Color}

object Lifebar {
  /** width of a lifebar */
  val width = 40
  /** height of a lifebar */
  val height = 5
  /** vertical distance between a lifebar and the top left corner of the image */
  val dist = 10
}

trait Lifebar {
  def hp: Float
  def maxHp: Float

  def topLeftCoord(): (Float, Float)

  def draw(g: Graphics, x: Float, y: Float): Unit =
    if (GameConfig.showLifebars && hp < maxHp) {
      import Lifebar._

      val color = g.getColor

      g.setColor(Color.black)
      g.drawRect(x, y-dist, width, height)
      g.setColor(Color.red)
      g.fillRect(x, y-dist, width * hp/maxHp, height)

      g.setColor(color)
    }
}
