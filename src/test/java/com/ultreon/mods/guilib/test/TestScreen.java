package com.ultreon.mods.guilib.test;

import com.mojang.datafixers.util.Pair;
import com.ultreon.mods.guilib.client.gui.screen.GenericMenuScreen;
import com.ultreon.mods.guilib.client.gui.screen.TitleStyle;
import com.ultreon.mods.guilib.client.gui.widget.ButtonMenuItem;
import com.ultreon.mods.guilib.client.gui.widget.ContextMenu;
import com.ultreon.mods.guilib.client.gui.widget.List;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;

public class TestScreen extends GenericMenuScreen {
    private final Screen back;
    private final Button buttonRowBtn;
    private final Pair<Button, Button> leftAndRightBtn;
    private final Button fullScreenBtn;

    protected TestScreen(Minecraft minecraft, Screen back) {
        super(new Properties().titleText("Title").titleStyle(TitleStyle.DETACHED).back(back));

        this.back = back;
        this.minecraft = minecraft;

        List list = addListRow();
        list.setAddEntries($ -> list.addEntry(() -> new ResourceLocation("textures/item/diamond_sword.png"), 0, 0, 16, 16, 16, 16, "Title", "Description"));
        buttonRowBtn = addButtonRow(new TextComponent("Button Row"));
        leftAndRightBtn = addButtonRow(new TextComponent("Button Left"), new TextComponent("Button Right"));
        fullScreenBtn = addButtonRow(new TextComponent("FULL SCREEN"), () -> {
            minecraft.setScreen(new TestFullScreen(minecraft, back));
        });
        addLabel("Label Text");
    }

    @Override
    protected ContextMenu getContextMenu(int x, int y) {
        ContextMenu contextMenu = super.getContextMenu(x, y);
        if (fullScreenBtn.isMouseOver(x, y)) {
            contextMenu = new ContextMenu(x, y, new TextComponent("LOL"));
            ButtonMenuItem openGoogle = contextMenu.add(new ButtonMenuItem(contextMenu, new TextComponent("Open Google"), btn -> {
                try {
                    Util.getPlatform().openUrl(new URL("https://www.google.com"));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }));
        } else {
            contextMenu = new ContextMenu(x, y, new TextComponent("Randomization.exe"));
            long id = new Random(new Random(x).nextLong() * new Random(y).nextLong()).nextLong();
            ButtonMenuItem openGoogle = contextMenu.add(new ButtonMenuItem(contextMenu, new TextComponent("Search " + id), btn -> {
                try {
                    Util.getPlatform().openUrl(new URL("https://www.google.com/search?q=" + id));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        return contextMenu;
    }

    @Override
    public void onClose() {
        Objects.requireNonNull(minecraft).setScreen(back);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
