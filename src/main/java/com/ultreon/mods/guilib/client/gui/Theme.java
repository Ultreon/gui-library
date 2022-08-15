package com.ultreon.mods.guilib.client.gui;

import com.ultreon.mods.guilib.UltreonGuiLib;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * The theme enum class.
 * For Vanilla theme use {@link #NORMAL}.
 */
public enum Theme {
    NORMAL("normal", new ResourceLocation("textures/gui/widgets.png")),
    LIGHT("light", UltreonGuiLib.res("textures/gui/widgets/main/light.png")),
    MIX("mix", UltreonGuiLib.res("textures/gui/widgets/main/light.png")),
    DARK("dark", UltreonGuiLib.res("textures/gui/widgets/main/dark.png"));

    private final String id;
    private final ResourceLocation widgetsTexture;

    Theme(String id,ResourceLocation widgetsTexture) {
        this.id = id;
        this.widgetsTexture = widgetsTexture;
    }

    /**
     * Gets the theme from the id.
     *
     * @param id the id of the theme.
     * @return the theme or null if not found.
     */
    @Nullable
    public static Theme fromId(String id) {
        for (Theme theme : values()) {
            if (theme.id.equals(id)) {
                return theme;
            }
        }

        // Not found
        return null;
    }

    /**
     * Gets the theme from the id.
     *
     * @param id the id of the theme.
     * @param defaultTheme the default theme to return if not found.
     * @return the theme or defaultTheme if not found.
     */
    @NonNull
    public static Theme fromIdOr(String id, Theme defaultTheme) {
        for (Theme theme : values()) {
            if (theme.id.equals(id)) {
                return theme;
            }
        }

        // Not found
        return defaultTheme;
    }

    /**
     * Gets the theme from the id.
     *
     * @param id the id of the theme.
     * @return the theme or the default theme if not found.
     */
    @NonNull
    public static Theme fromIdOrDefault(String id) {
        return fromIdOr(id, NORMAL);
    }

    /**
     * Get the theme's id.
     *
     * @return the theme's id.
     */
    public String id() {
        return id;
    }

    /**
     * Text color for inside the frame.
     * See {@link #getTitleColor()} for color of text in the title bar.
     *
     * @return the text color.
     */
    public int getTextColor() {
        return switch (this) {
            case LIGHT, MIX -> 0x555555;
            case DARK -> 0xFFFFFF;
            default -> 0x111111;
        };
    }

    /**
     * Text color for inside the title bar.
     * See {@link #getTextColor()} for color of text inside the frame.
     *
     * @return the text color in the title bar.
     */
    public int getTitleColor() {
        return switch (this) {
            case LIGHT -> 0x555555;
            case DARK, MIX -> 0xFFFFFF;
            default -> 0x111111;
        };
    }

    /**
     * Get the button text color.
     *
     * @return the button text color.
     */
    public int getButtonTextColor() {
        return switch (this) {
            case LIGHT, MIX -> 0xFFFFFF;
            case DARK -> 0xFFFFFF;
            default -> 0xFFFFFF;
        };
    }

    /**
     * Get the display name as a {@link Component chat component}.
     *
     * @return the display name.
     */
    @NonNull
    public Component getDisplayName() {
        return new TranslatableComponent(getDescriptionId());
    }

    /**
     * Get the translation's id for the display name.
     * @return the translation id.
     */
    @NonNull
    public String getDescriptionId() {
        return "ultreon.gui_lib.theme." + id;
    }

    /**
     * Get the theme after this one.
     *
     * @return the next theme.
     */
    public Theme next() {
        return Theme.values()[(ordinal() + 1) % Theme.values().length];
    }

    /**
     * Get the theme before this one.
     *
     * @return the previous theme.
     */
    public Theme previous() {
        return Theme.values()[(ordinal() - 1 + Theme.values().length) % Theme.values().length];
    }

    public ResourceLocation getWidgetsTexture() {
        return widgetsTexture;
    }
}
