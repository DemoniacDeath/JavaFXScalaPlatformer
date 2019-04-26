package main.gameObject

import javafx.scene.input.KeyCode
import main.{GameContext, GameObject, Rect, Size}

class Camera(context: GameContext, frame: Rect) extends GameObject(context, frame) {

  var originalSize = Size(frame.size.width / 2, frame.size.height / 2)

  override def handleKeyboardState(keys: Set[KeyCode]) {
    frame.size = if (keys.contains(KeyCode.Z)) {
      Size(originalSize.width * 2, originalSize.height * 2)
    } else {
      originalSize
    }
  }

  override def onWindowResize(newSize: Size) {
    val size = Size(newSize.width / 2, newSize.height / 2)
    frame.size = size
    originalSize = Size(frame.size.width / 2, frame.size.height / 2)
    super.onWindowResize(newSize)
  }
}
