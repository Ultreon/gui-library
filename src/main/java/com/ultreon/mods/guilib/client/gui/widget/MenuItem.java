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

import net.minecraft.network.chat.Component;

public abstract class MenuItem extends BaseWidget {
    private final ContextMenu menu;
    private int minWidth = 0;
    private int maxWidth = Integer.MAX_VALUE;

    public MenuItem(int height, ContextMenu menu, Component message) {
        super(menu.x, menu.y, menu.getWidth(), height, message);
        this.menu = menu;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        menu.invalidateSize();
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public ContextMenu getMenu() {
        return menu;
    }
}
