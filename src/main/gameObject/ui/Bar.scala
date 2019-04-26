package main.gameObject.ui

import main.{GameContext, Rect}

class Bar(context: GameContext, frame: Rect) extends Element(context, frame) {
  private var originalFrame = new Rect(center = super.frame.center.copy(), size = super.frame.size.copy())
  private var _value = 100.0

  def value: Double = _value

  def value_=(v: Double): Unit = {
    if (v > 100) _value = 100.0
    else if (v < 0) _value = 0.0
    else _value = v

    redrawValue()
  }

  override def frame_=(f: Rect): Unit = {
    super.frame_=(f)
    originalFrame = new Rect(f.center.copy(), f.size.copy())
    redrawValue()
  }

  private def redrawValue() {
    super.frame.center.x = originalFrame.center.x + originalFrame.size.width * ((value - 100) / 200)
    super.frame.size.width = originalFrame.size.width / 100 * value
  }
}