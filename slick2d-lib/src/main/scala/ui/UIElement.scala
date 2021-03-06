package com.github.fellowship_of_the_bus
package lib.slick2d.ui

import org.newdawn.slick.{GameContainer, Graphics, Input}
import org.newdawn.slick.state.{StateBasedGame}

trait UIElement {
  def x: Float
  def y: Float
  def width: Float
  def height: Float
  val topLeft = (x, y)
  val bottomRight = (x+width, y+height)

  // mouse needs to know absolute coordinates...
  var absoluteX = x
  var absoluteY = y

  var sbg: StateBasedGame = null

  def update(gc: GameContainer, sbg: StateBasedGame, delta: Int): Unit
  def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = draw(gc, sbg, g)
  def init(gc: GameContainer, sbg: StateBasedGame): Unit = {
    this.sbg = sbg
    setInput(gc.getInput)
  }

  def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit

  // default implementation do that init can call setInput
  def setInput(input: Input): Unit = ()

  var uiState: Int = -1
  def setState(st: Int): Unit = {
    uiState = st
  }

  private var visible: () => Boolean = () => true
  def setIsVisible(vis: () => Boolean): this.type = {
    visible = vis
    this
  }
  def isVisible(): Boolean = visible()

  protected def inside(newx: Int, newy: Int) =
    absoluteX < newx && newx < absoluteX+width && absoluteY < newy && newy < absoluteY+height
}

abstract class AbstractUIElement(val x: Float, val y: Float, val width: Float, val height: Float)
extends UIElement {}

