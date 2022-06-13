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

import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ButtonMenuItem extends AbstractButtonMenuItem {
    private final OnPress onPress;

    public ButtonMenuItem(ContextMenu menu, Component message) {
        this(menu, message, btn -> {
        });
    }

    public ButtonMenuItem(ContextMenu menu, Component message, OnPress onPress) {
        super(menu, message);
        this.onPress = onPress;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narrator) {
        this.defaultButtonNarrationText(narrator);
    }

    @Override
    protected void onPress() {
        this.onPress.run(this);
    }

    @FunctionalInterface
    public interface OnPress {
        void run(ButtonMenuItem menuItem);
    }
}
