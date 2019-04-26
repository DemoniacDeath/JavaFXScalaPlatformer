package main.gameObject

import main.GameContext
import main.GameObject
import main.PhysicsState
import main.Rect

class Consumable(context: GameContext, frame: Rect) extends GameObject(context, frame) {
  physics = Some(new PhysicsState(this))
}
