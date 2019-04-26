package main.gameObject

import main.{GameContext, GameObject, PhysicsState, Rect}

class Consumable(context: GameContext, frame: Rect) extends GameObject(context, frame) {
  physics = Some(new PhysicsState(this))
}
