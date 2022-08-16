package com.ultreon.mods.guilib.client.gui.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;

public class VanillaScreen extends BaseScreen {
    protected VanillaScreen(Component title) {
        super(title);
    }

    @Override
    public @Nullable Vec2 getCloseButtonPos() {
        return null;
    }
}
