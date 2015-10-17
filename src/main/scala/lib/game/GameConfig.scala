package com.github.fellowship_of_the_bus.lib
package game

import org.newdawn.slick.Graphics

/** Holds global game configuration. */
object GameConfig {
  /** screen width */
  var Width = 0
  /** screen height */
  var Height = 0

  /** target frame rate */
  var FrameRate = 60

  /** true if life bars should be displayed */
  var showLifebars = true

  var graphics: Graphics = null
}
