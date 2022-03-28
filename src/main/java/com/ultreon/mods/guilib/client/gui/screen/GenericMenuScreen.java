/*
 * Copyright (c) 2022. - Qboi SMP Development Team
 * Do NOT redistribute, or copy in any way, and do NOT modify in any way.
 * It is not allowed to hack into the code, use cheats against the code and/or compiled form.
 * And it is not allowed to decompile, modify or/and patch parts of code or classes or in full form.
 * Sharing this file isn't allowed either, and is hereby strictly forbidden.
 * Sharing decompiled code on social media or an online platform will cause in a report on that account.
 *
 * ONLY the owner can bypass these rules.
 */

package com.ultreon.mods.guilib.client.gui.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.ultreon.mods.guilib.UltreonGuiLib;
import com.ultreon.mods.guilib.client.UserSettings;
import com.ultreon.mods.guilib.client.gui.widget.ThemedButton;
import com.ultreon.mods.guilib.client.gui.widget.Label;
import com.ultreon.mods.guilib.client.gui.widget.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;

import static net.minecraft.client.gui.screens.TitleScreen.PANORAMA_OVERLAY;

@SuppressWarnings({"UnusedReturnValue", "unused", "SameParameterValue"})
public abstract class GenericMenuScreen extends BaseScreen {
    public final ResourceLocation background;

    protected final Minecraft mc = Minecraft.getInstance();

    @Nullable private final Screen back;
    private final java.util.List<Row> rows = new ArrayList<>();
    private boolean frozen = false;

    private final TitleStyle titleStyle;
    private int titleColor;
    private static final int TITLE_COLOR = 0xff606060;
    private static final int TITLE_COLOR_DARK = 0xffffffff;
    private final boolean panorama;
    private final boolean darkMode;
    private boolean initialized;

    protected GenericMenuScreen(Properties properties) {
        super(properties.title);
        this.darkMode = properties.darkMode;
        this.panorama = properties.panorama;

        this.titleColor = properties.darkMode ? TITLE_COLOR_DARK : TITLE_COLOR;
        this.titleStyle = properties.titleStyle;

        this.font = Minecraft.getInstance().font;
        this.minecraft = Minecraft.getInstance();

        this.back = properties.back;
        this.background = UltreonGuiLib.res(properties.darkMode ? "textures/gui/generic_menu_dark.png" : "textures/gui/generic_menu.png");
    }

    protected final boolean initialized() {
        return initialized;
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, Component componentR) {
        return addButtonRow(componentL, btn -> {}, componentR, btn -> {});
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, Button.OnPress onPressL, Component componentR, Button.OnPress onPressR) {
        return addButtonRow(componentL, onPressL, (btn, stack, mx, my) -> {}, componentR, onPressR, (btn, stack, mx, my) -> {});
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, Runnable onPressL, Component componentR, Runnable onPressR) {
        return addButtonRow(componentL, btn -> onPressL.run(), (btn, stack, mx, my) -> {}, componentR, btn -> onPressR.run(), (btn, stack, mx, my) -> {});
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, Button.OnPress onPressL, Button.OnTooltip onTooltipL, Component componentR, Button.OnPress onPressR, Button.OnTooltip onTooltipR) {
        return addButtonRow(componentL, darkMode ? ThemedButton.Type.DARK : ThemedButton.Type.NORMAL, onPressL, onTooltipL, componentR, darkMode ? ThemedButton.Type.DARK : ThemedButton.Type.NORMAL, onPressR, onTooltipR);
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, Runnable onPressL, Button.OnTooltip onTooltipL, Component componentR, Runnable onPressR, Button.OnTooltip onTooltipR) {
        return addButtonRow(componentL, darkMode ? ThemedButton.Type.DARK : ThemedButton.Type.NORMAL, btn -> onPressL.run(), onTooltipL, componentR, darkMode ? ThemedButton.Type.DARK : ThemedButton.Type.NORMAL, btn -> onPressR.run(), onTooltipR);
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, ThemedButton.Type typeL, Component componentR, ThemedButton.Type typeR) {
        return addButtonRow(componentL, typeL, btn -> {}, componentR, typeR, btn -> {});
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, ThemedButton.Type typeL, Button.OnPress onPressL, Component componentR, Button.OnPress onPressR) {
        return addButtonRow(componentL, typeL, onPressL, componentR, darkMode ? ThemedButton.Type.DARK : ThemedButton.Type.NORMAL, onPressR);
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, ThemedButton.Type typeL, Runnable onPressL, Component componentR, Runnable onPressR) {
        return addButtonRow(componentL, typeL, btn -> onPressL.run(), componentR, darkMode ? ThemedButton.Type.DARK : ThemedButton.Type.NORMAL, btn -> onPressR.run());
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, Button.OnPress onPressL, Component componentR, ThemedButton.Type typeR, Button.OnPress onPressR) {
        return addButtonRow(componentL, darkMode ? ThemedButton.Type.DARK : ThemedButton.Type.NORMAL, onPressL, componentR, typeR, onPressR);
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, Runnable onPressL, Component componentR, ThemedButton.Type typeR, Runnable onPressR) {
        return addButtonRow(componentL, darkMode ? ThemedButton.Type.DARK : ThemedButton.Type.NORMAL, btn -> onPressL.run(), componentR, typeR, btn -> onPressR.run());
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, ThemedButton.Type typeL, Button.OnPress onPressL, Component componentR, ThemedButton.Type typeR, Button.OnPress onPressR) {
        return addButtonRow(componentL, typeL, onPressL, (btn, stack, mx, my) -> {}, componentR, typeR, onPressR, (btn, stack, mx, my) -> {});
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, ThemedButton.Type typeL, Runnable onPressL, Component componentR, ThemedButton.Type typeR, Runnable onPressR) {
        return addButtonRow(componentL, typeL, btn -> onPressL.run(), (btn, stack, mx, my) -> {}, componentR, typeR, btn -> onPressR.run(), (btn, stack, mx, my) -> {});
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, ThemedButton.Type typeL, Runnable onPressL, Button.OnTooltip onTooltipL, Component componentR, ThemedButton.Type typeR, Runnable onPressR, Button.OnTooltip onTooltipR) {
        return addButtonRow(componentL, typeL, btn -> onPressL.run(), onTooltipL, componentR, typeR, btn -> onPressR.run(), onTooltipR);
    }

    @SuppressWarnings("unused")
    public Pair<Button, Button> addButtonRow(Component componentL, ThemedButton.Type typeL, Button.OnPress onPressL, Button.OnTooltip onTooltipL, Component componentR, ThemedButton.Type typeR, Button.OnPress onPressR, Button.OnTooltip onTooltipR) {
        if (this.frozen) {
            return null;
        }

        Button left = new ThemedButton(0, 0, 0, 0, componentL, onPressL, onTooltipL, typeL);
        Button right = new ThemedButton(0, 0, 0, 0, componentR, onPressR, onTooltipR, typeR);

        left.setWidth(((width() - 5 - 5) / 2) - 1);
        right.setWidth(((width() - 5 - 5) / 2) - 1);
        this.rows.add(new Row(ImmutableList.of(left, right), 24, ((width() - 5 - 5) / 2) - 3, 20, 6, 2, 4, 0, 77));
        addRenderableWidget(left);
        addRenderableWidget(right);
        return new Pair<>(left, right);
    }

    @SuppressWarnings("unused")
    public Button addButtonRow(Component component) {
        return addButtonRow(component, btn -> {});
    }

    @SuppressWarnings("unused")
    public Button addButtonRow(Component component, Runnable onPress) {
        return addButtonRow(component, btn -> onPress.run(), (btn, stack, mx, my) -> {});
    }

    @SuppressWarnings("unused")
    public Button addButtonRow(Component component, Button.OnPress onPress) {
        return addButtonRow(component, onPress, (btn, stack, mx, my) -> {});
    }

    @SuppressWarnings("unused")
    public Button addButtonRow(Component component, Runnable onPress, Button.OnTooltip onTooltip) {
        return addButtonRow(component, darkMode ? ThemedButton.Type.DARK : ThemedButton.Type.NORMAL, btn -> onPress.run(), onTooltip);
    }

    @SuppressWarnings("unused")
    public Button addButtonRow(Component component, Button.OnPress onPress, Button.OnTooltip onTooltip) {
        return addButtonRow(component, darkMode ? ThemedButton.Type.DARK : ThemedButton.Type.NORMAL, onPress, onTooltip);
    }

    @SuppressWarnings("unused")
    public Button addButtonRow(Component component, ThemedButton.Type type) {
        return addButtonRow(component, type, btn -> {});
    }

    @SuppressWarnings("unused")
    public Button addButtonRow(Component component, ThemedButton.Type type, Button.OnPress onPress) {
        return addButtonRow(component, type, onPress, (btn, stack, mx, my) -> {});
    }

    @SuppressWarnings("unused")
    public Button addButtonRow(Component component, ThemedButton.Type type, Runnable onPress) {
        return addButtonRow(component, type, btn -> onPress.run(), (btn, stack, mx, my) -> {});
    }

    @SuppressWarnings("unused")
    public Button addButtonRow(Component component, ThemedButton.Type type, Runnable onPress, Button.OnTooltip onTooltip) {
        return addButtonRow(component, type, btn -> onPress.run(), onTooltip);
    }

    @SuppressWarnings("unused")
    public Button addButtonRow(Component component, ThemedButton.Type type, Button.OnPress onPress, Button.OnTooltip onTooltip) {
        if (this.frozen) {
            return null;
        }

        Button button = new ThemedButton(0, 0, 0, 0, component, onPress, onTooltip, type);

        button.setWidth(width() - 5 - 5);
        this.rows.add(new Row(ImmutableList.of(button), 24, width() - 6 - 6, 20, 6, 2, 4, 0, 53));
        addRenderableWidget(button);
        return button;
    }

    @SuppressWarnings("unused")
    public GenericMenuScreen addInputRow(EditBox editBox) {
        if (this.frozen) {
            return this;
        }

        editBox.setBordered(false);

        editBox.setWidth(width() - 5 - 5);
        this.rows.add(new Row(ImmutableList.of(editBox), 16, width() - 5 - 5, 12, 7, 3, 4, 0, 37));
        addRenderableWidget(editBox);
        return this;
    }

    @SuppressWarnings("unused")
    public Label addLabel() {
        return addLabel("");
    }

    @SuppressWarnings("unused")
    public Label addLabel(String text) {
        return addLabel(new TextComponent(text));
    }

    @SuppressWarnings("unused")
    public Label addLabel(Component component) {
        if (this.frozen) {
            return null;
        }

        Label userWidget = new Label(0, 0, component);
        this.rows.add(new Row(ImmutableList.of(userWidget), font.lineHeight + 4, width() - 5 - 5, font.lineHeight, 8, 2, 4, 0, 226));
        addRenderableOnly(userWidget);
        return userWidget;
    }

    @SuppressWarnings("unused")
    public List addUserListRow(int count) {
        return addUserListRow(count, false);
    }

    @SuppressWarnings("unused")
    public List addUserListRow() {
        return addUserListRow(false);
    }

    @SuppressWarnings("unused")
    public List addUserListRow(boolean hasSearch) {
        return addUserListRow(3, hasSearch);
    }

    @SuppressWarnings("unused")
    public List addUserListRow(int count, boolean hasSearch) {
        if (this.frozen) {
            return null;
        }

        List widget = new List(this, 0, 0, width() - 6 - 7, count, hasSearch, title);
        widget.setWidth(width() - 6 - 6);
        this.rows.add(new Row(ImmutableList.of(widget), widget.getHeight() + 2, width() - 6 - 6, widget.getHeight(), 6, 0, 4, 0, 226, width(), 1));
        addRenderableWidget(widget);
        return widget;
    }

    @SuppressWarnings("unused")
    private void back(@Nullable Button button) {
        back();
    }

    /**
     * Go back to previous screen.
     *
     * @see #onPreBack()
     * @see #onPostBack()
     */
    public final void back() {
        if (onPreBack()) return;

        if (back != null) {
            mc.setScreen(back);
        } else {
            mc.popGuiLayer();
        }

        onPostBack();
    }

    /**
     * Handle things after going back to the previous screen.
     */
    protected void onPostBack() {

    }

    /**
     * Handle things before going back to the previous screen.
     *
     * @return true to cancel the back event, false to let the screen go back.
     */
    protected boolean onPreBack() {
        return false;
    }

    /**
     * @return the title color.
     */
    public int getTitleColor() {
        return titleColor;
    }

    /**
     * @param titleColor the title color to set.
     */
    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    /**
     * @return gui width.
     */
    protected final int width() {
        return 176;
    }

    /**
     * @return gui height.
     */
    protected final int height() {
        return rows.stream().mapToInt(Row::height).sum() + 4 + getRenderTitleBarHeight();
    }

    @Override
    protected boolean isAtTitleBar(double mouseX, double mouseY) {
        return isPointBetween((int) mouseX, (int) mouseY, left(), top(), width(), getTitleBarHeight());
    }

    private int getRenderTitleBarHeight() {
        if (titleStyle == null) {
            return 0;
        } else {
            return titleStyle.renderHeight;
        }
    }

    private int getTitleBarHeight() {
        if (titleStyle == null) {
            return 0;
        } else {
            return titleStyle.height;
        }
    }

    protected final int left() {
        return (this.width - width()) / 2;
    }

    protected final int right() {
        return left() + width();
    }

    protected final int top() {
        return (this.height - height()) / 2;
    }

    protected final int bottom() {
        return top() + height();
    }

    protected void init() {
        frozen = true;
        this.mc.keyboardHandler.setSendRepeatsToGui(true);
        initialized = true;
    }

    @Override
    protected void clearWidgets() {

    }

    public void removed() {
        this.mc.keyboardHandler.setSendRepeatsToGui(false);
    }

    @Override
    public void renderBackground(@NotNull PoseStack pose) {
        if (!this.panorama) super.renderBackground(pose);
    }

    @Override
    public void renderBackground(@NotNull PoseStack pose, int vOffset) {
        if (!this.panorama) super.renderBackground(pose, vOffset);
    }

    public void onPreRender() {

    }

    public void onPostRender() {

    }

    public final void render(@NotNull PoseStack pose, int mouseX, int mouseY, float frameTime) {
        onPreRender();
        if (this.panorama) this.renderPanorama(pose, frameTime);
        this.renderBackground(pose);

        int curY = top();
        int index = 0;

        // Title rendering (if present).
        RenderSystem.setShaderTexture(0, background);

        switch (this.titleStyle) {
            case HIDDEN -> {
                // Render no title. Only the top border.
                this.blit(pose, left(), top(), 0, 21, width(), 4);
                index++;
                curY += 4;
            }
            case NORMAL -> {
                // Render normal title and the top border.
                this.blit(pose, left(), top(), 0, 21, width(), 16);
                this.font.draw(pose, this.title, (int) (left() + width() / 2f - this.font.width(this.title.getString()) / 2), top() + 6, this.titleColor);

                index++;
                curY += 16;
            }
            case DETACHED -> {
                // Render detached title and the top border.
                blit(pose, left(), top(), 0, 0, width(), 25);
                this.font.draw(pose, this.title, (int) (left() + width() / 2f - this.font.width(this.title.getString()) / 2), top() + 6, this.titleColor);

                index++;
                curY += 25;
            }
            default -> throw new IllegalStateException("Unexpected value: " + titleStyle);
        }


        renderRows(pose, mouseX, mouseY, frameTime, curY, index);

        super.render(pose, mouseX, mouseY, frameTime);

        onPostRender();
    }

    @SuppressWarnings({"DuplicatedCode", "ConstantConditions", "UnusedAssignment"})
    public void renderPanorama(PoseStack pose, float partialTicks) {
        // Nonnull Requirements
        Objects.requireNonNull(this.minecraft);

        PanoramaScreen.panorama.render(partialTicks, minecraft.screen == this ? 1f : 0f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, PANORAMA_OVERLAY);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        blit(pose, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);

        int index = 0;
        if (!UltreonGuiLib.isModDev()) {
            drawString(pose, mc.font, "UltreonGuiLib Client: " + UltreonGuiLib.MOD_VERSION, 0, this.height - (10 + (index++) * (this.font.lineHeight + 1)), 0xffffff);
            drawString(pose, mc.font, "UltreonGuiLib: " + UltreonGuiLib.MOD_VERSION, 0, this.height - (10 + (index++) * (this.font.lineHeight + 1)), 0xffffff);
        }
    }

    private void renderRows(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks, int y, int index) {
        if (rows.size() > 0) {
            for (Row row : rows.subList(0, rows.size() - 1)) {
                // Render the row background and widgets.
                renderRowBackground(pose, y, row);
                renderRowWidgets(pose, mouseX, mouseY, partialTicks, y, row);

                // Advance in index, and add the current row height to the current y coordinate.
                y += row.height();
                index++;
            }
        }

        if (!rows.isEmpty()) {
            // Get the last row if there were rows at least.
            Row row = rows.get(rows.size() - 1);

            // Render the row background and widgets.
            renderRowBackground(pose, y, row);
            renderRowWidgets(pose, mouseX, mouseY, partialTicks, y, row);

            y += row.height();
        }

        // Render bottom border.
        RenderSystem.setShaderTexture(0, background);
        this.blit(pose, left(), y, 0, 252, width(), 4);
    }

    private void renderRowWidgets(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks, int curY, Row row) {
        // Render row.
        if (row.widgets.size() == 1) {
            // Render row with single widget.
            Widget widget = row.widgets.get(0);
            renderRowWidget(
                    widget,
                    left() + row.deltaX(),
                    curY + row.deltaY(),
                    row.widgetWidth(),
                    row.widgetHeight(),
                    pose,
                    mouseX, mouseY,
                    partialTicks
            );
        } else {
            // Render row with multiple widgets.
            int x = left() + row.deltaX();
            for (Widget widget : row.widgets()) {
                renderRowWidget(widget,
                        x,
                        curY + row.deltaY(),
                        row.widgetWidth(),
                        row.widgetHeight(),
                        pose,
                        mouseX, mouseY,
                        partialTicks);

                // Advance x position.
                x += row.widgetWidth() + row.widgetOffset();
            }
        }
    }

    private void renderRowWidget(Widget widget, int x, int y, int width, int height, @NotNull PoseStack matrices, int mouseX, int mouseY, float partialTicks) {
        if (widget instanceof AbstractWidget abstractWidget) {
            abstractWidget.x = x;
            abstractWidget.y = y;
            abstractWidget.setWidth(width);
            abstractWidget.setHeight(height);
        } else if (widget instanceof Label label) {
            label.x = x;
            label.y = y;
        }
        widget.render(matrices, mouseX, mouseY, partialTicks);
    }

    private void renderRowBackground(@NotNull PoseStack pose, int y, Row row) {
        // Render row background.
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, background);
        blit(pose, left(), y, width(), row.height(), row.u(), row.v(), row.uw(), row.vh(), 256, 256);
    }

    @Override
    @NotNull
    public java.util.List<? extends GuiEventListener> children() {
        ArrayList<GuiEventListener> list = new ArrayList<>(super.children());
        for (Row row : rows) {
            list.addAll(row.widgets()
                    .stream()
                    .filter(widget -> widget instanceof GuiEventListener)
                    .map(widget -> (GuiEventListener) widget)
                    .toList());
        }

        return list;
    }

    @Override
    public void tick() {
        for (Row row : rows) {
            for (Widget widget : row.widgets()) {
                if (widget instanceof EditBox editBox) {
                    editBox.tick();
                }
            }
        }
    }

    @Override
    public final Vec2 getCloseButtonPos() {
        switch (titleStyle) {
            case NORMAL -> {
                int iconX = right() - 9 - 5;
                int iconY = top() + (int) (5 + (21 / 2f - font.lineHeight / 2f) - 4);
                return new Vec2(iconX, iconY);
            }
            case DETACHED -> {
                int iconX = right() - 9 - 5;
                int iconY = top() + 1 + (int) (((25 - 6) / 2f - font.lineHeight / 2f));
                return new Vec2(iconX, iconY);
            }
        }
        return null;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Row row : rows) {
            for (Widget widget : row.widgets()) {
                if (widget instanceof EditBox editBox) {
                    if (editBox.isFocused()) {
                        editBox.mouseClicked(mouseX, mouseY, button);
                    }
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button)/* || this.filterList.mouseClicked(mouseX, mouseY, button)*/;
    }

    public boolean isPauseScreen() {
        return false;
    }

    private record Row(ImmutableList<Widget> widgets, int height, int widgetWidth, int widgetHeight,
                       int deltaX, int deltaY, int widgetOffset, int u, int v, int uw, int vh) {

        public Row(ImmutableList<Widget> widgets, int height, int widgetWidth, int deltaX, int deltaY, int widgetOffset) {
            this(widgets, height, widgetWidth, deltaX, deltaY, widgetOffset, 0, 216);
        }

        public Row(ImmutableList<Widget> widgets, int height, int widgetWidth, int deltaX, int deltaY, int widgetOffset, int u, int v) {
            this(widgets, height, widgetWidth, height, deltaX, deltaY, widgetOffset, u, v);
        }

        public Row(ImmutableList<Widget> widgets, int height, int widgetWidth, int widgetHeight, int deltaX, int deltaY, int widgetOffset, int u, int v) {
            this(widgets, height, widgetWidth, widgetHeight, deltaX, deltaY, widgetOffset, u, v, height);
        }

        public Row(ImmutableList<Widget> widgets, int height, int widgetWidth, int widgetHeight, int deltaX, int deltaY, int widgetOffset, int u, int v, int uh) {
            this(widgets, height, widgetWidth, widgetHeight, deltaX, deltaY, widgetOffset, u, v, 176, uh);
        }
    }

    public static class Properties {
        private Component title;
        private TitleStyle titleStyle = TitleStyle.NORMAL;
        private boolean darkMode = UserSettings.get().hasDarkMode();
        private boolean panorama = Minecraft.getInstance().level == null;
        @Nullable private Screen back = null;

        public Properties title(Component title) {
            this.title = title;
            return this;
        }

        public Properties titleText(String text) {
            this.title = new TextComponent(text);
            return this;
        }

        public Properties titleLang(String text) {
            this.title = new TranslatableComponent(text);
            return this;
        }

        public Properties titleStyle(TitleStyle titleStyle) {
            this.titleStyle = titleStyle;
            return this;
        }

        public Properties darkMode(boolean darkMode) {
            this.darkMode = darkMode;
            return this;
        }

        public Properties panorama() {
            this.panorama = true;
            return this;
        }

        public Properties back(Screen back) {
            this.back = back;
            return this;
        }
    }
}
