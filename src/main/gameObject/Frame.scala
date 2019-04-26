package main.gameObject

import main.{GameContext, GameObject, Rect}

class Frame(context: GameContext, frame: Rect, val width: Double) extends GameObject(context, frame) {
  val floor: Solid = new Solid(context, new Rect(
    .0,
    frame.size.height / 2 - width / 2,
    frame.size.width,
    width
  ))
  val wallLeft: Solid = new Solid(context, new Rect(
    -frame.size.width / 2 + width / 2,
    .0,
    width,
    frame.size.height - width * 2
  ))
  val wallRight: Solid = new Solid(context, new Rect(
    frame.size.width / 2 - width / 2,
    .0,
    width,
    frame.size.height - width * 2
  ))
  val ceiling: Solid = new Solid(context, new Rect(
    .0,
    -frame.size.height / 2 + width / 2,
    frame.size.width,
    width
  ))

  addChild(floor)
  addChild(wallLeft)
  addChild(wallRight)
  addChild(ceiling)
}