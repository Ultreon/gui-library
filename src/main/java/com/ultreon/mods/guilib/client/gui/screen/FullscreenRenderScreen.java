package com.ultreon.mods.guilib.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ultreon.mods.guilib.client.gui.widget.IToolbarItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

public class FullscreenRenderScreen extends BaseScreen {
    protected FullscreenRenderScreen(Component title) {
        super(title);
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float frameTime) {
        super.render(pose, mouseX, mouseY, frameTime);
    }

    public <T extends IToolbarItem> T addToolbarItem(T t) {
        return t;
    }

    @Override
    public Vec2 getCloseButtonPos() {
        return new Vec2(width - 6 - 3 - 5, 6 + 3);
    }
}
