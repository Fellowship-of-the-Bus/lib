package com.github.fellowship_of_the_bus.lib.util

/**
 * TimerListeners can register Timer events
 */
trait TimerListener {
  private var timers = Vector[Timer]()

  /**
   * Registers a timer event
   */
  def addTimer(timer: Timer) = {
    timers = timers :+ timer
  }

  /**
   * ticks all registered timers
   */
  def update(delta: Long) = {
    for (timer <- timers) {
      timer.tick(delta)
    }
    timers = timers.filter(_.canFire)
  }
}

sealed trait TimerFrequency
case object Finished extends TimerFrequency
case object RepeatForever extends TimerFrequency
case object FireOnce extends TimerFrequency
case class FireN(n: Long) extends TimerFrequency {
  require(n >= 1)
}

abstract class Timer(timeTillAction: Long, action: () => Unit, frequency: TimerFrequency = FireOnce) {
  var repeat = frequency

  private var timer: Long = 0
  def tick(tickAmt: Long): Unit = {
    timer = timer + tickAmt
    if (canFire && timer >= timeTillAction) {
      action()
      timer = 0
      updateRepeat
    }
  }

  protected def updateRepeat() = repeat = repeat match {
    case RepeatForever => RepeatForever
    case FireOnce | FireN(1) => Finished
    case FireN(n) => FireN(n-1)
    case Finished => Finished
  }

  /** The amount that the timer has to be ticked before the action fires */
  def timeRemaining(): Long = timeTillAction - timer

  /** true if this Timer can fire */
  def canFire() = repeat match {
    case Finished => false
    case _ => true
  }
}

/**
 * Timer which requires n calls to tick to fire
 */
class TickTimer(timeTillAction: Long, action: () => Unit, frequency: TimerFrequency = FireOnce) 
extends Timer(timeTillAction, action, frequency) {
  /** ignores delta and always ticks 1 */
  override def tick(delta: Long): Unit = {
     super.tick(1)
  }
}

/**
 * Timer which ticks by the received amount avery time 
 */
class MSTimer(timeTillAction: Long, action: () => Unit, frequency: TimerFrequency = FireOnce) 
extends Timer(timeTillAction, action, frequency)
