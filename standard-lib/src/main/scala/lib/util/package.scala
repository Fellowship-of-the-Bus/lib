package com.github.fellowship_of_the_bus.lib

import java.util.Scanner

package object util {
  import scala.util.Random
  private val random = new Random

  /** random number in range [0, i) */
  def rand(i: Int): Int = random.nextInt(i)

  /** random number in range [lower, upper] */
  def rand(lower: Int, upper: Int): Int = {
    require(lower <= upper)
    random.nextInt(upper-lower+1) + lower
  }

  /** random element from sequence s */
  def rand[T](s: Seq[T]): T = s(rand(s.length))

  def scanFile(filename: String): Scanner = {
    new Scanner(openFileAsStream(filename))
  }

  def openFileAsStream(filename: String): java.io.InputStream = {
    Native.getClass.getClassLoader().getResourceAsStream(filename)
  }
}
