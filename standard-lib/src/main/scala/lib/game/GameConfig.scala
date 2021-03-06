package com.github.fellowship_of_the_bus.lib
package game

object DefaultGameConfig {
  val FrameRate = 60
}

/** Holds global game configuration. */
object GameConfig {
  /** screen width */
  var Width = 0
  /** screen height */
  var Height = 0

  /** target frame rate */
  var FrameRate = DefaultGameConfig.FrameRate

  /** true if life bars should be displayed */
  var showLifebars = true

  val Windows = 0
  val MacOS = 1
  val Linux = 2
  val Other = 3

  val OS = System.getProperty("os.name").split(" ")(0).toLowerCase match {
    case "windows" => Windows
    case "mac" => MacOS
    case "linux" => Linux
    case _ => Other
  }
}
