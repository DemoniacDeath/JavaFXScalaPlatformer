package main.gameObject.ui

import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.{Scene, SnapshotParameters}
import main.{GameContext, Rect, RenderObject}

class Text(context: GameContext, frame: Rect) extends Element(context, frame) {
  private var _text: String = ""
  private var _color: Color = Color.BLACK
  private var _font: Option[Font] = None

  def text: String = _text

  def text_=(value: String): Unit = {
    _text = value
    generate()
  }


  def color: Color = _color

  def color_=(value: Color): Unit = {
    _color = value
    generate()
  }


  def font: Option[Font] = _font

  def font_=(value: Option[Font]): Unit = {
    _font = value
    generate()
  }

  def generate() {
    if (this.text.nonEmpty) {
      val label = new Label(this.text)
      if (this.font.nonEmpty) {
        label.setFont(this.font.get)
      }
      label.setTextFill(this.color)
      val pane = new StackPane(label)
      val scene = new Scene(pane)
      val sp = new SnapshotParameters()
      sp.setFill(Color.TRANSPARENT)
      val textImage = label.snapshot(sp, null)
      this.renderObject = Some(new RenderObject(textImage))
    }
  }
}
