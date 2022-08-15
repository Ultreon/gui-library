package com.ultreon.mods.guilib.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.ultreon.mods.guilib.UltreonGuiLib;
import com.ultreon.mods.guilib.client.gui.Clickable;
import com.ultreon.mods.guilib.client.gui.ReloadsTheme;
import com.ultreon.mods.guilib.client.gui.Theme;
import com.ultreon.mods.guilib.client.input.MouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToolbarButton extends ToolbarItem implements ReloadsTheme, Clickable {
    @Nullable
    private CommandCallback command;

    private final Object lock = new Object();
    private Theme theme;

    public ToolbarButton(int x, int y, int width, Component message) {
        this(x, y, width, message, null);
    }

    public ToolbarButton(int x, int y, int width, Component message, @Nullable CommandCallback command) {
        super(x, y, width, 20, message);
        this.command = command;
        reloadTheme();
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

    protected final ResourceLocation getWidgetsTexture() {
        return theme.getWidgetsTexture();
    }

    @Override
    public void reloadTheme() {
        theme = UltreonGuiLib.getTheme();
        setFGColor(theme.getButtonTextColor());
    }

    @Override
    public void renderButton(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, getWidgetsTexture());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHoveredOrFocused());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(pose, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        this.blit(pose, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBg(pose, minecraft, mouseX, mouseY);
        int j = getFGColor();
        drawCenteredString(pose, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);

        if (this.isHoveredOrFocused()) {
            this.renderToolTip(pose, mouseX, mouseY);
        }
    }

//    @Override
//    public boolean mouseClicked(double x, double y, int button) {
//        System.out.println("x = " + x + ", y = " + y + ", button = " + button);
//        if (isMouseOver(x, y) && button == MouseButton.LEFT) {
//            System.out.println("Clicked");
//            click();
//            return true;
//        }
//
//        System.out.println("Not over");
//
//        return super.mouseClicked(x, y, button);
//    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        click();
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
