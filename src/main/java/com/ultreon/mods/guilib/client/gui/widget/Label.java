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

import com.mojang.blaze3d.vertex.PoseStack;
import com.ultreon.mods.guilib.client.gui.ReloadsTheme;
import com.ultreon.mods.guilib.client.gui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public class Label implements Widget, ReloadsTheme {
    protected final Minecraft minecraft;
    protected final Font font;
    public int y;
    public int x;
    private Component message;
    private boolean shadow;
    private int color;
    private Theme theme;

    public Label(int x, int y, Component message, Theme theme) {
        this(x, y, message, false, theme);
    }

    public Label(int x, int y, Component message, boolean shadow, Theme theme) {
        this.x = x;
        this.y = y;
        this.theme = theme;
        this.color = theme.getTextColor();
        this.message = message;
        this.minecraft = Minecraft.getInstance();
        this.font = minecraft.font;
        this.shadow = shadow;
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        if (shadow) {
            this.font.drawShadow(pose, message, x, y, getColor());
        } else {
            this.font.draw(pose, message, x, y, getColor());
        }
    }

    public Component getMessage() {
        return message;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean hasShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public void setMessage(String s) {
        setMessage(new TextComponent(s));
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
        this.color = theme.getTextColor();
    }

    @Override
    public void reloadTheme() {
        this.color = theme.getTextColor();
    }
}
