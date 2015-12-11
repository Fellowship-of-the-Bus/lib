package com.github.fellowship_of_the_bus
package lib.ui

import org.newdawn.slick.{Graphics, Color, Input}
import org.newdawn.slick.util.InputAdapter
import org.newdawn.slick.gui.MouseOverArea
import org.newdawn.slick.state.{StateBasedGame}

object ToggleButton {
  val width = Button.width
  val height = Button.height
  val cornerRadius = Button.cornerRadius

  def apply(text: String, x: Float, y: Float, action: () => Unit, query: () => Boolean)
           (implicit input: Input, state: Int, game: StateBasedGame): ToggleButton = {
    val act = () => if (game.getCurrentStateID == state) action()
    val b = new ToggleButton(text, x, y, width, height, act, query)
    b.setInput(input)
    b
  }
}

class ToggleButton(text: String, x: Float,y: Float, width: Float, height: Float,
  action: () => Unit, state: () => Boolean)
    extends Button(text, x, y, width, height, action) {

  override def render(g: Graphics): Unit = {
    val textColor =
      if (state()) Color.red
      else Color.white
    var bgColor =
      if (state()) Color.white
      else Color.darkGray

    if (isMouseOver){
      bgColor = bgColor.darker(0.2f)
    } else if (isMouseDown) {
      bgColor = bgColor.darker(0.3f)
    }

    g.setColor(bgColor)
    g.fillRoundRect(x, y, width, height, ToggleButton.cornerRadius)
    g.setColor(textColor)
    drawCentred(text, y, g)
  }
}
