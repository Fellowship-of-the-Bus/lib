package com.github.fellowship_of_the_bus.lib.game

trait Coordinates {
  def x: Float
  def y: Float
  def width: Float
  def height: Float

  def topLeftCoord(): (Float, Float) = {
    val (x, y, _, _) = coordinates
    (x, y)
  }

  def bottomRightCoord(): (Float, Float) = {
    val (_, _, x, y) = coordinates
    (x, y)
  }

  def coordinates(): (Float, Float, Float, Float)
}

trait TopLeftCoordinates extends Coordinates {
  def coordinates(): (Float, Float, Float, Float) = {
    (x, y, x+width, y+height)
  }
}

trait CenteredCoordinates extends Coordinates {
  def coordinates() = {
    (x-width/2, y-height/2, x+width/2, y+height/2)
  }
}
