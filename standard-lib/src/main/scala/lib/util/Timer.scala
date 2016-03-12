package com.github.fellowship_of_the_bus.lib
package util

import math.min

/**
 * TimerManagers can register Timer events
 */
trait TimerManager {
  private[util] var timers = Vector[Timer]()

  /** Registers a timer event with this TimerListener. */
  @deprecated("Use TimerManager.add instead", "0.3")
  def addTimer(timer: Timer): Unit = add(timer)

  /** Registers a timer event with this TimerListener. */
  def add(timer: Timer): Unit = {
    timers = timers :+ timer
  }

  /** ticks all registered timers */
  def tick(delta: Long): Unit

  /** ticks all registered timers */
  @deprecated("Use TimerManager.tick instead", "0.3")
  def update(delta: Long): Unit = tick(delta)

  /** Unregister all timer events */
  def cancelAll(): Unit = timers = Vector[Timer]()

  /** Returns true if there are active timer events */
  def ticking(): Boolean = timers.exists(_.canFire)
}

trait TimerListener extends TimerManager {
  /** ticks all registered timers and causes them to
    * fire if they become ready */
  def tick(delta: Long): Unit = {
    for (timer <- timers) {
      timer.tick(delta)
      timer.fire()
    }
    timers = timers.filter(_.canFire)
  }
}

/** A TimerManager that does not automatically call
  * fire on ready timers. To fire the ready timers,
  * the user should call fire on the ManualTimerManager
  * object. */
trait ManualTimerManager extends TimerManager {
  def tick(delta: Long): Unit = {
    for (timer <- timers) {
      timer.tick(delta)
    }
  }

  /** fires all ready timers */
  def fire(): Unit = {
    for (timer <- timers) {
      timer.fire()
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

abstract class Timer(timeTillAction: Long, protected val action: () => Unit,
    frequency: TimerFrequency = FireOnce) {

  private var repeat = frequency

  private var timer: Long = 0
  /** decreases the amount of time remaining until the timer can fire */
  def tick(tickAmt: Long): Unit = {
    timer = min(timer + tickAmt, timeTillAction)
  }

  /** performs the timer action if the timer is ready, and resets
    * internal state for future ticking/firing */
  def fire(): Unit = {
    if (ready) {
      action()
      timer = 0
      updateRepeat
    }
  }

  /** updates the number of times this timer can fire again.
    * Called when a timer successfully fires. */
  protected def updateRepeat() = repeat = repeat match {
    case RepeatForever => RepeatForever
    case FireOnce | FireN(1) => Finished
    case FireN(n) => FireN(n-1)
    case Finished => Finished
  }

  /** The amount that the timer has to be ticked before the action fires */
  def timeRemaining(): Long = timeTillAction - timer

  /** true if this timer is ready to fire */
  def ready(): Boolean = canFire && timeTillAction == timer

  /** true if this Timer can fire again at some point in the future */
  def canFire(): Boolean = repeat match {
    case Finished => false
    case _ => true
  }
}

/** Timer which requires n calls to tick before it can fire */
class TickTimer(timeTillAction: Long, action: () => Unit, frequency: TimerFrequency = FireOnce)
extends Timer(timeTillAction, action, frequency) {
  /** ignores delta and always ticks 1 */
  override def tick(delta: Long): Unit = {
     super.tick(1)
  }
}

/** Timer which ticks by the received amount every time */
class MSTimer(timeTillAction: Long, action: () => Unit, frequency: TimerFrequency = FireOnce)
extends Timer(timeTillAction, action, frequency)


trait ConditionalTimer extends Timer {
  protected val query: () => Boolean

  override def ready(): Boolean = super.ready() && query()
}

/** Timer which requires n calls to tick before it can fire and waits
  * to perform its action until its query function returns true */
class ConditionalTickTimer(timeTillAction: Long, action: () => Unit,
    protected val query: () => Boolean, frequency: TimerFrequency = FireOnce)
extends TickTimer(timeTillAction, action, frequency) with ConditionalTimer

/** Timer which ticks by the received amount every time
  * and waits to perform its action until its query function returns true */
class ConditionalMSTimer(timeTillAction: Long, action: () => Unit,
    protected val query: () => Boolean, frequency: TimerFrequency = FireOnce)
extends MSTimer(timeTillAction, action, frequency) with ConditionalTimer
