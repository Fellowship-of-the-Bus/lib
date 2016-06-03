package com.github.fellowship_of_the_bus.lib
package game

trait Game {
  protected var over: Boolean = false
  def isGameOver: Boolean = over
  def gameOver(): Unit = over = true
}
