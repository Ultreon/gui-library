package com.ultreon.mods.guilib.test;

import com.ultreon.mods.guilib.client.gui.screen.GenericMenuScreen;
import com.ultreon.mods.guilib.client.gui.screen.TitleStyle;
import com.ultreon.mods.guilib.client.gui.widget.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class TestScreen extends GenericMenuScreen {
    private final Screen back;

    protected TestScreen(Minecraft minecraft, Screen back) {
        super(new Properties().titleText("Title").titleStyle(TitleStyle.DETACHED).back(back));

        this.back = back;
        this.minecraft = minecraft;

        List list = addUserListRow();
        list.addEntry(() -> new ResourceLocation("texture/item/diamond_sword"), 0, 0, 16, 16, 16, 16, "Title", "Description");
        addButtonRow(new TextComponent("Button Row"));
        addButtonRow(new TextComponent("Button Left"), new TextComponent("Button Right"));
        addLabel("Label Text");
    }

    @Override
    public void onClose() {
        Objects.requireNonNull(minecraft).setScreen(back);
    }
}
