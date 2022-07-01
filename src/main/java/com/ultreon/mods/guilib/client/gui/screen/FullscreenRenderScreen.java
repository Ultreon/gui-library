package com.ultreon.mods.guilib.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ultreon.mods.guilib.Config;
import com.ultreon.mods.guilib.client.gui.widget.IToolbarItem;
import com.ultreon.mods.guilib.client.gui.widget.ToolbarItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class FullscreenRenderScreen extends BaseScreen {
    private static final int ITEM_PADDING = 2;
    private static final int BOTTOM_MARGIN = 12;
    private final List<IToolbarItem> items = new ArrayList<>();

    protected FullscreenRenderScreen(Component title) {
        super(title);
    }

    @Override
    public abstract void renderBackground(@NotNull PoseStack pose);

    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        renderBackground(pose);

        renderToolbar(pose, mouseX, mouseY, partialTicks);

        super.render(pose, mouseX, mouseY, partialTicks);
    }

    private void renderToolbar(PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        final int paddings = ITEM_PADDING * Math.max(items.size() - 1, 0);
        final int width = items.stream().mapToInt(IToolbarItem::width).sum() + paddings;
        final int height = items.stream().mapToInt(IToolbarItem::height).max().orElse(0);
        final int frameWidth = width - 4;
        final int frameHeight = height - 4;
        final int frameX = this.width / 2 - ((frameWidth) / 2);
        final int frameY = this.height - frameHeight - BOTTOM_MARGIN - 14;
        renderFrame(pose, frameX, frameY, frameWidth, frameHeight, Config.THEME.get());
        int x = frameX + 5;
        final int y = frameY + 2 + 7 - 5;
        for (IToolbarItem item : items) {
            int width1 = item.width();
            if (item instanceof ToolbarItem toolbarItem) {
                toolbarItem.x = x;
                toolbarItem.y = y;
            }
            item.render(pose, mouseX, mouseY, partialTicks);

            x += width1;
        }
    }

    public <T extends IToolbarItem> T addToolbarItem(T t) {
        items.add(t);
        if (t instanceof ToolbarItem toolbarItem) {
            addWidget(toolbarItem);
        }
        return t;
    }

    @Override
    public Vec2 getCloseButtonPos() {
        return new Vec2(width - 6 - 3 - 5, 6 + 3);
    }
}
