package com.github.noamm9.skijarenderer.mixin;

import com.github.noamm9.skijarenderer.skia.Skija;
import com.github.noamm9.skijarenderer.skia.SkijaCompositor;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "close", at = @At("TAIL"))
    private void skijarenderer$close(CallbackInfo ci) {
        SkijaCompositor.INSTANCE.getGlSurface().close();
    }

    @Inject(method = "render(Lnet/minecraft/client/DeltaTracker;Z)V", at = @At("TAIL"))
    private void skijarenderer$render(CallbackInfo ci) {
        SkijaCompositor.composite();
    }
}

