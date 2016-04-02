package com.github.fellowship_of_the_bus.lib
package game

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith


/* Test suite for lib/Game.scala */
@RunWith(classOf[JUnitRunner])
class GameTest extends LibTest {

  /* Since Game is a trait, we need a test class that uses it */
  class TestClassForGame extends Game {
    // Getter for the protected "isGameOver" field
    def getGameOver: Boolean = isGameOver
  }

  // TEST //
  test("A new game, by default, should not be over") {
    Given("a brand new game")
    val testClass = new TestClassForGame
    val isGameOver = testClass.getGameOver

    Then("the game should not be over yet")
    isGameOver should be (false) withClue ("// i.e. Game should not be over, but isGameOver is " + isGameOver + " //")
  }

  // TEST //
  test("A game that is not over can be ended") {
    Given("a game in progress")
    val testClass = new TestClassForGame
    var isGameOver = testClass.getGameOver //////////// make f'n?
    assert(isGameOver == false)

    When("Game Over occurs")
    testClass.gameOver()

    Then("the game is over")
    isGameOver = testClass.getGameOver
    isGameOver should be (true) withClue ("// i.e. Game should be over, but isGameOver is " + isGameOver + " //") //////// f'n?
  }

  // TEST //
  test("Game Over has no effect on a game that is already over") (pending)
}
