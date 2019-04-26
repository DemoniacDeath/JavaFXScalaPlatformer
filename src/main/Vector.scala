package main

case class Vector(var x: Double = 0.0, var y: Double = 0.0) {
  def *(scalar: Double) = Vector(x * scalar, y * scalar)

  def /(scalar: Double) = Vector(x / scalar, y / scalar)

  def +(v: Vector): Vector = Vector(x + v.x, y + v.y)

  def -(v: Vector) = Vector(x - v.x, y - v.y)
}