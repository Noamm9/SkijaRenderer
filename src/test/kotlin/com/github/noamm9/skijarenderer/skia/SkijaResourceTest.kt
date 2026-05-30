package com.github.noamm9.skijarenderer.skia

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SkijaResourceTest {
    @Test
    fun `images compare by location`() {
        assertEquals(SkijaImage("assets/skijarenderer/icon.png"), SkijaImage("assets/skijarenderer/icon.png"))
    }

    @Test
    fun `image detects svg extension`() {
        assertTrue(SkijaImage("assets/skijarenderer/demo-badge.svg").isSvg)
    }

    @Test
    fun `fonts compare by location`() {
        assertEquals(SkijaFont("assets/skijarenderer/inter.ttf"), SkijaFont("assets/skijarenderer/inter.ttf"))
    }
}
