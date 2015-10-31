package com.github.fellowship_of_the_bus.lib

package object math {
  def max[A](objs: A*)(implicit cmp: Ordering[A]): A = objs.max
  def min[A](objs: A*)(implicit cmp: Ordering[A]): A = objs.min

  def floor(arg: Double): Int = scala.math.floor(arg).toInt
  def sqrt(arg: Double): Float = scala.math.sqrt(arg).toFloat

  /** if lower <= v <= upper, returns v, else returns the closer bound */
  def clamp[A](v: A, lower: A, upper: A)(implicit cmp: Ordering[A]): A = {
    require(cmp.lteq(lower, upper))
    if (cmp.lt(v, lower)) lower
    else if (cmp.gt(v, upper)) upper
    else v
  }

}


