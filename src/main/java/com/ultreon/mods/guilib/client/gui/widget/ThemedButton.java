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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public non-sealed class ThemedButton extends TexturedButton {
    private final Type type;

    public ThemedButton(int x, int y, int width, int height, Component title, OnPress pressedAction, Type type) {
        super(x, y, width, height, title, pressedAction);
        this.type = type;
    }

    public ThemedButton(int x, int y, int width, int height, Component title, OnPress pressedAction, OnTooltip onTooltip, Type type) {
        super(x, y, width, height, title, pressedAction, onTooltip);
        this.type = type;
    }

    public ThemedButton(Component title, OnPress pressedAction, Type type) {
        super(0, 0, 0, 0, title, pressedAction);
        this.type = type;
    }

    public ThemedButton(Component title, OnPress pressedAction, OnTooltip onTooltip, Type type) {
        super(0, 0, 0, 0, title, pressedAction, onTooltip);
        this.type = type;
    }

    @Override
    protected final ResourceLocation getWidgetsTexture() {
        return type.res;
    }

    public enum Type {
        NORMAL(new ResourceLocation("textures/gui/widgets.png")),
        DARK(UltreonGuiLib.res("textures/gui/widgets/dark.png")),
        PRIMARY(UltreonGuiLib.res("textures/gui/widgets/primary.png")),
        SUCCESS(UltreonGuiLib.res("textures/gui/widgets/success.png")),
        WARNING(UltreonGuiLib.res("textures/gui/widgets/warning.png")),
        DANGER(UltreonGuiLib.res("textures/gui/widgets/danger.png")),
        ;

        private final ResourceLocation res;

        Type(ResourceLocation res) {

            this.res = res;
        }
    }
}
