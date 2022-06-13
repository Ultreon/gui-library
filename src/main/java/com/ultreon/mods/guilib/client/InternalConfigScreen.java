package com.ultreon.mods.guilib.client;

import com.ultreon.mods.guilib.UltreonGuiLib;
import com.ultreon.mods.guilib.client.gui.Theme;
import com.ultreon.mods.guilib.client.gui.screen.GenericMenuScreen;
import com.ultreon.mods.guilib.client.gui.screen.TitleStyle;
import com.ultreon.mods.guilib.util.KeyboardHelper;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class InternalConfigScreen extends GenericMenuScreen {

    public InternalConfigScreen(Screen back) {
        super(new Properties().titleLang("gui.ultreon.gui_lib.config.title").back(back));

        addButtonRow(new TranslatableComponent("gui.ultreon.gui_lib.config.theme").append(UltreonGuiLib.getTheme().getDisplayName()), this::changeTheme);
        addButtonRow(new TranslatableComponent("gui.ultreon.gui_lib.config.title_style").append(UltreonGuiLib.getTitleStyle().getDisplayName()), this::changeTitleStyle);
    }

    private void changeTheme(Button button) {
        Theme theme = UltreonGuiLib.getTheme();
        theme = KeyboardHelper.isShiftDown() ? theme.previous() : theme.next();
        UltreonGuiLib.setTheme(theme);
        System.out.println("theme.getDescriptionId() = " + theme.getDescriptionId());
        button.setMessage(new TranslatableComponent("gui.ultreon.gui_lib.config.theme").append(theme.getDisplayName()));
    }

    private void changeTitleStyle(Button button) {
        TitleStyle titleStyle = UltreonGuiLib.getTitleStyle();
        titleStyle = KeyboardHelper.isShiftDown() ? titleStyle.previous() : titleStyle.next();
        UltreonGuiLib.setTitleStyle(titleStyle);
        button.setMessage(new TranslatableComponent("gui.ultreon.gui_lib.config.title_style").append(UltreonGuiLib.getTitleStyle().getDisplayName()));
    }

    @Override
    protected void init() {
        super.init();
    }
}
