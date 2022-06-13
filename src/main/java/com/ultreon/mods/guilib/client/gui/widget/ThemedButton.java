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

import com.ultreon.mods.guilib.UltreonGuiLib;
import com.ultreon.mods.guilib.client.gui.ReloadsTheme;
import com.ultreon.mods.guilib.client.gui.Theme;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public non-sealed class ThemedButton extends TexturedButton implements ReloadsTheme {
    private Type type;

    public ThemedButton(int x, int y, int width, int height, Component title, OnPress pressedAction, Type type) {
        super(x, y, width, height, title, pressedAction);
        this.type = type;
        setFGColor(type.fgColor);
    }

    public ThemedButton(int x, int y, int width, int height, Component title, OnPress pressedAction, OnTooltip onTooltip, Type type) {
        super(x, y, width, height, title, pressedAction, onTooltip);
        this.type = type;
        setFGColor(type.fgColor);
    }

    public ThemedButton(Component title, OnPress pressedAction, Type type) {
        super(0, 0, 0, 0, title, pressedAction);
        this.type = type;
        setFGColor(type.fgColor);
    }

    public ThemedButton(Component title, OnPress pressedAction, OnTooltip onTooltip, Type type) {
        super(0, 0, 0, 0, title, pressedAction, onTooltip);
        this.type = type;
        setFGColor(type.fgColor);
    }

    @Override
    protected final ResourceLocation getWidgetsTexture() {
        return type.res;
    }

    @Override
    public void reloadTheme() {
        if (type.usesTheme()) {
            Theme theme = UltreonGuiLib.getTheme();
            this.type = Type.of(theme);
            setFGColor(type.fgColor);
        }
    }

    public enum Type {
        LIGHT(UltreonGuiLib.res("textures/gui/widgets/main/light.png"), Theme.LIGHT.getButtonTextColor()),
        NORMAL(new ResourceLocation("textures/gui/widgets.png"), Theme.NORMAL.getButtonTextColor()),
        DARK(UltreonGuiLib.res("textures/gui/widgets/main/dark.png"), Theme.DARK.getButtonTextColor()),
        PRIMARY(UltreonGuiLib.res("textures/gui/widgets/main/primary.png"), Theme.NORMAL.getButtonTextColor()),
        SUCCESS(UltreonGuiLib.res("textures/gui/widgets/main/success.png"), Theme.NORMAL.getButtonTextColor()),
        WARNING(UltreonGuiLib.res("textures/gui/widgets/main/warning.png"), Theme.NORMAL.getButtonTextColor()),
        DANGER(UltreonGuiLib.res("textures/gui/widgets/main/danger.png"), Theme.NORMAL.getButtonTextColor()),
        ;

        private final ResourceLocation res;
        private final int fgColor;

        Type(ResourceLocation res) {
            this(res, 0xffffff);
        }

        Type(ResourceLocation res, int fgColor) {
            this.res = res;
            this.fgColor = fgColor;
        }

        public boolean usesTheme() {
            return this == LIGHT || this == DARK || this == NORMAL;
        }

        public static Type of(Theme theme) {
            return switch (theme) {
                case LIGHT, MIX -> LIGHT;
                case DARK -> DARK;
                default -> NORMAL;
            };
        }
    }
}
