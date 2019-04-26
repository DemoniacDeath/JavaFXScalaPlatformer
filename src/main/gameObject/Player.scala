package main.gameObject

import javafx.scene.input.KeyCode
import main._

class Player(
              context: GameContext,
              frame: Rect,
              private val idleAnimation: Animation,
              private val jumpAnimation: Animation,
              private val crouchAnimation: Animation,
              private val crouchMoveAnimation: Animation,
              private val moveAnimation: Animation
            ) extends GameObject(context, frame) {
  private val originalSize: Size = frame.size.copy()
  var speed: Double = 0.0
  var jumpSpeed: Double = 0.0
  var power: Int = 0
  var maxPower: Int = 0
  var jumped: Boolean = false
  var health: Int = 100
  var dead: Boolean = false
  var crouched: Boolean = false

  private var _won: Boolean = false
  def won: Boolean = _won
  def won_=(value: Boolean): Unit = {
    if (value && !won) {
      _won = true
      frame.center.y += originalSize.height / 4
      frame.size.height = originalSize.height / 2
    } else if (!value && won) {
      _won = false
      frame.center.y -= originalSize.height / 4
      frame.size.height = originalSize.height
    }
  }

  physics = Some(new PhysicsState(this))
  physics.get.gravity = true
  physics.get.still = false

  override def keyDown(key: KeyCode) {
    if (key == KeyCode.G && physics.nonEmpty) {
      physics.get.gravity = !physics.get.gravity
      if (physics.get.gravity) {
        jumped = true
        physics.get.velocity = Vector()
      }
    }
    super.keyDown(key)
  }

  override def handleKeyboardState(keys: Set[KeyCode]) {
    if (!dead) {
      var sitDown = false
      var moveLeft = false
      var moveRight = false
      val moveVector = Vector()
      if (keys.contains(KeyCode.LEFT) ||
        keys.contains(KeyCode.A)) {
        moveVector.x -= speed
        moveLeft = true
      }
      if (keys.contains(KeyCode.RIGHT) ||
        keys.contains(KeyCode.D)) {
        moveVector.x += speed
        moveRight = true
      }
      if (keys.contains(KeyCode.UP) ||
        keys.contains(KeyCode.W) ||
        keys.contains(KeyCode.SPACE)) {
        if (physics.nonEmpty && !physics.get.gravity) {
          moveVector.y -= speed
        }
        else if (physics.nonEmpty && !jumped) {
          physics.get.velocity.y = physics.get.velocity.y - jumpSpeed
          jumped = true
        }
      }
      if (keys.contains(KeyCode.DOWN) ||
        keys.contains(KeyCode.S) ||
        keys.contains(KeyCode.CONTROL)) {
        if (physics.nonEmpty && !physics.get.gravity) {
          moveVector.y += speed
        }
        else {
          sitDown = true
        }
      }
      crouched = sitDown

      if (moveLeft && !moveRight) {
        moveAnimation.turnedLeft = true
        crouchAnimation.turnedLeft = true
        crouchMoveAnimation.turnedLeft = true
      }
      if (moveRight && !moveLeft) {
        moveAnimation.turnedLeft = false
        crouchAnimation.turnedLeft = false
        crouchMoveAnimation.turnedLeft = false
      }
      if (!moveLeft && !moveRight && !jumped && !crouched) animation = Some(idleAnimation)
      else if (!moveLeft && !moveRight && !jumped && crouched) animation = Some(crouchAnimation)
      else if ((moveLeft || moveRight) && !jumped && !crouched) animation = Some(moveAnimation)
      else if ((moveLeft || moveRight) && !jumped && crouched) animation = Some(crouchMoveAnimation)
      else if (jumped && crouched) animation = Some(crouchAnimation)
      else if (jumped && !crouched) animation = Some(jumpAnimation)
      frame.center += moveVector
    }
    super.handleKeyboardState(keys)
  }

  override def handleEnterCollision(collision: Collision) {
    val consumable = collision.collider
    if (consumable.isInstanceOf[Consumable]) {
      power += 1
      context.ui.powerBar.value = power.toDouble / maxPower.toDouble * 100.0
      consumable.removed = true
      speed += 0.01
      jumpSpeed += 0.01
      if (power >= maxPower) {
        win()
      }
    }
  }

  private def win() {
    won = true
    context.ui.winText.visible = true
  }

  override def handleExitCollision(collider: GameObject) {
    if (physics.nonEmpty && physics.get.colliders.isEmpty) {
      jumped = true
    }
  }

  override def handleCollision(collision: Collision) {
    if (Math.abs(collision.collisionVector.x) > Math.abs(collision.collisionVector.y)) {
      if (collision.collisionVector.y > 0 && jumped && physics.nonEmpty && physics.get.gravity) {
        jumped = false
      }
    }
  }

  def dealDamage(damage: Int) {
    if (!won) {
      health -= damage
      context.ui.healthBar.value = health.toDouble
      if (health < 0) {
        die()
      }
    }
  }

  private def die() {
    dead = true
    context.ui.deathText.visible = true
  }


}
