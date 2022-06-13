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

import com.ultreon.mods.guilib.client.gui.Theme;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

public enum TitleStyle {
    HIDDEN(4, 0, "hidden"),
    NORMAL(16, 16, "normal"),
    DETACHED(25, 20, "detached"),
    ;

    public final int renderHeight;
    public final int height;
    private final String name;

    TitleStyle(int renderHeight, int height, String name) {

        this.renderHeight = renderHeight;
        this.height = height;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @NonNull
    public Component getDisplayName() {
        return new TranslatableComponent(getDescriptionId());
    }

    @NonNull
    private String getDescriptionId() {
        return "ultreon.gui_lib.title_style." + name;
    }

    public TitleStyle next() {
        return TitleStyle.values()[(ordinal() + 1) % TitleStyle.values().length];
    }

    public TitleStyle previous() {
        return TitleStyle.values()[(ordinal() - 1 + Theme.values().length) % TitleStyle.values().length];
    }
}
