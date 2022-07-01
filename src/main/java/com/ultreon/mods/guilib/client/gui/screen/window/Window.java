package com.ultreon.mods.guilib.client.gui.screen.window;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ultreon.mods.guilib.UltreonGuiLib;
import com.ultreon.mods.guilib.client.gui.Anchor;
import com.ultreon.mods.guilib.client.gui.ReloadsTheme;
import com.ultreon.mods.guilib.client.gui.Theme;
import com.ultreon.mods.guilib.client.gui.screen.BaseScreen;
import com.ultreon.mods.guilib.client.gui.widget.BaseContainerWidget;
import com.ultreon.mods.guilib.common.Scale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

/**
 * Window widget class.
 * Title height is 21 pixels.
 * Content height is the height of the window minus the title height.
 * Border size is 5 pixels.
 * The border is outside the content area.
 * The content area is defined by the x, y, width and height properties.
 *
 * @author Qboi123
 * @version 1.0
 * @since 0.0.1.6
 */
public class Window extends BaseContainerWidget implements ReloadsTheme {
    private static final String CLOSE_ICON = "\u00D7";
    private static final String CLOSE_ICON_HOVER = ChatFormatting.RED + CLOSE_ICON;

    private Component title = TextComponent.EMPTY;
    private boolean valid = true;
    private boolean visible;
    private final Minecraft minecraft = Minecraft.getInstance();
    private Theme theme = UltreonGuiLib.getTheme();
    private boolean customThemeEnabled;
    private boolean resizing;
    private double dragX;
    private double dragY;
    private double resizeX;
    private double resizeY;
    private boolean resizable;
    private Anchor currentResizingBorder;
    private final Font font = Minecraft.getInstance().font;

    private final int titleHeight = 21;
    private final int borderSize = 5;
    private final Scale titleTextScale = Scale.of(1f);

    private final WindowManager wm = WindowManager.INSTANCE;

    /**
     * Constructs a new window.
     *
     * @param x      The x position of the window
     * @param y      The y position of the window
     * @param width  The width of the window
     * @param height The height of the window
     */
    public Window(int x, int y, int width, int height) {
        super(x, y, width, height, TextComponent.EMPTY);
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, getTitle());
    }

    /**
     * Shows the window.
     */
    public void show() {
        checkValid();
        wm.addWindow(this);
        visible = true;
    }

    private void checkValid() {
        if (!valid) {
            throw new IllegalStateException("Window is not valid, probably destroyed before.");
        }
    }

    /**
     * Hide the window.
     *
     * @throws IllegalStateException If the window is not shown
     */
    public void hide() {
        checkValid();
        visible = false;
    }

    /**
     * Gets the title of the window.
     *
     * @return The title of the window.
     * @throws IllegalStateException If the window is not shown
     */
    public Component getTitle() {
        checkValid();
        return title;
    }

    /**
     * Sets the title of the window.
     *
     * @param title The title to set.
     * @throws IllegalStateException If the window is not shown
     */
    public void setTitle(Component title) {
        checkValid();
        this.title = title;
    }

    /**
     * Closes the window.
     *
     * @throws IllegalStateException If the window is not shown
     */
    public void close(boolean force) {
        if (force) {
            checkValid();
        }
        if (onClose() || force) {
            destroy();
        }
    }

    /**
     * Called when the window is being closed.
     *
     * @return true if the window should be destroyed, false if it should be kept open.
     */
    public boolean onClose() {
        return true;
    }

    /**
     * Destroys the window.
     * Calling this method will remove the window from the screen.
     * This method is called automatically when the window is closed.
     * After calling this method, other methods may throw an {@link IllegalStateException}.
     * Check {@link #isValid()} to see if the window is still valid.
     *
     * @return true if the window was destroyed.
     * If the window is invalid or already destroyed, this method will return false.
     * @see #isValid()
     */
    public final boolean destroy() {
        if (valid) {
            this.invalidate();
            return true;
        }
        return false;
    }

    private void invalidate() {
        this.valid = false;
    }

    /**
     * Get whether the window is valid.
     *
     * @return true if the window is valid, false if it was destroyed.
     * @throws IllegalStateException if the window is not valid.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Closes the window.
     *
     * @throws IllegalStateException if the window is not valid.
     */
    public void close() {
        checkValid();
        close(false);

        wm.removeWindow(this);
    }

    /**
     * Renders the window including the border and title.
     *
     * @param pose         The pose-stack of the window.
     * @param mouseX       The x position of the mouse
     * @param mouseY       The y position of the mouse
     * @param partialTicks The partial ticks
     * @throws IllegalStateException If the window is not valid.
     */
    @Override
    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        // Check for valid window.
        checkValid();

        if (isVisible()) {
            // Window is shown, so render it.
            renderTitle(pose, mouseX, mouseY, partialTicks);
            renderFrame(pose, mouseX, mouseY, partialTicks);
            renderContents(pose, mouseX, mouseY, partialTicks);
        }
    }

    /**
     * Render the content area.
     *
     * @param pose         The pose-stack to render with.
     * @param mouseX       The x position of the mouse.
     * @param mouseY       The y position of the mouse.
     * @param partialTicks The partial ticks.
     */
    private void renderContents(PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        for (GuiEventListener widget : children()) {
            if (widget instanceof Widget) {
                ((AbstractWidget) widget).render(pose, mouseX, mouseY, partialTicks);
            }
        }
    }

    /**
     * Renders the title bar.
     *
     * @param pose         The pose-stack to render with.
     * @param mouseX       The x position of the mouse.
     * @param mouseY       The y position of the mouse.
     * @param partialTicks The partial ticks.
     */
    private void renderTitle(PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        BaseScreen.renderTitleFrame(pose, x, y - 20, width, 12, theme);
        drawCenteredString(pose, minecraft.font, getTitle(), x + width / 2, y - 12, theme.getTitleColor());

        renderCloseButton(pose, mouseX, mouseY, partialTicks, x + width - 12, y - 12);
    }

    /**
     * Renders the close button.
     *
     * @param pose         The pose-stack to render with.
     * @param mouseX       The x position of the mouse.
     * @param mouseY       The y position of the mouse.
     * @param partialTicks The partial ticks.
     * @param x            The x position of the close button.
     * @param y            The y position of the close button.
     */
    private void renderCloseButton(PoseStack pose, int mouseX, int mouseY, float partialTicks, int x, int y) {
        if (isMouseOver(mouseX, mouseY, x, y, 12, 12)) {
            drawCenteredString(pose, minecraft.font, CLOSE_ICON_HOVER, x + 6, y + 6, theme.getTitleColor());
        } else {
            drawCenteredString(pose, minecraft.font, CLOSE_ICON, x + 6, y + 6, theme.getTitleColor());
        }
    }

    /**
     * Get whether the mouse is over a certain area.
     *
     * @param mouseX The x position of the mouse
     * @param mouseY The y position of the mouse
     * @param x      The x position of the area
     * @param y      The y position of the area
     * @param width  The width of the area
     * @param height The height of the area
     * @return true if the mouse is over the area, false if it is not.
     */
    private boolean isMouseOver(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public final Vec2 getCloseButtonOffset() {
        int iconX = (x + width) - 9 - 5;
        int iconY = (y - 21) + 1 + (int) (((25 - 6) / 2f - font.lineHeight / 2f));
        return new Vec2(iconX, iconY);
    }

    private void renderFrame(PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        BaseScreen.renderFrame(pose, getX(), getY(), getWidth(), getHeight(), UltreonGuiLib.getTheme());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void reloadTheme() {
        if (!customThemeEnabled) {
            theme = UltreonGuiLib.getTheme();
        }
    }

    public boolean isCustomThemeEnabled() {
        return customThemeEnabled;
    }

    public void setCustomThemeEnabled(boolean customThemeEnabled) {
        synchronized (this) {
            this.customThemeEnabled = customThemeEnabled;
            if (!customThemeEnabled) {
                theme = UltreonGuiLib.getTheme();
            }
        }
    }

    public void setCustomTheme(Theme theme) {
        this.theme = theme;
        this.customThemeEnabled = true;
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        // Check for left mouse button being down.
        if (GLFW.glfwGetMouseButton(minecraft.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS) {
            // Dragging something.
            if (isVisible()) {
                // Window is shown, so we can drag it.
                if (isDragging()) {
                    // The window is being dragged, so move it.
                    setX(getX() + (int) (pMouseX - getDragX()));
                    setY(getY() + (int) (pMouseY - getDragY()));
                } else if (isResizing() && isResizable() && currentResizingBorder != null) {
                    // Resize the window based on the current resizing border
                    // The border is the anchor that is currently being resized
                    // The border is an anchor object.
                    switch (currentResizingBorder) {
                        case TOP_LEFT -> {
                            setX(getX() + (int) (pMouseX - getDragX()));
                            setY(getY() + (int) (pMouseY - getDragY()));
                            setWidth(getWidth() - (int) (pMouseX - getDragX()));
                            setHeight(getHeight() - (int) (pMouseY - getDragY()));
                        }
                        case TOP_CENTER -> {
                            setY(getY() + (int) (pMouseY - getDragY()));
                            setHeight(getHeight() - (int) (pMouseY - getDragY()));
                        }
                        case TOP_RIGHT -> {
                            setWidth(getWidth() + (int) (pMouseX - getDragX()));
                            setY(getY() + (int) (pMouseY - getDragY()));
                            setHeight(getHeight() - (int) (pMouseY - getDragY()));
                        }
                        case BOTTOM_LEFT -> {
                            setX(getX() + (int) (pMouseX - getDragX()));
                            setWidth(getWidth() - (int) (pMouseX - getDragX()));
                            setHeight(getHeight() + (int) (pMouseY - getDragY()));
                        }
                        case BOTTOM_CENTER -> {
                            setHeight(getHeight() + (int) (pMouseY - getDragY()));
                        }
                        case BOTTOM_RIGHT -> {
                            setWidth(getWidth() + (int) (pMouseX - getDragX()));
                            setHeight(getHeight() + (int) (pMouseY - getDragY()));
                        }
                        case MIDDLE_LEFT -> {
                            setX(getX() + (int) (pMouseX - getDragX()));
                            setWidth(getWidth() - (int) (pMouseX - getDragX()));
                        }
                        case MIDDLE_RIGHT -> {
                            setWidth(getWidth() + (int) (pMouseX - getDragX()));
                        }
                    }
                }

                if (isOnTopOfTitle(pMouseX, pMouseY)) {
                    // If the mouse is on the title, start dragging the window.
                    setDragging(true);
                    setDragX(pMouseX);
                    setDragY(pMouseY);
                } else {
                    Anchor resizingBorder = findResizeBorder(pMouseX, pMouseY);
                    if (resizingBorder != null) {
                        // If the mouse is on a resize border, start resizing the window.
                        setResizing(true);
                        currentResizingBorder = resizingBorder;
                    }
                }
            }
        }
        super.mouseMoved(pMouseX, pMouseY);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if (pButton == 0) {
            setDragging(false);
            setResizing(false);
            currentResizingBorder = null;
            return true;
        }

        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    private Anchor findResizeBorder(double x, double y) {
        if (isOnTopOfBorder(x, y, Anchor.TOP_LEFT)) {
            return Anchor.TOP_LEFT;
        } else if (isOnTopOfBorder(x, y, Anchor.TOP_CENTER)) {
            return Anchor.TOP_CENTER;
        } else if (isOnTopOfBorder(x, y, Anchor.TOP_RIGHT)) {
            return Anchor.TOP_RIGHT;
        } else if (isOnTopOfBorder(x, y, Anchor.BOTTOM_LEFT)) {
            return Anchor.BOTTOM_LEFT;
        } else if (isOnTopOfBorder(x, y, Anchor.BOTTOM_CENTER)) {
            return Anchor.BOTTOM_CENTER;
        } else if (isOnTopOfBorder(x, y, Anchor.BOTTOM_RIGHT)) {
            return Anchor.BOTTOM_RIGHT;
        } else if (isOnTopOfBorder(x, y, Anchor.MIDDLE_LEFT)) {
            return Anchor.MIDDLE_LEFT;
        } else if (isOnTopOfBorder(x, y, Anchor.MIDDLE_RIGHT)) {
            return Anchor.MIDDLE_RIGHT;
        }

        // The mouse isn't on a resize border.
        return null;
    }

    private boolean isOnTopOfBorder(double pMouseX, double pMouseY, Anchor anchor) {
        return switch (anchor) {
            // Top section.
            case TOP_LEFT -> pMouseX >= getX() - 5 && pMouseX <= getX() && pMouseY >= getY() - 5 && pMouseY <= getY();
            case TOP_CENTER ->
                    pMouseX >= getX() && pMouseX <= getX() + getWidth() && pMouseY >= getY() - 5 && pMouseY <= getY();
            case TOP_RIGHT ->
                    pMouseX >= getX() + getWidth() && pMouseX <= getX() + getWidth() + 5 && pMouseY >= getY() - 5 && pMouseY <= getY();

            // Middle section.
            case MIDDLE_LEFT ->
                    pMouseX >= getX() - 5 && pMouseX <= getX() && pMouseY >= getY() && pMouseY <= getY() + getHeight();
            case MIDDLE_CENTER ->
                    pMouseX >= getX() && pMouseX <= getX() + getWidth() && pMouseY >= getY() && pMouseY <= getY() + getHeight();
            case MIDDLE_RIGHT ->
                    pMouseX >= getX() + getWidth() && pMouseX <= getX() + getWidth() + 5 && pMouseY >= getY() && pMouseY <= getY() + getHeight();

            // Bottom section.
            case BOTTOM_LEFT ->
                    pMouseX >= getX() - 5 && pMouseX <= getX() && pMouseY >= getY() + getHeight() && pMouseY <= getY() + getHeight() + 5;
            case BOTTOM_CENTER ->
                    pMouseX >= getX() && pMouseX <= getX() + getWidth() && pMouseY >= getY() + getHeight() && pMouseY <= getY() + getHeight() + 5;
            case BOTTOM_RIGHT ->
                    pMouseX >= getX() + getWidth() && pMouseX <= getX() + getWidth() + 5 && pMouseY >= getY() + getHeight() && pMouseY <= getY() + getHeight() + 5;
        };
    }

    private Anchor findBorderRegion(double pMouseX, double pMouseY) {
        if (isOnTopOfBorder(pMouseX, pMouseY, Anchor.TOP_LEFT)) {
            return Anchor.TOP_LEFT;
        } else if (isOnTopOfBorder(pMouseX, pMouseY, Anchor.TOP_CENTER)) {
            return Anchor.TOP_CENTER;
        } else if (isOnTopOfBorder(pMouseX, pMouseY, Anchor.TOP_RIGHT)) {
            return Anchor.TOP_RIGHT;
        } else if (isOnTopOfBorder(pMouseX, pMouseY, Anchor.MIDDLE_LEFT)) {
            return Anchor.MIDDLE_LEFT;
        } else if (isOnTopOfBorder(pMouseX, pMouseY, Anchor.MIDDLE_CENTER)) {
            return Anchor.MIDDLE_CENTER;
        } else if (isOnTopOfBorder(pMouseX, pMouseY, Anchor.MIDDLE_RIGHT)) {
            return Anchor.MIDDLE_RIGHT;
        } else if (isOnTopOfBorder(pMouseX, pMouseY, Anchor.BOTTOM_LEFT)) {
            return Anchor.BOTTOM_LEFT;
        } else if (isOnTopOfBorder(pMouseX, pMouseY, Anchor.BOTTOM_CENTER)) {
            return Anchor.BOTTOM_CENTER;
        } else if (isOnTopOfBorder(pMouseX, pMouseY, Anchor.BOTTOM_RIGHT)) {
            return Anchor.BOTTOM_RIGHT;
        }

        // The mouse isn't on a border region or in the content.
        return null;
    }

    private double getDragX() {
        return dragX;
    }

    private void setDragX(double dragX) {
        this.dragX = dragX;
    }

    private double getDragY() {
        return dragY;
    }

    private void setDragY(double dragY) {
        this.dragY = dragY;
    }

    private double getResizeX() {
        return resizeX;
    }

    private void setResizeX(double resizeX) {
        this.resizeX = resizeX;
    }

    private double getResizeY() {
        return resizeY;
    }

    private void setResizeY(double resizeY) {
        this.resizeY = resizeY;
    }

    private boolean isOnTopOfTitle(double x, double y) {
        return x >= getX() && x <= getX() + getWidth() && y >= getY() - 21 && y <= getY();
    }

    public boolean isResizing() {
        return resizing;
    }

    private void setResizing(boolean resizing) {
        this.resizing = resizing;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    /**
     * Get whether the window is shown.
     *
     * @return true if the window is shown, false if it is not.
     * @throws IllegalStateException if the window is not valid.
     */
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @NotNull
    public String getPlainTitle() {
        return Objects.requireNonNull(ChatFormatting.stripFormatting(getTitle().getString()), () -> "Wtf happened here?");
    }
}
