package com.github.fellowship_of_the_bus.lib.game

object Coordinates {
  type Val = Float
  type Coord = (Val, Val)
  type CoordPair = (Val, Val, Val, Val)
}
import Coordinates._

trait Coordinates {
  def x: Val
  def y: Val
  def width: Val
  def height: Val

  def topLeftCoord(): Coord = {
    val (x, y, _, _) = coordinates
    (x, y)
  }

  def bottomRightCoord(): Coord = {
    val (_, _, x, y) = coordinates
    (x, y)
  }

  def distance (coord: Coordinates): Val = {
    val (x1, y1) = centerCoord
    val (x2, y2) = coord.centerCoord
    val dx = x1 - x2
    val dy = y1 - y2
    Math.sqrt(dx*dx + dy*dy).toFloat
  }

  def coordinates(): CoordPair
  def centerCoord(): Coord
}

trait TopLeftCoordinates extends Coordinates {
  def coordinates(): CoordPair = {
    (x, y, x+width, y+height)
  }

  def centerCoord(): Coord = (x+width/2,y+height/2)
}

trait CenteredCoordinates extends Coordinates {
  def coordinates(): CoordPair = {
    (x-width/2, y-height/2, x+width/2, y+height/2)
  }

  def centerCoord(): Coord = (x, y)
}
