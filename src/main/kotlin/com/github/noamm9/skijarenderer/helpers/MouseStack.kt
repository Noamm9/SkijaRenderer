package com.github.noamm9.skijarenderer.helpers

import net.minecraft.client.Minecraft
import org.joml.Matrix3x2f
import org.joml.Vector2f

/**
 * Simple 2D transform stack for mapping screen-space mouse coordinates into local GUI space.
 *
 * The current local mouse position is recalculated whenever the transform changes or when a new
 * screen-space position is supplied through [update] or [updateFromMinecraft].
 */
class MouseStack(autoUpdateFromMinecraft: Boolean = true) {
    private var current = Matrix3x2f()
    private val stack = ArrayDeque<Matrix3x2f>()
    private var screenX = 0.0
    private var screenY = 0.0

    var x: Double = 0.0
        private set
    var y: Double = 0.0
        private set

    init {
        if (autoUpdateFromMinecraft) {
            updateFromMinecraft()
        }
    }

    fun push() {
        stack.addLast(Matrix3x2f(current))
    }

    fun pop() {
        if (stack.isNotEmpty()) {
            current = stack.removeLast()
            updatePosition()
        }
    }

    fun translate(x: Number, y: Number) {
        current.translate(x.toFloat(), y.toFloat())
        updatePosition()
    }

    fun scale(x: Number, y: Number) {
        current.scale(x.toFloat(), y.toFloat())
        updatePosition()
    }

    fun scale(n: Number) {
        current.scale(n.toFloat(), n.toFloat())
        updatePosition()
    }

    fun update(screenX: Double, screenY: Double) {
        this.screenX = screenX
        this.screenY = screenY
        updatePosition()
    }

    fun updateFromMinecraft() {
        val mc = Minecraft.getInstance()
        val mouse = mc.mouseHandler
        val window = mc.window
        update(mouse.getScaledXPos(window), mouse.getScaledYPos(window))
    }

    fun toLocal(x: Double, y: Double): Pair<Double, Double> {
        val det = current.determinant()
        if (det == 0f) return x to y

        val inv = Matrix3x2f(current).invert()
        val vector = Vector2f(x.toFloat(), y.toFloat())
        inv.transformPosition(vector)
        return vector.x.toDouble() to vector.y.toDouble()
    }

    private fun updatePosition() {
        val (localX, localY) = toLocal(screenX, screenY)
        x = localX
        y = localY
    }
}