package main

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.{Image, WritableImage}
import javafx.scene.paint.Color

class RenderObject(
                    val texture: Image
                  ) {
  var flipped: Boolean = false

  def render(
              graphics: GraphicsContext,
              context: GameContext,
              position: Vector,
              size: Size,
              cameraPosition: Vector,
              cameraSize: Size
            ): Unit = {
    val renderPosition = position +
      Vector(-size.width / 2, -size.height / 2) -
      cameraPosition -
      Vector(-cameraSize.width / 2, -cameraSize.height / 2)
    graphics.drawImage(
      texture,
      0.0,
      0.0,
      texture.getWidth,
      texture.getHeight,
      Math.round(
        context.windowSize.width *
          ((renderPosition.x + (if (flipped) size.width else 0.0)) / cameraSize.width)).toDouble
      ,
      Math.round(
        context.windowSize.height *
          (renderPosition.y / cameraSize.height)).toDouble
      ,
      Math.round(
        context.windowSize.width *
          (size.width / cameraSize.width)).toDouble * (if (flipped) -1.0 else 1.0)
      ,
      Math.round(
        context.windowSize.height *
          (size.height / cameraSize.height)).toDouble
    )
  }
}

object RenderObject {
  def fromColor(color: Color): RenderObject = {
    val wi = new WritableImage(1, 1)
    wi.getPixelWriter.setColor(0, 0, color)
    new RenderObject(wi)
  }
}