package com.github.noamm9.skijarenderer

import com.github.noamm9.skijarenderer.skia.Skija
import com.github.noamm9.skijarenderer.skia.SkijaGradient
import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import org.lwjgl.glfw.GLFW
import java.awt.Color

object SkijaRenderer: ClientModInitializer {
    internal var dev = false

    private val openDemoKey = KeyMappingHelper.registerKeyMapping(
        KeyMapping("skija demo", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F8, KeyMapping.Category.DEBUG)
    )

    override fun onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            while (openDemoKey.consumeClick()) dev = dev.not()
        }
    }

    @JvmStatic
    fun drawDemo() {
        val x = 20f
        val y = 20f
        val w = 230f
        val h = 132f

        Skija.dropShadow(x, y, w, h, 24f, 6f, 16f)
        Skija.gradientRect(x, y, w, h, Color(22, 28, 44, 240), Color(12, 16, 28, 235), SkijaGradient.TOP_BOTTOM, 16f)
        Skija.hollowRect(x, y, w, h, 1f, Color(96, 130, 200, 110), 16f)

        Skija.image("assets/skijarenderer/icon.png", x + 16f, y + 14f, 28f, 28f, 8f)
        Skija.text("Skija Renderer", x + 54f, y + 16f, 18f, Color.WHITE)
        Skija.text("Drawing on the GL context", x + 54f, y + 38f, 11f, Color(168, 184, 216))

        val rowY = y + 70f
        Skija.circle(x + 32f, rowY + 18f, 16f, Color(89, 218, 255))
        Skija.rect(x + 60f, rowY + 2f, 32f, 32f, Color(100, 140, 255, 220), 8f)
        Skija.hollowRect(x + 104f, rowY, 36f, 36f, 2f, Color(255, 255, 255, 170), 10f)
        Skija.gradientRect(x + 152f, rowY + 6f, 60f, 24f, Color(255, 115, 115), Color(255, 214, 102), SkijaGradient.LEFT_RIGHT, 8f)
    }
}