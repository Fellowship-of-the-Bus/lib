package com.github.fellowship_of_the_bus.lib

import java.util.Scanner

package object util {
  import scala.util.Random
  private val random = new Random

  /** random number in range [0, i) */
  def rand(i: Int) = random.nextInt(i)

  /** random number in range [lower, upper] */
  def rand(lower: Int, upper: Int) = {
    require(lower <= upper)
    random.nextInt(upper-lower+1) + lower
  }

  def scanFile(filename: String): Scanner = {
    val stream = Native.getClass.getClassLoader().getResourceAsStream(filename)
    new Scanner(stream)
  }
}
