package com.github.fellowship_of_the_bus.lib.slick2d.ui
import org.newdawn.slick.{Animation => SlickAnimation, Image => SlickImage, Graphics}

/** Something that can be drawn directly to the screen.
  * Primary purpose is to unite org.newdawn.slick.Image
  * and org.newdawn.slick.Animation into a single hierarchy */
trait Drawable {
  var scaleFactor: Float = 1.0f

  def draw(x: Float, y: Float, flipX: Boolean = false, flipY: Boolean = false): Unit
  def draw(x: Float, y: Float, width: Float, height: Float): Unit = draw(x, y)

  def getWidth: Float
  def getHeight: Float
  def setRotation(ang: Float): Unit = ()
  def setCenterOfRotation(x: Float, y: Float): Unit = ()
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
  def setImageColor(r: Float, g: Float, b: Float): Unit = ()
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
  private var centerOfRotation = (0.0f, 0.0f)
  private var rotation = 0.0f

  def draw(x: Float, y: Float, flipX: Boolean = false, flipY: Boolean = false): Unit = {
    val img = this.img.getFlippedCopy(flipX, flipY)
    val (cx, cy) = centerOfRotation
    img.setCenterOfRotation(cx, cy)
    img.setRotation(rotation)
    img.draw(x, y, scaleFactor)
  }

  override def draw(x: Float, y: Float, width: Float, height: Float): Unit = {
    val (cx, cy) = centerOfRotation
    img.setCenterOfRotation(cx, cy)
    img.setRotation(rotation)
    img.draw(x/scaleFactor, y/scaleFactor, width, height) // what?
  }

  override def setRotation(ang: Float): Unit = rotation = ang
  override def setCenterOfRotation(x: Float, y: Float): Unit =
    centerOfRotation = (x/scaleFactor,y/scaleFactor)
  def getWidth: Float = img.getWidth * scaleFactor
  def getHeight: Float = img.getHeight * scaleFactor


  def copy(): Image = {
    val img = new Image(str)
    img.scaleFactor = scaleFactor
    img
  }

  override def setImageColor(r: Float, g: Float, b: Float): Unit =
    img.setImageColor(r,g,b)
}

object Animation {
  // def apply(strs: Array[String]): Animation = Animation(strs.map(new SlickImage(_)))
  def apply(strs: Array[String], scaleFactor: Float = 1.0f): Animation = {
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
  def draw(x: Float, y: Float, flipX: Boolean = false, flipY: Boolean = false): Unit =
    anim.draw(x/scaleFactor, y/scaleFactor)
  def getWidth: Float = anim.getWidth * scaleFactor
  def getHeight: Float = anim.getHeight * scaleFactor
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
