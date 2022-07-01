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

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;

public class LabelItem extends MenuItem {
    public LabelItem(ContextMenu menu, Component message) {
        super(13, menu, message);
    }

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        this.font.drawShadow(pose, getMessage(), x, y + 6 - font.lineHeight / 2f, getFGColor());
        if (isHoveredOrFocused()) {
            int x1 = x + width;
            int y1 = y + height;
            fill(pose, x, y, x1, y, getFGColor());   // top
            fill(pose, x, y1, x1, y1, getFGColor()); // bottom
            fill(pose, x, y, x, y1, getFGColor());   // left
            fill(pose, x1, y, x1, y1, getFGColor()); // right
        }
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narrator) {
        narrator.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                narrator.add(NarratedElementType.USAGE, new TranslatableComponent("narration.button.usage.focused"));
            } else {
                narrator.add(NarratedElementType.USAGE, new TranslatableComponent("narration.button.usage.hovered"));
            }
        }
    }
}
