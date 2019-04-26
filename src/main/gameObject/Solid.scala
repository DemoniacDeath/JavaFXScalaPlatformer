package main.gameObject

import main._

class Solid(context: GameContext, frame: Rect) extends GameObject(context, frame) {
  physics = Some(new PhysicsState(this))
}
