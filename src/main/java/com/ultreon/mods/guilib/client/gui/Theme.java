package com.ultreon.mods.guilib.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public enum Theme {
    NORMAL("normal"),
    LIGHT("light"),
    MIX("mix"),
    DARK("dark");

    private final String id;

    Theme(String id) {
        this.id = id;
    }

    @Nullable
    public static Theme fromId(String id) {
        for (Theme theme : values()) {
            if (theme.id.equals(id)) {
                return theme;
            }
        }

        return null;
    }

    @NonNull
    public static Theme fromIdOr(String id, Theme defaultTheme) {
        for (Theme theme : values()) {
            if (theme.id.equals(id)) {
                return theme;
            }
        }

        return defaultTheme;
    }

    @NonNull
    public static Theme fromIdOrDefault(String id) {
        return fromIdOr(id, NORMAL);
    }

    public String id() {
        return id;
    }

    public int getTextColor() {
        return switch (this) {
            case LIGHT, MIX -> 0x555555;
            case DARK -> 0xFFFFFF;
            default -> 0x111111;
        };
    }

    public int getTitleColor() {
        return switch (this) {
            case LIGHT -> 0x555555;
            case DARK, MIX -> 0xFFFFFF;
            default -> 0x111111;
        };
    }

    public int getButtonTextColor() {
        return switch (this) {
            case LIGHT, MIX -> 0xFFFFFF;
            case DARK -> 0xFFFFFF;
            default -> 0xFFFFFF;
        };
    }

    @NonNull
    public Component getDisplayName() {
        return new TranslatableComponent(getDescriptionId());
    }

    @NonNull
    public String getDescriptionId() {
        return "ultreon.gui_lib.theme." + id;
    }

    public Theme next() {
        return Theme.values()[(ordinal() + 1) % Theme.values().length];
    }

    public Theme previous() {
        return Theme.values()[(ordinal() - 1 + Theme.values().length) % Theme.values().length];
    }
}
