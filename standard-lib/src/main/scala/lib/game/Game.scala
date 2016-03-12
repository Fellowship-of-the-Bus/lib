package com.github.fellowship_of_the_bus.lib
package game

trait Game {
  protected var isGameOver: Boolean = false

  def gameOver(): Unit = {
    isGameOver = true
  }
}
