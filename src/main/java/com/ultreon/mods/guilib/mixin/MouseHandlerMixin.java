package com.ultreon.mods.guilib.mixin;

import com.ultreon.mods.guilib.client.gui.screen.window.ScreenHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "onMove", at = @At("HEAD"), cancellable = true)
    public void onMove(long windowPtr, double xPos, double yPos, CallbackInfo ci) {
        double mouseX = xPos * (double) this.minecraft.getWindow().getGuiScaledWidth() / (double) this.minecraft.getWindow().getScreenWidth();
        double mouseY = yPos * (double) this.minecraft.getWindow().getGuiScaledHeight() / (double) this.minecraft.getWindow().getScreenHeight();

        if (ScreenHooks.onMouseMoved(windowPtr, mouseX, mouseY)) {
            ci.cancel();
        }
    }
}
