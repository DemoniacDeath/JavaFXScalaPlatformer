package main.gameObject

import main._

class Solid(context: GameContext, frame: Rect) extends GameObject(context, frame) {
  physics = Some(new PhysicsState(this))

  override def handleEnterCollision(collision: Collision) {
    val collider = collision.collider
    collider match {
      case player: Player =>
        if (collision.collider.physics.nonEmpty && collision.collider.physics.get.velocity.y > 5) {
          player.dealDamage(Math.round(collision.collider.physics.get.velocity.y * 10).toInt)
        }
    }
  }

  override def handleCollision(collision: Collision) {
    if (Math.abs(collision.collisionVector.x) < Math.abs(collision.collisionVector.y)) {
      collision.collider.frame.center.x += collision.collisionVector.x
      collision.collider.physics.foreach(p => p.velocity.x = 0.0)
    } else {
      collision.collider.frame.center.y += collision.collisionVector.y
      collision.collider.physics.foreach(p => p.velocity.y = 0.0)
    }
  }
}
