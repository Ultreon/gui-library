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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.ultreon.mods.guilib.UltreonGuiLib;
import com.ultreon.mods.guilib.client.HasContextMenu;
import com.ultreon.mods.guilib.client.gui.ReloadsTheme;
import com.ultreon.mods.guilib.client.gui.Theme;
import com.ultreon.mods.guilib.client.gui.widget.ButtonMenuItem;
import com.ultreon.mods.guilib.client.gui.widget.ContextMenu;
import com.ultreon.mods.guilib.client.input.MouseButton;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class BaseScreen extends Screen implements ReloadsTheme {
    private static final String CLOSE_ICON = "\u00D7";
    private static final String CLOSE_ICON_HOVER = ChatFormatting.RED + CLOSE_ICON;

    private static final ResourceLocation WIDGETS_DARK = UltreonGuiLib.res("textures/gui/widgets_dark.png");
    private static final ResourceLocation WIDGETS = UltreonGuiLib.res("textures/gui/widgets.png");
    private static final ResourceLocation WIDGETS_LIGHT = UltreonGuiLib.res("textures/gui/widgets_light.png");

    private ContextMenu contextMenu = null;
    private Theme theme;

    protected BaseScreen(Component title) {
        super(title);
        this.theme = UltreonGuiLib.getTheme();
    }

    @Override
    public void reloadTheme() {
        this.theme = UltreonGuiLib.getTheme();
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float frameTime) {
        renderCloseButton(pose, mouseX, mouseY);

        boolean flag = contextMenu != null && contextMenu.isMouseOver(mouseX, mouseY);

        int mx = flag ? Integer.MIN_VALUE : mouseX;
        int my = flag ? Integer.MIN_VALUE : mouseY;
        for (Widget widget : this.renderables) {
            widget.render(pose, mx, my, frameTime);
        }

        renderContextMenu(pose, mouseX, mouseY, frameTime);
    }

    private void renderContextMenu(PoseStack pose, int mouseX, int mouseY, float frameTime) {
        ContextMenu menu = contextMenu;
        if (menu != null) {
            RenderSystem.disableDepthTest();
            menu.render(pose, mouseX, mouseY, frameTime);
        }
    }

    public abstract Vec2 getCloseButtonPos();

    protected final boolean isPointBetween(int mouseX, int mouseY, int x, int y, int w, int h) {
        final int x1 = x + w;
        final int y1 = y + h;

        return mouseX >= x && mouseY >= y && mouseX <= x1 && mouseY <= y1;
    }

    protected final void renderCloseButton(PoseStack pose, int mouseX, int mouseY) {
        Vec2 iconPos = getCloseButtonPos();
        if (iconPos != null) {
            int iconX = (int) iconPos.x;
            int iconY = (int) iconPos.y;
            if (isPointBetween(mouseX, mouseY, iconX, iconY, 6, 6)) {
                this.font.draw(pose, CLOSE_ICON_HOVER, iconX, iconY, theme.getTitleColor());
            } else {
                this.font.draw(pose, CLOSE_ICON, iconX, iconY, theme.getTitleColor());
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isHoveringContextMenu((int) mouseX, (int) mouseY) && contextMenu.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean isHoveringContextMenu(int mouseX, int mouseY) {
        return contextMenu != null &&
                mouseX >= contextMenu.x && mouseX <= contextMenu.x + contextMenu.getWidth() &&
                mouseY >= contextMenu.y && mouseY <= contextMenu.y + contextMenu.getHeight();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (isHoveringContextMenu((int) mouseX, (int) mouseY)) {
            contextMenu.mouseMoved(mouseX, mouseY);
            return;
        }

        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isAtCloseButton(mouseX, mouseY)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            back();
            return true;
        }

        if (isHoveringContextMenu((int) mouseX, (int) mouseY) && contextMenu.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        if (button == MouseButton.RIGHT && isAtTitleBar(mouseX, mouseY)) {
            ContextMenu menu = new ContextMenu((int) mouseX, (int) mouseY, null);
            menu.add(new ButtonMenuItem(menu, new TextComponent("Close"), menuItem -> closeScreen()));
            placeContextMenu(menu);
            return true;
        }
        if (button == MouseButton.RIGHT) {
            ContextMenu menu = getContextMenu((int) mouseX, (int) mouseY);
            GuiEventListener it = getChildAt(mouseX, mouseY).orElse(null);
            if (it instanceof HasContextMenu contextMenuHolder) {
                contextMenuHolder.contextMenu((int) mouseX, (int) mouseY, button);
            }
            if (menu != null) {
                placeContextMenu(menu);
                return true;
            }
        }
        if (button == MouseButton.LEFT && contextMenu != null && !contextMenu.isMouseOver(mouseX, mouseY)) {
            contextMenu.onClose();
            contextMenu = null;
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public final void closeScreen() {
        onClose();
    }

    protected ContextMenu getContextMenu(int x, int y) {
        return null;
    }

    protected boolean isAtTitleBar(double mouseX, double mouseY) {
        return false;
    }

    public void placeContextMenu(@NotNull ContextMenu menu) {
        this.contextMenu = Objects.requireNonNull(menu);
    }

    public void closeContextMenu() {
        this.contextMenu = null;
    }

    protected void back() {
        onClose();
    }

    protected final boolean isAtCloseButton(int mouseX, int mouseY) {
        Vec2 iconPos = getCloseButtonPos();
        if (iconPos == null) {
            return false;
        }

        return isPointBetween(mouseX, mouseY, (int) iconPos.x, (int) iconPos.y, 6, 6);
    }

    protected final boolean isAtCloseButton(double mouseX, double mouseY) {
        return isAtCloseButton((int) mouseX, (int) mouseY);
    }

    public static void renderFrame(PoseStack pose, int x, int y, int width, int height, boolean darkMode) {
        renderFrame(pose, x, y, width, height, darkMode, 0);
    }

    public static void renderFrame(PoseStack pose, int x, int y, int width, int height, Theme theme) {
        renderFrame(pose, x, y, width, height, theme, 0);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    public static void renderFrame(PoseStack pose, int x, int y, int width, int height, boolean darkMode, int u) {
        RenderSystem.setShaderTexture(0, darkMode ? WIDGETS_DARK : WIDGETS);
        blit(pose, x, y, 7, 7, u + 0, 0, 7, 7, 256, 256);
        blit(pose, x + 7, y, width, 7, u + 7, 0, 7, 7, 256, 256);
        blit(pose, x + 7 + width, y, 7, 7, u + 14, 0, 7, 7, 256, 256);
        blit(pose, x, y + 7, 7, height, u + 0, 7, 7, 7, 256, 256);
        blit(pose, x + 7, y + 7, width, height, u + 7, 7, 7, 7, 256, 256);
        blit(pose, x + 7 + width, y + 7, 7, height, u + 14, 7, 7, 7, 256, 256);
        blit(pose, x, y + 7 + height, 7, 7, u + 0, 14, 7, 7, 256, 256);
        blit(pose, x + 7, y + 7 + height, width, 7, u + 7, 14, 7, 7, 256, 256);
        blit(pose, x + 7 + width, y + 7 + height, 7, 7, u + 14, 14, 7, 7, 256, 256);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    public static void renderFrame(PoseStack pose, int x, int y, int width, int height, Theme theme, int u) {
        RenderSystem.setShaderTexture(0, switch (theme) {
            case DARK -> WIDGETS_DARK;
            case LIGHT -> WIDGETS_LIGHT;
            default -> WIDGETS;
        });
        blit(pose, x, y, 7, 7, u + 0, 0, 7, 7, 256, 256);
        blit(pose, x + 7, y, width, 7, u + 7, 0, 7, 7, 256, 256);
        blit(pose, x + 7 + width, y, 7, 7, u + 14, 0, 7, 7, 256, 256);
        blit(pose, x, y + 7, 7, height, u + 0, 7, 7, 7, 256, 256);
        blit(pose, x + 7, y + 7, width, height, u + 7, 7, 7, 7, 256, 256);
        blit(pose, x + 7 + width, y + 7, 7, height, u + 14, 7, 7, 7, 256, 256);
        blit(pose, x, y + 7 + height, 7, 7, u + 0, 14, 7, 7, 256, 256);
        blit(pose, x + 7, y + 7 + height, width, 7, u + 7, 14, 7, 7, 256, 256);
        blit(pose, x + 7 + width, y + 7 + height, 7, 7, u + 14, 14, 7, 7, 256, 256);
    }
}
