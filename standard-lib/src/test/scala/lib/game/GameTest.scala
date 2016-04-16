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

  /* For convenience, method to get a game test class in the proper state */
  def newTestGame(isOver: Boolean): TestClassForGame = {
    val testGame = new TestClassForGame

    if (isOver) {
      testGame.gameOver()
    }

    assert(testGame.getGameOver == isOver, "// i.e. Sanity check for test game object failed")  // should never actually fail
    testGame   // return the game test object
  }

  /* Reusable clue text for "game over" Boolean assertions */
  def gameOverClueText(expectedOver: Boolean, actuallyOver: Boolean): String = {
    val gameOverFieldName = "isGameOver"
    var text = "// i.e. Game should "

    if (! expectedOver) {
      text += "not "
    }

    // build and return the text string
    text + "be over, but " + gameOverFieldName + " is " + actuallyOver + " //"
  }

  // TEST //
  test("A new game, by default, should not be over") {
    Given("a brand new game")
    val testGame = newTestGame(false)

    Then("the game should not be over yet")
    val isGameOver = testGame.getGameOver
    isGameOver should be (false) withClue (gameOverClueText(false, isGameOver))
  }

  // TEST //
  test("A game that is not over can be ended") {
    Given("a game in progress")
    val testGame = newTestGame(false)

    When("Game Over occurs")
    testGame.gameOver()

    Then("the game is over")
    val isGameOver = testGame.getGameOver
    isGameOver should be (true) withClue (gameOverClueText(true, isGameOver))
  }

  // TEST //
  test("Game Over has no effect on a game that is already over") {
    Given("a game that is over")
    val testGame = newTestGame(true)

    When("Game Over occurs")
    Then("nothing breaks")
    noException should be thrownBy testGame.gameOver() withClue ("// i.e. Something broke")

    And("the game is still over")
    val isGameOver = testGame.getGameOver
    isGameOver should be (true) withClue (gameOverClueText(true, isGameOver))
  }
}
