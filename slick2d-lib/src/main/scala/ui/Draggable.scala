package com.github.fellowship_of_the_bus
package lib.slick2d.ui

import org.newdawn.slick.{GameContainer, Graphics, Color, Input, MouseListener}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

trait Draggable extends UIElement with MouseListener {
  def mouseClicked(button: Int, x: Int, y: Int, clickCount: Int): Unit
  def mouseMoved(oldx: Int, oldy: Int, newx: Int, newy: Int): Unit
  def mouseWheelMoved(change: Int): Unit

  def inputEnded(): Unit = ()
  def inputStarted(): Unit = ()
  def isAcceptingInput(): Boolean = true

  override def setInput(input: Input): Unit = {
    input.addMouseListener(this)
  }

  private var selected = false
  private var shiftX = 0.0f
  private var shiftY = 0.0f
  private val LEFT = 0

  def mouseDragged(oldx: Int, oldy: Int, newx: Int, newy: Int): Unit = {
    if (selected) {
      shiftX += (newx-oldx)
      shiftY += (newy-oldy)
    }
  }

  def mousePressed(button: Int, x: Int, y: Int): Unit = {
    if (button == LEFT && inside(x, y)) {
      selected = true
    }
  }

  def mouseReleased(button: Int, x: Int, y: Int): Unit = {
    selected = false
  }
}
