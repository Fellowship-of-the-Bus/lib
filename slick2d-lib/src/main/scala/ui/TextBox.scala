package com.github.fellowship_of_the_bus
package lib
package slick2d
package ui

import lib.game.GameConfig.{Width,Height}

import org.newdawn.slick.{GameContainer, Graphics, Color}
import org.newdawn.slick.state.{StateBasedGame}

class TextBox(x: Float, y: Float, width: Float, height: Float, query: () => String)
             (implicit bg: Color)
    extends Pane(x, y, width, height) {

  override def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    super.draw(gc, sbg, g)
    g.drawString(query(), 2, 0)
  }
}
