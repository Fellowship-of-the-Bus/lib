package com.github.fellowship_of_the_bus.lib.slick2d.ui
import org.newdawn.slick.{Animation => SlickAnimation, Image => SlickImage, Graphics, Color}

/** Something that can be drawn directly to the screen.
  * Primary purpose is to unite org.newdawn.slick.Image
  * and org.newdawn.slick.Animation into a single hierarchy */
trait Drawable {
  var scaleFactor: Float = 1.0f
  var centerOfRotation = (0.0f, 0.0f)
  var rotation = 0.0f
  var rgba = (1f, 1f, 1f, 1f)

  def draw(x: Float, y: Float, flipX: Boolean = false, flipY: Boolean = false, filter: XColor = NoColor): Unit
  @deprecated("Width and height aren't properly taken into account. Set a scale factor instead", "0.2")
  def draw(x: Float, y: Float, width: Float, height: Float): Unit = draw(x, y)

  def getWidth: Float
  def getHeight: Float
  def width: Float
  def height: Float
  def setRotation(ang: Float): Unit = rotation = ang
  def setCenterOfRotation(x: Float, y: Float): Unit =
    centerOfRotation = (x,y)
  def update(delta: Long): Unit = ()
  def copy(): Drawable
  def start(): Unit = ()
  def stop(): Unit = ()

  def setAutoUpdate(b: Boolean): Unit = ()
  def setLooping(b: Boolean): Unit = ()
  def setCurrentFrame(index: Int): Unit = ()
  def setDuration(index: Int, duration: Int): Unit = ()
  def stopAt(frameIndex: Int): Unit = ()

  def reinit(): Unit = ()
  def setImageColor(r: Float, g: Float, b: Float): Unit = setImageColor(r, g, b, 1)
  def setImageColor(r: Float, g: Float, b: Float, a: Float): Unit = rgba = (r, g, b, a)
}

object Image {
  def apply(str: String, scaleFactor: Float): Image = {
    val im = new Image(str)
    im.scaleFactor = scaleFactor
    im
  }
}

/** wrapper class for Images */
case class Image(str: String) extends Drawable {
  val img = new SlickImage(str)

  def draw(x: Float, y: Float, flipX: Boolean = false, flipY: Boolean = false, filter: XColor = NoColor): Unit = {
    val img = this.img.getFlippedCopy(flipX, flipY)
    val (r, g, b, a) = rgba
    val (cx, cy) = centerOfRotation
    img.setCenterOfRotation(cx, cy)
    img.setRotation(rotation)
    if (filter == NoColor) {
      img.draw(x, y, scaleFactor, new Color(r, g, b, a))
    } else {
      img.draw(x, y, scaleFactor, filter.color)
    }
  }

  // override def draw(x: Float, y: Float, width: Float, height: Float): Unit = {
  //   val (cx, cy) = centerOfRotation
  //   img.setCenterOfRotation(cx, cy)
  //   img.setRotation(rotation)
  //   img.draw(x/scaleFactor, y/scaleFactor, width, height) // what?
  // }

  def width: Float = img.getWidth * scaleFactor
  def height: Float = img.getHeight * scaleFactor

  def getWidth: Float = width
  def getHeight: Float = height

  def copy(): Image = {
    val img = new Image(str)
    img.scaleFactor = scaleFactor
    img
  }
}

object Animation {
  // def apply(strs: Array[String]): Animation = Animation(strs.map(new SlickImage(_)))
  def apply(strs: Array[String], scaleFactor: Float = 1.0f, animationMS: Int = 1000): Animation = {
    val anim = Animation(new SlickAnimation(strs.map(new SlickImage(_)), 1000/strs.length, false))
    anim.scaleFactor = scaleFactor
    anim
  }
}

/** wrapper class for animations */
case class Animation(anim: SlickAnimation) extends Drawable {
  // anim.start
  // val duration = 1000 / imgs.length
  // val anim = new SlickAnimation(imgs, duration, false)
// imgs: Array[SlickImage]

  // fix so that draw does the scaling
  def draw(x: Float, y: Float, flipX: Boolean = false, flipY: Boolean = false, filter: XColor = NoColor): Unit = {
    val img = anim.getCurrentFrame.getFlippedCopy(flipX, flipY)
    val (cx, cy) = centerOfRotation
    img.setCenterOfRotation(cx, cy)
    img.setRotation(rotation)
    if (filter == NoColor) {
      img.draw(x, y, scaleFactor)
    } else {
      img.draw(x, y, scaleFactor, filter.color)
    }

    // anim.draw(x, y, getWidth*scaleFactor, getHeight*scaleFactor)
  }

  def width = anim.getWidth * scaleFactor
  def height = anim.getHeight * scaleFactor

  // @deprecated("Use width instead")
  def getWidth: Float = width
  // @deprecated("Use height instead")
  def getHeight: Float = height
  override def update(delta: Long): Unit = {
    anim.update(delta)
  }

  def copy(): Animation = {
    val cpy = new Animation(anim.copy)
    cpy.scaleFactor = scaleFactor
    cpy
  }

  override def start(): Unit = anim.start
  override def stop(): Unit = anim.stop

  override def setAutoUpdate(b: Boolean): Unit = anim.setAutoUpdate(b)
  override def setLooping(b: Boolean): Unit = anim.setLooping(b)
  override def setCurrentFrame(index: Int): Unit = anim.setCurrentFrame(index)
  override def setDuration(index: Int, duration: Int): Unit = anim.setDuration(index, duration)
  override def stopAt(frameIndex: Int): Unit = anim.stopAt(frameIndex)
}
