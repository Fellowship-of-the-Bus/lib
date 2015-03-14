package com.github.fellowship_of_the_bus

import org.newdawn.slick.{Graphics,AppGameContainer}

package object ui {
  def drawCentred(s: String, w: Float, h: Float, g: Graphics) = {
    g.drawString(s, w/2 - s.length()*5, h)
  }
}
