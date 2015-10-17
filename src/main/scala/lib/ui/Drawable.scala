package com.github.fellowship_of_the_bus.lib.ui
import org.newdawn.slick.{Animation => SlickAnimation, Image => SlickImage, Graphics}

/** Something that can be drawn directly to the screen.
  * Primary purpose is to unite org.newdawn.slick.Image 
  * and org.newdawn.slick.Animation into a single hierarchy */
trait Drawable {
  def draw(x: Float, y: Float): Unit
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

/** wrapper class for Images */
case class Image(str: String) extends Drawable {
  val img = new SlickImage(str)
  private var centerOfRotation = (0.0f, 0.0f)
  private var rotation = 0.0f

  def draw(x: Float, y: Float) = {
    val (cx, cy) = centerOfRotation
    img.setCenterOfRotation(cx, cy)
    img.setRotation(rotation)
    img.draw(x, y)
  }

  override def draw(x: Float, y: Float, width: Float, height: Float) = {
    val (cx, cy) = centerOfRotation
    img.setCenterOfRotation(cx, cy)
    img.setRotation(rotation)
    img.draw(x, y, width, height)
  }

  override def setRotation(ang: Float) = rotation = ang
  override def setCenterOfRotation(x: Float, y: Float) = centerOfRotation = (x,y)
  def getWidth: Float = img.getHeight
  def getHeight: Float = img.getHeight


  def copy() = new Image(str)

  override def setImageColor(r: Float, g: Float, b: Float): Unit = 
    img.setImageColor(r,g,b)
}

object Animation {
  // def apply(strs: Array[String]): Animation = Animation(strs.map(new SlickImage(_)))
  def apply(strs: Array[String]): Animation = {
    Animation(new SlickAnimation(strs.map(new SlickImage(_)), 1000/strs.length, false))
  }
}

/** wrapper class for animations */
case class Animation(anim: SlickAnimation) extends Drawable {
  // anim.start
  // val duration = 1000 / imgs.length
  // val anim = new SlickAnimation(imgs, duration, false)
// imgs: Array[SlickImage]
  def draw(x: Float, y: Float) = anim.draw(x, y)
  def getWidth: Float = anim.getWidth
  def getHeight: Float = anim.getHeight
  override def update(delta: Long) = {
    anim.update(delta)
  }

  def copy() = new Animation(anim.copy)

  override def start() = anim.start
  override def stop() = anim.stop

  override def setAutoUpdate(b: Boolean) = anim.setAutoUpdate(b)
  override def setLooping(b: Boolean) = anim.setLooping(b)
  override def setCurrentFrame(index: Int) = anim.setCurrentFrame(index)
  override def setDuration(index: Int, duration: Int) = anim.setDuration(index, duration)
  override def stopAt(frameIndex: Int) = anim.stopAt(frameIndex)

}
