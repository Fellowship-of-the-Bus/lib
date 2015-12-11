package com.github.fellowship_of_the_bus.lib
package math

import game.Coordinates
import Coordinates.{Val, CoordPair}

object Rect {
  def intersect(obj1: Coordinates, obj2: Coordinates): Boolean =
    intersect(obj1.coordinates, obj2.coordinates)

  /** check intersection between two axis-aligned rectangles given by
    * (x1, y1, x2, y2) */
  def intersect(coord1: CoordPair, coord2: CoordPair): Boolean = {
    val (x1, y1, x2, y2) = coord1
    val (cx1, cy1, cx2, cy2) = coord2
    Rect(x1,y1,x2,y2).intersect(cx1,cy1,cx2,cy2)
  }

  def apply(x1: Val, y1: Val, x2: Val, y2: Val): Rect = {
    val nx1 = min(x1, x2)
    val nx2 = max(x1, x2)
    val ny1 = min(y1, y2)
    val ny2 = max(y1, y2)
    new Rect(nx1, ny1, nx2, ny2)
  }

  def unapply(rect: Rect): Option[CoordPair] =
    Some((rect.x1, rect.y1, rect.x2, rect.y2))
}

class Rect(val x1: Val, val y1: Val, val x2: Val, val y2: Val) {
  require(x1 <= x2)
  require(y1 <= y2)

  private def inRange(v: Val, min: Val, max: Val) = (v >= min) && (v <= max)

  def intersect(cx1: Val, cy1: Val, cx2: Val, cy2: Val): Boolean = {
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
