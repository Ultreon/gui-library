package com.ultreon.mods.guilib.client.gui.screen.window;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;

public class WMRenderLayer extends Gui implements IIngameOverlay {
    public WMRenderLayer(Minecraft pMinecraft) {
        super(pMinecraft);
    }

    public static void register() {
        OverlayRegistry.registerOverlayTop("wm_render_layer", new WMRenderLayer(Minecraft.getInstance()));
    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        if (Minecraft.getInstance().screen != null) {
            WindowManager.INSTANCE.renderAllWindows(poseStack, Integer.MAX_VALUE, Integer.MAX_VALUE, partialTick);
        }
    }
}
