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

public enum TitleStyle {
    HIDDEN(4, 0),
    NORMAL(16, 16),
    DETACHED(25, 20),
    ;

    public final int renderHeight;
    public final int height;

    TitleStyle(int renderHeight, int height) {

        this.renderHeight = renderHeight;
        this.height = height;
    }
}
