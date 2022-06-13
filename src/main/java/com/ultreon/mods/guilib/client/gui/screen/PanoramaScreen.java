/*
 * Copyright (c) 2022. - Qboi SMP Development Team
 * Do NOT redistribute, or copy in any way, and do NOT modify in any way.
 * It is not allowed to hack into the code, use cheats against the code and/or compiled form.
 * And it is not allowed to decompile, modify or/and patch parts of code or classes or in full form.
 * Sharing this file isn't allowed either, and is hereby strictly forbidden.
 * Sharing decompiled code on social media or an online platform will cause in a report on that account.
 *
 * ONLY the owner can bypass these rules.
 */

package com.ultreon.mods.guilib.client.gui.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.network.chat.Component;

import java.util.Objects;

import static net.minecraft.client.gui.screens.TitleScreen.PANORAMA_OVERLAY;

/**
 * Panorama screen, for rendering a panorama in the background of your screen / menu.
 *
 * @author Qboi123
 */
public abstract class PanoramaScreen extends BaseScreen {
    public static final PanoramaRenderer panorama = new PanoramaRenderer(TitleScreen.CUBE_MAP);

    /**
     * Panorama screen constructor.
     *
     * @param title screen title.
     */
    protected PanoramaScreen(Component title) {
        super(title);
    }

    /**
     * Render the panorama background/
     *
     * @param pose pose stack.
     * @param frameTime render frame time.
     */
    public void renderPanorama(PoseStack pose, float frameTime) {
        // Nonnull Requirements
        Objects.requireNonNull(this.minecraft);

        panorama.render(frameTime, 1.0f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, PANORAMA_OVERLAY);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        blit(pose, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
    }
}
