package com.ultreon.mods.guilib.test;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ultreon.mods.guilib.client.gui.screen.FullscreenRenderScreen;
import com.ultreon.mods.guilib.client.gui.widget.ToolbarButton;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class TestFullScreen extends FullscreenRenderScreen {
    private final Screen back;
    private final ToolbarButton openButton;

    protected TestFullScreen(Minecraft minecraft, Screen back) {
        super(new TextComponent("LOL"));

        this.back = back;
        this.minecraft = minecraft;

        openButton = addToolbarItem(new ToolbarButton(0, 0, 60, new TextComponent("Open"), button -> {
            try {
                Util.getPlatform().openUrl(new URL("https://www.google.com/search?q=Wikipedia+GUI"));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }));

//        List list = addUserListRow();
//        list.addEntry(() -> new ResourceLocation("texture/item/diamond_sword"), 0, 0, 16, 16, 16, 16, "Title", "Description");
//        addButtonRow(new TextComponent("Button Row"));
//        addButtonRow(new TextComponent("Button Left"), new TextComponent("Button Right"));
//        addLabel("Label Text");
    }

    @Override
    public void onClose() {
        Objects.requireNonNull(minecraft).setScreen(back);
    }

    @Override
    public void renderBackground(@NotNull PoseStack pose) {
        renderBackground(pose, 0);
    }
}
