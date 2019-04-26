package main

import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode

import scala.collection.mutable.ListBuffer

abstract class GameObject(
                           val context: GameContext,
                           private var _frame: Rect
                         ) {
  private val children: ListBuffer[GameObject] = new ListBuffer[GameObject]

  var parent: Option[GameObject] = None
  var renderObject: Option[RenderObject] = None
  var physics: Option[PhysicsState] = None
  var animation: Option[Animation] = None
  var visible: Boolean = true
  var removed: Boolean = false

  def frame: Rect = _frame

  def frame_=(f: Rect): Unit = _frame = f

  def handleKeyboardState(keys: Set[KeyCode]): Unit = {
    for (child <- children) child.handleKeyboardState(keys)
  }

  def keyDown(key: KeyCode): Unit = {
    for (child <- children) child.keyDown(key)
  }

  def handleEnterCollision(collision: Collision): Unit = {}

  def handleExitCollision(collider: GameObject): Unit = {}

  def handleCollision(collision: Collision): Unit = {}

  def animate(now: Long): Unit = {
    if (animation.nonEmpty) {
      renderObject = Some(animation.get.animate(now))
    }
    for (child <- children) child.animate(now)
  }

  def render(
              graphics: GraphicsContext,
              localBasis: Vector,
              cameraPosition: Vector,
              cameraSize: Size
            ) {
    if (visible && renderObject.nonEmpty) {
      renderObject.get.render(
        graphics,
        context,
        frame.center + localBasis,
        frame.size,
        cameraPosition,
        cameraSize
      )
    }
    for (child <- children) child.render(
      graphics,
      frame.center + localBasis,
      cameraPosition,
      cameraSize
    )
  }

  def processPhysics() {
    physics.foreach(p => p.change())
    for (child <- children) child.processPhysics()
  }

  def detectCollisions() {
    val allColliders = new ListBuffer[GameObject]()
    detectCollisions(allColliders)
    val size = allColliders.size
    for (i <- 0 until size) {
      for (j <- i + 1 until size) {
        for {
          p1 <- allColliders(i).physics
          p2 <- allColliders(j).physics
        } p1.detectCollisions(p2)
      }
    }
  }

  private def detectCollisions(allColliders: ListBuffer[GameObject]) {
    if (physics != null) allColliders += this

    for (child <- children) child.detectCollisions(allColliders)
  }

  def addChild(child: GameObject) {
    children += child
    child.parent = Some(this)
  }

  def clean() {
    for (child <- children) child.physics.foreach(p => p.clean())
    for (child <- children) {
      if (child.removed) {
        children -= child
      }
    }
  }

  def globalPosition(): Vector = {
    frame.center + parent.map(p => p.globalPosition()).getOrElse(Vector())
  }

  def onWindowResize(newSize: Size) {
    for (child <- children) child.onWindowResize(newSize)
  }
}
