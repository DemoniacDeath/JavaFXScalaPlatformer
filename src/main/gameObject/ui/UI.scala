package main.gameObject.ui

import javafx.scene.canvas.GraphicsContext
import main.{GameContext, Rect, Size, Vector}

class UI(context: GameContext, frame: Rect) extends Element(context, frame) {
  val healthBarHolder = new Element(context, new Rect(-frame.size.width / 2 + 16, -frame.size.height / 2 + 2.5, 30.0, 3.0))
  val healthBar = new Bar(context, new Rect(-frame.size.width / 2 + 16, -frame.size.height / 2 + 2.5, 29.0, 2.0))
  val powerBarHolder = new Element(context, new Rect(frame.size.width / 2 - 16, -frame.size.height / 2 + 2.5, 30.0, 3.0))
  val powerBar = new Bar(context, new Rect(frame.size.width / 2 - 16, -frame.size.height / 2 + 2.5, 29.0, 2.0))
  val deathText = new Text(context, new Rect(0.0, 0.0, 100.0, 15.0))
  val winText = new Text(context, new Rect(0.0, 0.0, 100.0, 15.0))

  addChild(healthBarHolder)
  addChild(healthBar)
  addChild(powerBarHolder)
  addChild(powerBar)
  addChild(deathText)
  addChild(winText)

  def render(graphics: GraphicsContext) {
    super.render(
      graphics,
      frame.center,
      Vector(),
      context.world.camera.originalSize
    )
  }

  override def onWindowResize(newSize: Size) {
    frame.size = Size(newSize.width / 4, newSize.height / 4)
    healthBarHolder.frame = new Rect(-frame.size.width / 2 + 16, -frame.size.height / 2 + 2.5, 30.0, 3.0)
    healthBar.frame = new Rect(-frame.size.width / 2 + 16, -frame.size.height / 2 + 2.5, 29.0, 2.0)
    powerBarHolder.frame = new Rect(frame.size.width / 2 - 16, -frame.size.height / 2 + 2.5, 30.0, 3.0)
    powerBar.frame = new Rect(frame.size.width / 2 - 16, -frame.size.height / 2 + 2.5, 29.0, 2.0)
    super.onWindowResize(newSize)
  }
}
