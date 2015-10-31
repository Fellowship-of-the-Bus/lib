package com.github.fellowship_of_the_bus.lib
package math

import game.Coordinates

object Rect {
  def intersect(obj1: Coordinates, obj2: Coordinates): Boolean =
    intersect(obj1.coordinates, obj2.coordinates)

  /** check intersection between two axis-aligned rectangles given by
    * (x1, y1, x2, y2) */
  def intersect(coord1: (Float, Float, Float, Float), coord2: (Float, Float, Float, Float)): Boolean = {
    val (x1, y1, x2, y2) = coord1
    val (cx1, cy1, cx2, cy2) = coord2
    Rect(x1,y1,x2,y2).intersect(cx1,cy1,cx2,cy2)
  }
}

case class Rect(x1: Float, y1: Float, x2: Float, y2: Float) {
  require(x1 <= x2)
  require(y1 <= y2)

  private def inRange(v: Float, min: Float, max: Float) = (v >= min) && (v <= max)

  def intersect(cx1: Float, cy1: Float, cx2: Float, cy2: Float): Boolean = {
    require(cx1 <= cx2)
    require(cy1 <= cy2)
    val xOver = inRange(cx1,x1,x2) || inRange(x1, cx1, cx2)
    val yOver = inRange(cy1,y1,y2) || inRange(y1, cy1, cy2)
    xOver && yOver  
  }

  def intersect(rect: Rect): Boolean = 
    intersect(rect.x1, rect.y1, rect.x2, rect.y2)

  def intersect(obj: Coordinates): Boolean = {
    val (x1, y1, x2, y2) = obj.coordinates
    intersect(x1, y1, x2, y2)
  }
}
