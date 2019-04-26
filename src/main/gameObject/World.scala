package main.gameObject

import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import main.{GameContext, GameObject, Rect}

class World(context: GameContext, frame: Rect) extends GameObject(context, frame) {
  val camera = new Camera(context, frame)

  def render(graphics: GraphicsContext) {
    super.render(graphics, frame.center, camera.globalPosition(), camera.frame.size)
  }

  override def keyDown(key: KeyCode) {
    super.keyDown(key)
    if (key == KeyCode.Q) context.quit = true
  }
}