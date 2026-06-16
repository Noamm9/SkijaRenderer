package com.github.noamm9.skijarenderer.skia

import com.mojang.blaze3d.opengl.GlTexture
import net.minecraft.client.Minecraft

/**
 * Composites the global Skija batch (draw calls made with no active frame) onto the
 * Minecraft framebuffer once per frame, at the tail of the game render pass.
 *
 * Draws onto the existing framebuffer (over screens + HUD), so it never clears.
 */
internal object SkijaCompositor {
    val glSurface = SkijaGlSurface()

    @JvmStatic
    fun composite() {
        if (! Skija.hasBatch()) return

        val minecraft = Minecraft.getInstance()
        val window = minecraft.window
        val mainRenderTarget = minecraft.mainRenderTarget

        val width = mainRenderTarget.width.takeIf { it > 0 } ?: return
        val height = mainRenderTarget.height.takeIf { it > 0 } ?: return
        val colorTexId = (mainRenderTarget.colorTexture as? GlTexture)?.glId() ?: return

        val rawWidth = width.toFloat()
        val rawHeight = height.toFloat()
        val guiWidth = window.guiScaledWidth.toFloat().coerceAtLeast(1f)
        val dpr = (rawWidth / guiWidth).takeIf { it.isFinite() && it > 0f } ?: 1f

        glSurface.render(width, height, rawWidth, rawHeight, dpr, colorTexId, clear = false) {
            Skija.flush()
        }
    }
}
