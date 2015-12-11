package com.github.fellowship_of_the_bus.lib

import org.newdawn.slick.{Graphics,AppGameContainer}

package object ui {
  /** draw string s centered horizontally at height h */
  def drawCentred(s: String, h: Float, g: Graphics): Unit = {
    g.drawString(s, game.GameConfig.Width/2 - s.length()*5, h)
  }
}
