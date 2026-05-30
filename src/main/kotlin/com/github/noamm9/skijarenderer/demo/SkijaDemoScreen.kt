package com.github.noamm9.skijarenderer.demo

import com.github.noamm9.skijarenderer.helpers.MouseStack
import com.github.noamm9.skijarenderer.helpers.SkijaText
import com.github.noamm9.skijarenderer.skia.Skija
import com.github.noamm9.skijarenderer.skia.SkijaGradient
import com.github.noamm9.skijarenderer.skia.SkijaImage
import com.github.noamm9.skijarenderer.skia.SkijaPIP.Companion.drawSkija
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import java.awt.Color
import kotlin.math.sin

class SkijaDemoScreen: Screen(Component.translatable("screen.skijarenderer.demo")) {
    private val mouseStack = MouseStack(autoUpdateFromMinecraft = false)
    private var iconImage: SkijaImage? = null
    private var svgImage: SkijaImage? = null
    private val startNanos = System.nanoTime()

    override fun init() {
        super.init()
        if (iconImage == null) iconImage = Skija.createImage("assets/skijarenderer/icon.png")
        if (svgImage == null) svgImage = Skija.createImage("assets/skijarenderer/demo-badge.svg")
    }

    override fun removed() {
        iconImage?.let(Skija::deleteImage)
        svgImage?.let(Skija::deleteImage)
        iconImage = null
        svgImage = null
        super.removed()
    }

    override fun isPauseScreen(): Boolean = false

    override fun extractRenderState(guiGraphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick)

        val time = (System.nanoTime() - startNanos) / 1_000_000_000f
        val screenWidth = width.toFloat()
        val screenHeight = height.toFloat()
        val panelX = 26f
        val panelY = 28f
        val panelWidth = screenWidth - panelX * 2f
        val panelHeight = screenHeight - panelY * 2f

        guiGraphics.drawSkija {
            Skija.rect(0f, 0f, screenWidth, screenHeight, Color(6, 10, 18, 165))
            Skija.dropShadow(panelX, panelY, panelWidth, panelHeight, 28f, 10f, 20f)
            Skija.gradientRect(
                panelX,
                panelY,
                panelWidth,
                panelHeight,
                Color(16, 21, 33, 240),
                Color(10, 13, 22, 235),
                SkijaGradient.TOP_BOTTOM,
                20f
            )
            Skija.hollowRect(panelX, panelY, panelWidth, panelHeight, 1f, Color(90, 122, 184, 110), 20f)

            drawHeader(panelX, panelY, panelWidth)
            drawShapeShowcase(panelX + 20f, panelY + 78f, time)
            drawTextShowcase(panelX + 250f, panelY + 78f)
            drawClipShowcase(panelX + 20f, panelY + 248f, time)
            drawTransformShowcase(panelX + 250f, panelY + 248f, mouseX.toFloat(), mouseY.toFloat(), time)
            drawImageShowcase(panelX + panelWidth - 250f, panelY + 78f, time)
        }
    }

    private fun drawHeader(panelX: Float, panelY: Float, panelWidth: Float) {
        Skija.drawHalfRoundedRect(panelX + 20f, panelY + 18f, 190f, 34f, Color(50, 78, 132, 215), 14f, true)
        Skija.drawHalfRoundedRect(panelX + 20f, panelY + 52f, 190f, 10f, Color(22, 35, 59, 215), 8f, false)
        SkijaText.draw("Skija Demo", panelX + 34f, panelY + 26f, Color.WHITE, 20f)
        SkijaText.draw(
            "F8 reopens this screen, ESC closes it",
            panelX + panelWidth - 24f,
            panelY + 24f,
            Color(190, 202, 230),
            12f,
            align = SkijaText.Align.RIGHT
        )
        SkijaText.drawGradient("Native Skija GUI rendering", panelX + 34f, panelY + 58f, Color(160, 212, 255), Color(132, 255, 208), 12f)
    }

    private fun drawShapeShowcase(x: Float, y: Float, time: Float) {
        SkijaText.draw("Shapes", x, y, Color.WHITE, 16f)
        val originY = y + 30f

        Skija.rect(x, originY, 190f, 120f, Color(12, 16, 27, 170), 16f)
        Skija.line(x + 16f, originY + 88f, x + 174f, originY + 88f, 2f, Color(135, 185, 255))
        Skija.circle(x + 42f, originY + 42f, 18f, Color(89, 218, 255))
        Skija.rect(x + 74f, originY + 24f, 42f, 42f, Color(100, 140, 255, 220), 10f)
        Skija.hollowRect(x + 128f, originY + 18f, 42f, 54f, 3f, Color(255, 255, 255, 180), 12f)
        Skija.dropShadow(x + 56f, originY + 90f, 84f, 14f, 12f, 2f, 7f)
        Skija.gradientRect(x + 56f, originY + 90f, 84f, 14f, Color(255, 115, 115), Color(255, 214, 102), SkijaGradient.LEFT_RIGHT, 7f)

        Skija.push()
        Skija.translate(x + 150f, originY + 92f)
        Skija.rotate(sin(time) * 0.15f)
        Skija.globalAlpha(0.85f)
        Skija.rect(- 18f, - 18f, 36f, 36f, Color(87, 255, 190, 210), 10f)
        Skija.globalAlpha(1f)
        Skija.pop()
    }

    private fun drawTextShowcase(x: Float, y: Float) {
        SkijaText.draw("Text", x, y, Color.WHITE, 16f)
        val boxY = y + 30f

        Skija.rect(x, boxY, 220f, 120f, Color(12, 16, 27, 170), 16f)
        SkijaText.draw("Plain text", x + 16f, boxY + 16f, Color.WHITE, 14f)
        SkijaText.draw("Centered", x + 110f, boxY + 36f, Color(202, 233, 255), 14f, align = SkijaText.Align.CENTER)
        SkijaText.draw("Shadow", x + 16f, boxY + 58f, Color(255, 223, 166), 14f, shadow = true)
        SkijaText.drawGradient("Gradient", x + 16f, boxY + 82f, Color(157, 212, 255), Color(136, 255, 197), 16f)
        Skija.textGradient("Vertical", x + 136f, boxY + 80f, 16f, 70f, Color(255, 176, 138), Color(255, 244, 165), direction = SkijaGradient.TOP_BOTTOM)

        SkijaText.wrap("This block exercises width, alignment, gradients, wrapping, and formatting cleanup.", 188f, 12f)
            .take(3)
            .forEachIndexed { index, line ->
                SkijaText.draw(line, x + 16f, boxY + 106f + index * 13f, Color(190, 202, 230), 12f)
            }
    }

    private fun drawClipShowcase(x: Float, y: Float, time: Float) {
        SkijaText.draw("Scissor / clipping", x, y, Color.WHITE, 16f)
        val boxY = y + 30f

        Skija.rect(x, boxY, 190f, 120f, Color(12, 16, 27, 170), 16f)
        Skija.pushScissor(x + 12f, boxY + 12f, 166f, 96f)
        for (index in 0 until 7) {
            val itemY = boxY + 18f + index * 22f + sin(time * 1.8f + index) * 10f
            val color = if (index % 2 == 0) Color(78, 124, 214, 180) else Color(56, 92, 162, 180)
            Skija.rect(x + 18f, itemY, 148f, 16f, color, 7f)
            SkijaText.draw("Row ${index + 1}", x + 28f, itemY + 4f, Color.WHITE, 12f)
            Skija.circle(x + 154f, itemY + 8f, 5f, Color(255, 225, 145))
        }
        Skija.popScissor()
        Skija.hollowRect(x + 12f, boxY + 12f, 166f, 96f, 1f, Color(255, 255, 255, 80), 10f)
    }

    private fun drawTransformShowcase(x: Float, y: Float, mouseX: Float, mouseY: Float, time: Float) {
        SkijaText.draw("Transforms / hover", x, y, Color.WHITE, 16f)
        val boxY = y + 30f
        val scale = 1.15f + (sin(time * 1.4f) * 0.08f)

        mouseStack.update(mouseX.toDouble(), mouseY.toDouble())
        mouseStack.push()
        mouseStack.translate(x + 22f, boxY + 20f)
        mouseStack.scale(scale)

        val hovered = mouseStack.x in 0.0 .. 120.0 && mouseStack.y in 0.0 .. 54.0

        Skija.rect(x, boxY, 220f, 120f, Color(12, 16, 27, 170), 16f)
        Skija.push()
        Skija.translate(x + 22f, boxY + 20f)
        Skija.scale(scale)
        Skija.rect(0f, 0f, 120f, 54f, if (hovered) Color(77, 170, 122, 230) else Color(62, 78, 112, 220), 12f)
        Skija.hollowRect(0f, 0f, 120f, 54f, 2f, Color.WHITE, 12f)
        SkijaText.draw(if (hovered) "Hovered" else "Move mouse here", 60f, 18f, Color.WHITE, 13f, align = SkijaText.Align.CENTER)
        SkijaText.draw("${mouseStack.x.toInt()}, ${mouseStack.y.toInt()}", 60f, 34f, Color(232, 239, 255), 12f, align = SkijaText.Align.CENTER)
        Skija.pop()
        mouseStack.pop()

        SkijaText.draw("MouseStack follows translate + scale", x + 16f, boxY + 94f, Color(190, 202, 230), 12f)
    }

    private fun drawImageShowcase(x: Float, y: Float, time: Float) {
        SkijaText.draw("Images", x, y, Color.WHITE, 16f)
        val boxY = y + 30f

        Skija.rect(x, boxY, 220f, 120f, Color(12, 16, 27, 170), 16f)
        iconImage?.let { Skija.image(it, x + 18f, boxY + 18f, 56f, 56f, 14f) }
        svgImage?.let { Skija.image(it, x + 90f, boxY + 18f, 56f, 56f, 14f) }
        Skija.push()
        Skija.translate(x + 178f, boxY + 46f)
        Skija.rotate(sin(time * 1.3f) * 0.25f)
        svgImage?.let { Skija.image(it, - 20f, - 20f, 40f, 40f) }
        Skija.pop()

        SkijaText.draw("PNG", x + 46f, boxY + 86f, Color(190, 202, 230), 12f, align = SkijaText.Align.CENTER)
        SkijaText.draw("SVG", x + 118f, boxY + 86f, Color(190, 202, 230), 12f, align = SkijaText.Align.CENTER)
        SkijaText.draw("Animated", x + 178f, boxY + 86f, Color(190, 202, 230), 12f, align = SkijaText.Align.CENTER)
    }
}