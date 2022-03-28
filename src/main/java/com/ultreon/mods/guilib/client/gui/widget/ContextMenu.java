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

package com.ultreon.mods.guilib.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.ultreon.mods.guilib.UltreonGuiLib;
import com.ultreon.mods.guilib.client.gui.screen.BaseScreen;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Qboi123
 */
public class ContextMenu extends AbstractContainerWidget {
    // Constants
    private static final int BORDER_WIDTH = 5;
    private static final ResourceLocation WIDGETS = UltreonGuiLib.res("textures/gui/widgets/context_menu");

    // Entries
    private final List<MenuItem> entries = new ArrayList<>();

    // Events.
    private OnClose onClose;

    /**
     * @param x position x to place.
     * @param y position y to place.
     * @param title context menu title.
     */
    public ContextMenu(int x, int y, Component title) {
        super(x, y, BORDER_WIDTH * 2, BORDER_WIDTH * 2, title);
    }

    /**
     * Updates narration.
     *
     * @param narration output for narration elements.
     */
    @Override
    public void updateNarration(@NotNull NarrationElementOutput narration) {
        narration.add(NarratedElementType.TITLE, this.createNarrationMessage());
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float frameTime) {
        RenderSystem.setShaderTexture(0, WIDGETS);

        blit(pose, x, y, 5, 5, 0, 0, 5, 5, 16, 16);
        blit(pose, x + width - 5, y, 5, 5, 6, 0, 5, 5, 16, 16);
        blit(pose, x, y + height, 5, 5, 6, 0, 5, 5, 16, 16);

        super.render(pose, mouseX, mouseY, frameTime);
    }

    /**
     * Adds a menu item entry.
     *
     * @param menuItem menu item to add.
     * @param <T> item type.
     * @return the same as menu item parameter.
     */
    public <T extends MenuItem> T add(T menuItem) {
        entries.add(menuItem);
        return menuItem;
    }

    void invalidateSize() {
        width = BORDER_WIDTH * 2 + entries.stream().mapToInt(MenuItem::getMinWidth).max().orElse(1);
        height = BORDER_WIDTH * 2 + entries.stream().mapToInt(MenuItem::getHeight).sum();
    }

    /**
     * Get all menu entries currently in the context menu.
     *
     * @return all menu entries (unmodifiable).
     */
    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Set the on close event handler.
     *
     * @param onClose close handler.
     */
    public void setOnClose(OnClose onClose) {
        this.onClose = onClose;
    }

    /**
     * DON'T CALL IF YOU DON'T KNOW WHAT YOU ARE DOING.
     * This method is called for internal usage, and should not be called to close the context menu.
     * Use the {@link BaseScreen#closeContextMenu()} method to close the menu instead.
     */
    public final void onClose() {
        onClose.call(this);
    }

    /**
     * On Close event handler interface.
     * Could be used as lambda.
     */
    @FunctionalInterface
    public interface OnClose {
        /**
         * Handler itself.
         *
         * @param menu context menu for handling the closing with.
         */
        void call(ContextMenu menu);
    }
}
