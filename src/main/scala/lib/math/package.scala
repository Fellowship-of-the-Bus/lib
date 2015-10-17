package com.github.fellowship_of_the_bus.lib

package object math {
  def max[A](objs: A*)(implicit cmp: Ordering[A]): A = objs.max
  def min[A](objs: A*)(implicit cmp: Ordering[A]): A = objs.min

  def floor(arg: Double) = scala.math.floor(arg).asInstanceOf[Int]
}


