package com.github.noamm9.skijarenderer.helpers

import kotlin.test.Test
import kotlin.test.assertEquals

class SkijaTextTest {
    @Test
    fun `stripFormatting removes minecraft formatting codes`() {
        assertEquals("Hello World", SkijaText.stripFormatting("\u00A7aHello \u00A7lWorld"))
    }

    @Test
    fun `stripFormatting removes malformed replacement plus section sequences`() {
        assertEquals("Hello", SkijaText.stripFormatting("\uFFFD\u00A7aHello"))
    }

    @Test
    fun `stripFormatting leaves normal text alone`() {
        assertEquals("Plain text", SkijaText.stripFormatting("Plain text"))
    }
}
