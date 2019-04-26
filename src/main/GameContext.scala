package main

import main.gameObject.World
import main.gameObject.ui.UI

class GameContext(var windowSize: Size) {
  val world = new World(this, new Rect(0.0, 0.0, windowSize.width / 2, windowSize.height / 2))
  val ui = new UI(this, new Rect(0.0, 0.0, windowSize.width / 4, windowSize.height / 4))
  var quit: Boolean = false
}
