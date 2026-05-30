package com.github.noamm9.skijarenderer.helpers

import com.github.noamm9.skijarenderer.skia.Skija
import com.github.noamm9.skijarenderer.skia.SkijaFont
import com.github.noamm9.skijarenderer.skia.SkijaGradient
import java.awt.Color

object SkijaText {
    var font: SkijaFont = Skija.defaultFont
    var size: Float = 9f

    enum class Align { LEFT, CENTER, RIGHT }

    fun width(text: String, size: Float = this.size, font: SkijaFont = this.font): Float {
        return Skija.textWidth(stripFormatting(text), size, font)
    }

    fun draw(
        text: String,
        x: Float,
        y: Float,
        color: Color = Color.WHITE,
        size: Float = this.size,
        font: SkijaFont = this.font,
        align: Align = Align.LEFT,
        shadow: Boolean = false
    ) {
        val clean = stripFormatting(text)
        val drawX = when (align) {
            Align.LEFT -> x
            Align.CENTER -> x - (width(clean, size, font) / 2f)
            Align.RIGHT -> x - width(clean, size, font)
        }

        if (shadow) Skija.textShadow(clean, drawX, y, size, color, font)
        else Skija.text(clean, drawX, y, size, color, font)
    }

    fun drawGradient(
        text: String,
        x: Float,
        y: Float,
        color1: Color,
        color2: Color,
        size: Float = this.size,
        font: SkijaFont = this.font,
        align: Align = Align.LEFT,
        direction: SkijaGradient = SkijaGradient.LEFT_RIGHT
    ) {
        val clean = stripFormatting(text)
        val w = width(clean, size, font)
        val drawX = when (align) {
            Align.LEFT -> x
            Align.CENTER -> x - w / 2f
            Align.RIGHT -> x - w
        }

        Skija.textGradient(clean, drawX, y, size, w, color1, color2, font, direction)
    }

    fun wrap(text: String, maxWidth: Float, size: Float = this.size, font: SkijaFont = this.font): List<String> {
        return Skija.wrap(stripFormatting(text), maxWidth, size, font)
    }

    fun stripFormatting(text: String): String {
        if (text.isEmpty()) return text

        val builder = StringBuilder(text.length)
        var index = 0

        while (index < text.length) {
            val current = text[index]

            if (current == '\u00A7') {
                index = (index + 2).coerceAtMost(text.length)
                continue
            }

            if (current == '\uFFFD' && index + 2 < text.length && text[index + 1] == '\u00A7') {
                index += 3
                continue
            }

            builder.append(current)
            index ++
        }

        return builder.toString()
    }
}
