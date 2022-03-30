package com.ultreon.mods.guilib.client;

import com.ultreon.mods.guilib.client.gui.widget.ContextMenu;
import org.jetbrains.annotations.Nullable;

public interface HasContextMenu {
    @Nullable ContextMenu contextMenu(int mouseX, int mouseY, int button);
}
