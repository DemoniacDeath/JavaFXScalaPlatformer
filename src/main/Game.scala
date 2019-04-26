package main

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.Font
import main.gameObject.{Consumable, Frame, Player, Solid}

import scala.collection.immutable.HashSet
import scala.collection.mutable
import scala.util.Random

class Game(size: Size, val exitHandler: () => Unit) {
  private val context: GameContext = new GameContext(size)
  private var player: Player = _
  private val keysPressed: mutable.Set[KeyCode] = new mutable.HashSet[KeyCode]()
  private var lastTick = 0L

  private def init(): Unit = {
    val gridSquareSize = 10.0
    val gravityForce = 0.1
    val itemChance = 0.16
    val frame = new Frame(context, new Rect(
      .0, .0,
      context.world.frame.size.width,
      context.world.frame.size.height
    ), gridSquareSize)
    val rnd = new Random()
    val count: Int = (context.world.frame.size.width * context.world.frame.size.height * itemChance / (gridSquareSize * gridSquareSize)).intValue()
    val x: Int = (context.world.frame.size.width / gridSquareSize - 2).intValue()
    player = new Player(
      context,
      new Rect(
        0.0,
        0.0,
        gridSquareSize,
        gridSquareSize * 2
      ),
      Animation.withSingleRenderObject(new RenderObject(new Image("/img/idle.png"))),
      Animation.withSingleRenderObject(new RenderObject(new Image("/img/jump.png"))),
      Animation.withSingleRenderObject(new RenderObject(new Image("/img/crouch.png"))),
      Animation.withSingleRenderObject(new RenderObject(new Image("/img/crouch.png"))),
      Animation.withSpeedAndImage(1000, new Image("/img/move.png"), 40, 80, 6)
    )
    player.speed = 1.3
    player.jumpSpeed = 2.5
    player.physics.get.gravityForce = gravityForce
    player.physics.get.gravity = true
    player.addChild(context.world.camera)
    player.renderObject = Some(RenderObject.fromColor(Color.BLACK))
    context.world.addChild(player)
    val y = (context.world.frame.size.height / gridSquareSize - 2).intValue()
    frame.ceiling.renderObject = Some(RenderObject.fromColor(Color.BLACK))
    frame.wallLeft.renderObject = Some(RenderObject.fromColor(Color.BLACK))
    frame.wallRight.renderObject = Some(RenderObject.fromColor(Color.BLACK))
    frame.floor.renderObject = Some(RenderObject.fromColor(Color.BLACK))
    context.world.addChild(frame)
    val takenX: Array[Int] = new Array[Int](count)
    val takenY: Array[Int] = new Array[Int](count)
    var powerCount: Int = count / 2
    player.maxPower = powerCount
    var rndX: Int = 0
    var rndY: Int = 0
    for (i <- 0 until count) {
      var taken: Boolean = false
      do {
        taken = false
        rndX = rnd.nextInt(x)
        rndY = rnd.nextInt(y)
        for (j <- 0 to i) {
          if (rndX == takenX(j) && rndY == takenY(j)) {
            taken = true
          }
        }
      } while (taken)

      takenX(i) = rndX
      takenY(i) = rndY

      val rect = new Rect(
        context.world.frame.size.width / 2 - gridSquareSize * 1.5 - rndX * gridSquareSize,
        context.world.frame.size.height / 2 - gridSquareSize * 1.5 - rndY * gridSquareSize,
        gridSquareSize,
        gridSquareSize)

      if (powerCount > 0) {
        val gameObject = new Consumable(context, rect)
        gameObject.renderObject = Some(RenderObject.fromColor(Color.GREEN))
        context.world.addChild(gameObject)
        powerCount -= 1
      } else {
        val gameObject = new Solid(context, rect)
        gameObject.renderObject = Some(new RenderObject(new Image("/img/brick.png")))
        context.world.addChild(gameObject)
      }
    }

    context.ui.healthBarHolder.renderObject = Some(RenderObject.fromColor(Color.BLACK))
    context.ui.healthBar.renderObject = Some(RenderObject.fromColor(Color.RED))
    context.ui.powerBarHolder.renderObject = Some(RenderObject.fromColor(Color.BLACK))
    context.ui.powerBar.renderObject = Some(RenderObject.fromColor(Color.GREEN))
    context.ui.deathText.text = "You died! Game Over!"
    context.ui.deathText.font = Some(new Font(25.0))
    context.ui.deathText.color = Color.RED
    context.ui.deathText.visible = false
    context.ui.winText.text = "Congratulations! You won!"
    context.ui.winText.font = Some(new Font(25.0))
    context.ui.winText.color = Color.GREEN
    context.ui.winText.visible = false

  }
  init()

  def keyDown(keyCode: KeyCode) {
    keysPressed.add(keyCode)
    context.world.keyDown(keyCode)
  }

  def keyUp(keyCode: KeyCode) {
    keysPressed.remove(keyCode)
  }

  def tick(ticks: Long, graphics: GraphicsContext) {
    if (ticks - lastTick < Game.tickThreshold) {
      return
    }
    lastTick = ticks

    if (context.quit) {
      exitHandler()
      return
    }

    context.world.handleKeyboardState(HashSet() ++ keysPressed)

    context.world.clean()

    context.world.processPhysics()

    context.world.detectCollisions()

    context.world.animate(ticks)

    graphics.clearRect(0.0, 0.0, context.windowSize.width, context.windowSize.height)
    context.world.render(graphics)
    context.ui.render(graphics)
  }

  def resize(size: Size) {
    context.windowSize = size
    context.world.onWindowResize(size)
    context.ui.onWindowResize(size)
  }

}

object Game {
  private val tickThreshold: Long = 10000000L
}
