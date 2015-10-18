package com.github.fellowship_of_the_bus.lib

package object util {
  import scala.util.Random
  private val random = new Random
  def rand(i: Int) = random.nextInt(i)
  def rand(lower: Int, upper: Int) = {
    require(lower <= upper)
    random.nextInt(upper-lower) + lower
  }
}
