package com.github.fellowship_of_the_bus
package lib
package slick2d
package game

import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.{StateBasedGame}

trait Game extends lib.game.Game {
  def update(gc: GameContainer, game: StateBasedGame, delta: Int): Unit
}
