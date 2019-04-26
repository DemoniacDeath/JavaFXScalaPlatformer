package main

import javafx.animation.AnimationTimer
import javafx.application.{Application, Platform}
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyEvent
import javafx.scene.{Group, Scene}
import javafx.stage.Stage

class App extends Application {
  override def start(primaryStage: Stage): Unit = {
    val width = 800.0
    val height = 600.0
    primaryStage.setTitle("JavaFX + Scala Platformer")

    val root = new Group
    val canvas = new Canvas(width, height)
    val gc = canvas.getGraphicsContext2D
    val game = new Game(Size(width, height), () => {
      Platform.exit()
      System.exit(0)
    })

    primaryStage.addEventHandler[KeyEvent](KeyEvent.KEY_PRESSED, e => {
      game.keyDown(e.getCode)
    })

    primaryStage.addEventHandler[KeyEvent](KeyEvent.KEY_RELEASED, e => {
      game.keyUp(e.getCode)
    })

    primaryStage.widthProperty().addListener((_, _, newWidth) => {
      canvas.setWidth(newWidth.doubleValue() - 16)
      game.resize(Size(canvas.getWidth, canvas.getHeight))
    })
    primaryStage.heightProperty().addListener((_, _, newHeight) => {
      canvas.setHeight(newHeight.doubleValue() - 16)
      game.resize(Size(canvas.getWidth, canvas.getHeight))
    })

    new AnimationTimer() {
      override def handle(now: Long): Unit = {
        game.tick(now, gc)
      }
    }.start()

    root.getChildren.add(canvas)
    primaryStage.setScene(new Scene(root))
    primaryStage.setWidth(width)
    primaryStage.setHeight(height)
    primaryStage.show()
  }
}