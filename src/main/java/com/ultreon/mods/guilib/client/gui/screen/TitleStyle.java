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

/**
 * The title style enum class.
 * See {@link #NORMAL} for the default one.
 *
 * @author Qboi123
 */
public enum TitleStyle {
    /**
     * The title bar isn't shown, including the title.
     */
    HIDDEN(4, 0, "hidden"),

    /**
     * This is similar to the vanilla's style for container screens.
     * The title bar is merged with the frame.
     */
    NORMAL(16, 16, "normal"),

    /**
     * Title bar is shown, and the title is shown.
     * It's also looks detached from the frame.
     */
    DETACHED(25, 20, "detached"),
    ;

    /**
     * The title bar height for the texture.
     */
    public final int renderHeight;

    /**
     * The title bar widget height, for boundaries of the widgets in the title bar.
     */
    public final int height;

    /**
     * The style's id name.
     */
    private final String id;

    TitleStyle(int renderHeight, int height, String id) {

        this.renderHeight = renderHeight;
        this.height = height;
        this.id = id;
    }

    /**
     * Gets the id of the title style.
     * @return the id of the title style.
     */
    public String id() {
        return id;
    }

    /**
     * Get the display name of the title style.
     *
     * @return the display name.
     */
    @NonNull
    public Component getDisplayName() {
        return new TranslatableComponent(getDescriptionId());
    }

    /**
     * Get the translation id for the name.
     * @return the translation id.
     */
    @NonNull
    private String getDescriptionId() {
        return "ultreon.gui_lib.title_style." + id;
    }

    /**
     * Get the title style after this one.
     *
     * @return the next title style.
     */
    public TitleStyle next() {
        return TitleStyle.values()[(ordinal() + 1) % TitleStyle.values().length];
    }

    /**
     * Get the title style before this one.
     *
     * @return the previous title style.
     */
    public TitleStyle previous() {
        return TitleStyle.values()[(ordinal() - 1 + Theme.values().length) % TitleStyle.values().length];
    }
}
