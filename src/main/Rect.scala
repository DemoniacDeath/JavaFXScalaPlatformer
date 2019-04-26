package main

class Rect(var center: Vector, var size: Size) {
  def this(
            x: Double,
            y: Double,
            width: Double,
            height: Double
          ) = this(
    Vector(x, y),
    Size(width, height)
  )
}