package com.github.fellowship_of_the_bus.lib
package game

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

  def topLeftCoord: (Float, Float)

  def draw(g: Graphics) =
    if (GameConfig.showLifebars) {
      import Lifebar._
      val (x, y) = topLeftCoord

      g.setColor(Color.black)
      g.drawRect(x, y-dist, width, height)
      g.setColor(Color.red)
      g.fillRect(x, y-dist, width * hp/maxHp, height)
    }
}
