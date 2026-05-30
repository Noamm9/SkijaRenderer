package com.github.noamm9.skijarenderer.helpers

import kotlin.test.Test
import kotlin.test.assertEquals

class MouseStackTest {
    @Test
    fun `translate updates local mouse coordinates`() {
        val stack = MouseStack(autoUpdateFromMinecraft = false)

        stack.update(30.0, 50.0)
        stack.translate(10, 5)

        assertEquals(20.0, stack.x, 0.0001)
        assertEquals(45.0, stack.y, 0.0001)
    }

    @Test
    fun `scale updates local mouse coordinates`() {
        val stack = MouseStack(autoUpdateFromMinecraft = false)

        stack.update(30.0, 50.0)
        stack.scale(2)

        assertEquals(15.0, stack.x, 0.0001)
        assertEquals(25.0, stack.y, 0.0001)
    }

    @Test
    fun `pop restores the previous transform and recomputes coordinates`() {
        val stack = MouseStack(autoUpdateFromMinecraft = false)

        stack.update(30.0, 50.0)
        stack.push()
        stack.translate(10, 5)
        stack.pop()

        assertEquals(30.0, stack.x, 0.0001)
        assertEquals(50.0, stack.y, 0.0001)
    }

    @Test
    fun `singular transforms fall back to the input coordinates`() {
        val stack = MouseStack(autoUpdateFromMinecraft = false)

        stack.scale(0)

        val local = stack.toLocal(12.0, 34.0)
        assertEquals(12.0, local.first, 0.0001)
        assertEquals(34.0, local.second, 0.0001)
    }
}
