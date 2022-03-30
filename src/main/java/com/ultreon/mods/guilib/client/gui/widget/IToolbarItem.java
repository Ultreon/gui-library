package com.ultreon.mods.guilib.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Widget;
import org.jetbrains.annotations.NotNull;

public interface IToolbarItem extends Widget {
    @Override
    void render(@NotNull PoseStack pose, int mouseX, int mouseY, float frameTime);
}
