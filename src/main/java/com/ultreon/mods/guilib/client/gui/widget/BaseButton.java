package com.ultreon.mods.guilib.client.gui.widget;

import com.ultreon.mods.guilib.client.gui.Clickable;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class BaseButton extends Button implements Clickable {
    private long multiClickDelay = 500L;
    private long lastClickTime;
    private int clicks;

    public BaseButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress);
    }

    public BaseButton(int x, int y, int width, int height, Component message, OnPress onPress, OnTooltip onTooltip) {
        super(x, y, width, height, message, onPress, onTooltip);
    }

    public long getMultiClickDelay() {
        return multiClickDelay;
    }

    public void setMultiClickDelay(long multiClickDelay) {
        this.multiClickDelay = multiClickDelay;
    }

    private long getTimeSinceLastClick() {
        return this.lastClickTime - System.currentTimeMillis();
    }

    @Override
    public void click() {
        this.onPress.onPress(this);
    }

    public void click(int clicks) {
        click();
    }

    public final void doubleClick() {
        clicks = getClicks() + 2;
        lastClickTime = System.currentTimeMillis();
        click(clicks);
    }

    @Override
    public final void onPress() {
        clicks = getClicks() + 1;
        lastClickTime = System.currentTimeMillis();
        click(clicks);
    }

    @Override
    public final void onClick(double pMouseX, double pMouseY) {
        super.onClick(pMouseX, pMouseY);
    }

    public final int getClicks() {
        long timeSinceLastClick = getTimeSinceLastClick();
        clicks = timeSinceLastClick < multiClickDelay ? clicks : 0;
        return clicks;
    }
}
