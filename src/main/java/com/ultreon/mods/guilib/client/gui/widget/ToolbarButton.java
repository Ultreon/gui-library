package com.ultreon.mods.guilib.client.gui.widget;

import com.ultreon.mods.guilib.client.input.MouseButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToolbarButton extends ToolbarItem {
    @Nullable
    private CommandCallback command;

    private final Object lock = new Object();

    public ToolbarButton(int x, int y, int width, Component message) {
        this(x, y, width, message, null);
    }

    public ToolbarButton(int x, int y, int width, Component message, @Nullable CommandCallback command) {
        super(x, y, width, message);
        this.command = command;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narration) {
        narration.add(NarratedElementType.TITLE, getMessage());
    }

    public void click() {
        synchronized (lock) {
            if (command != null) {
                command.call(this);
            }
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (isMouseOver(x, y) && button == MouseButton.LEFT) {
            click();
            return true;
        }

        return super.mouseClicked(x, y, button);
    }

    public void setCommand(@Nullable CommandCallback command) {
        synchronized (lock) {
            this.command = command;
        }
    }

    @FunctionalInterface
    public interface CommandCallback {
        void call(ToolbarButton button);
    }
}
