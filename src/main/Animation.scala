package main

import javafx.scene.image.{Image, WritableImage}

import scala.collection.mutable

class Animation(
                 val speed: Int,
                 val frames: mutable.MutableList[RenderObject] = new mutable.MutableList[RenderObject]
               ) {
  var startTick: Long = 0L
  private var _turnedLeft = false

  def turnedLeft: Boolean = _turnedLeft

  def turnedLeft_=(value: Boolean): Unit = {
    _turnedLeft = value

    frames.foreach(frame => {
      frame.flipped = value
    })
  }

  def animate(ticks: Long): RenderObject = {
    if (startTick == 0L || ticks - startTick >= frames.size * speed * Animation.speedScale) startTick = ticks
    frames(((ticks - startTick).toFloat / (speed * Animation.speedScale).toFloat).toInt)
  }
}

object Animation {
  private val speedScale = 100000

  def withSingleRenderObject(renderObject: RenderObject) = new Animation(
    1, mutable.MutableList[RenderObject]() += renderObject
  )

  def withSpeedAndImage(speed: Int, image: Image, width: Int, height: Int, framesNumber: Int): Animation = {
    val animation = new Animation(speed)
    val reader = image.getPixelReader
    for (i <- 0 until framesNumber) {
      animation.frames += new RenderObject(new WritableImage(reader, 0, i * height, width, height))
    }
    animation
  }

}

