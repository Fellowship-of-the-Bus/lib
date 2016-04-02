package com.github.fellowship_of_the_bus.lib.test

import org.scalatest._

/* Base class for Library tests */
abstract class LibTest extends FunSuite with GivenWhenThen with Matchers { }
