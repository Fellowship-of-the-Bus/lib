package com.github.fellowship_of_the_bus.lib.ui
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
  def setRotation(ang: Float) = ()
  def setCenterOfRotation(x: Float, y: Float) = ()
  def update(delta: Long) = ()
  def copy(): Drawable
  def start() = ()
  def stop() = ()

  def setAutoUpdate(b: Boolean) = ()
  def setLooping(b: Boolean) = ()
  def setCurrentFrame(index: Int) = ()
  def setDuration(index: Int, duration: Int) = ()
  def stopAt(frameIndex: Int) = ()

  def reinit(): Unit = ()
  def setImageColor(r: Float, g: Float, b: Float): Unit = ()
}

object Image {
  def apply(str: String, scaleFactor: Float) = {
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

  def draw(x: Float, y: Float, flipX: Boolean = false, flipY: Boolean = false) = {
    val img = this.img.getFlippedCopy(flipX, flipY)
    val (cx, cy) = centerOfRotation
    img.setCenterOfRotation(cx, cy)
    img.setRotation(rotation)
    img.draw(x, y, scaleFactor)
  }

  override def draw(x: Float, y: Float, width: Float, height: Float) = {
    val (cx, cy) = centerOfRotation
    img.setCenterOfRotation(cx, cy)
    img.setRotation(rotation)
    img.draw(x/scaleFactor, y/scaleFactor, width, height) // what?
  }

  override def setRotation(ang: Float) = rotation = ang
  override def setCenterOfRotation(x: Float, y: Float) = centerOfRotation = (x/scaleFactor,y/scaleFactor)
  def getWidth: Float = img.getWidth * scaleFactor
  def getHeight: Float = img.getHeight * scaleFactor


  def copy() = {
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
  def draw(x: Float, y: Float, flipX: Boolean = false, flipY: Boolean = false) = anim.draw(x/scaleFactor, y/scaleFactor)
  def getWidth: Float = anim.getWidth * scaleFactor
  def getHeight: Float = anim.getHeight * scaleFactor
  override def update(delta: Long) = {
    anim.update(delta)
  }

  def copy() = {
    val cpy = new Animation(anim.copy)
    cpy.scaleFactor = scaleFactor
    cpy
  }

  override def start() = anim.start
  override def stop() = anim.stop

  override def setAutoUpdate(b: Boolean) = anim.setAutoUpdate(b)
  override def setLooping(b: Boolean) = anim.setLooping(b)
  override def setCurrentFrame(index: Int) = anim.setCurrentFrame(index)
  override def setDuration(index: Int, duration: Int) = anim.setDuration(index, duration)
  override def stopAt(frameIndex: Int) = anim.stopAt(frameIndex)

}
