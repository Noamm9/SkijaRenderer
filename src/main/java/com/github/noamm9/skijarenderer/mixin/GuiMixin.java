package com.github.noamm9.skijarenderer.mixin;

import com.github.noamm9.skijarenderer.SkijaRenderer;
import com.github.noamm9.skijarenderer.skia.Skija;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void skijarenderer$render(CallbackInfo ci) {
        if (!SkijaRenderer.INSTANCE.getDev$SkijaRenderer()) return;
        SkijaRenderer.drawDemo();
    }
}