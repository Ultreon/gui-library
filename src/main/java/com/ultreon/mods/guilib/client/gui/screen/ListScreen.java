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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.ultreon.mods.guilib.UltreonGuiLib;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * List screen. A screen that's made for only a list with entries.
 */
public final class ListScreen extends Screen {
    private IListFilter listFilter;
    private ListWidget list;
    private EditBox searchBox;
    private String searchTerms = "";
    private boolean initialized;

    private static final Component SEARCH_HINT = (new TranslatableComponent("gui.socialInteractions.search_hint")).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);
    private static final Component SEARCH_EMPTY = (new TranslatableComponent("gui.socialInteractions.search_empty")).withStyle(ChatFormatting.GRAY);
    private static final ResourceLocation GUI_TEXTURE = UltreonGuiLib.res("textures/gui/list.png");

    private IListEntryClick onListEntryClick = (list, entry) -> {

    };

    private IInitHandler onInit = () -> {

    };

    final List<ListWidget.Entry> cachedEntries = new ArrayList<>();

    /**
     * List screen constructor.
     *
     * @param title screen title.
     */
    public ListScreen(Component title) {
        super(title);
    }

    public void setOnListEntryClick(IListEntryClick handler) {
        this.onListEntryClick = handler;
    }

    public void setOnInit(IInitHandler handler) {
        this.onInit = handler;
    }

    public void setListFilter(IListFilter handler) {
        this.listFilter = handler;
    }

    public void addEntry(ListWidget.Entry entry) {
        this.cachedEntries.add(entry);
        if (list != null) {
            this.list.addEntry(entry);
        }
    }

    private int getListHeight() {
        return Math.max(52, this.height - 128 - 16);
    }

    private int func0() {
        return this.getListHeight() / 16;
    }

    private int func1() {
        return 80 + this.func0() * 16 - 8;
    }

    private int left() {
        return (this.width - 238) / 2;
    }

    public void tick() {
        super.tick();
        this.searchBox.tick();
    }

    protected void init() {
        onInit.init();

        Objects.requireNonNull(this.minecraft).keyboardHandler.setSendRepeatsToGui(true);
        if (this.initialized && this.list != null) {
            this.list.updateSize(this.width, this.height, 88, this.func1());
        } else {
            this.list = new ListWidget(this, this.minecraft, this.width, this.height, 88, this.func1(), 36);
        }

        String s = this.searchBox != null ? this.searchBox.getValue() : "";
        this.searchBox = new EditBox(this.font, this.left() + 28, 78, 196, 16, SEARCH_HINT);
        this.searchBox.setMaxLength(16);
        this.searchBox.setBordered(false);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(16777215);
        this.searchBox.setValue(s);
        this.searchBox.setResponder(this::search);
        this.addRenderableWidget(this.searchBox);
        this.addRenderableWidget(this.list);
        this.initialized = true;
    }

    public void removed() {
        Objects.requireNonNull(this.minecraft).keyboardHandler.setSendRepeatsToGui(false);
    }

    public void renderBackground(@NotNull PoseStack pose) {
        int i = this.left() + 3;
        super.renderBackground(pose);

        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        this.blit(pose, i, 64, 1, 1, 236, 8);
        int j = this.func0();

        for (int k = 0; k < j; ++k) {
            this.blit(pose, i, 72 + 16 * k, 1, 10, 236, 16);
        }

        this.blit(pose, i, 72 + 16 * j, 1, 27, 236, 8);
        this.blit(pose, i + 10, 76, 243, 1, 12, 12);
    }

    public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        Objects.requireNonNull(this.minecraft);
        this.renderBackground(pose);
        drawString(pose, this.minecraft.font, this.title, this.left() + 8, 35, -1);

        if (!this.list.isEmpty()) {
            this.list.render(pose, mouseX, mouseY, partialTicks);
        } else if (!this.searchBox.getValue().isEmpty()) {
            drawCenteredString(pose, this.minecraft.font, SEARCH_EMPTY, this.width / 2, (78 + this.func1()) / 2, -1);
        }

        if (!this.searchBox.isFocused() && this.searchBox.getValue().isEmpty()) {
            drawString(pose, this.minecraft.font, SEARCH_HINT, this.searchBox.x, this.searchBox.y, -1);
        } else {
            this.searchBox.render(pose, mouseX, mouseY, partialTicks);
        }

        super.render(pose, mouseX, mouseY, partialTicks);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.searchBox.isFocused()) {
            this.searchBox.mouseClicked(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button) || this.list.mouseClicked(mouseX, mouseY, button);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean isPauseScreen() {
        return false;
    }

    private void search(String text) {
        text = text.toLowerCase(Locale.ROOT);
        if (!text.equals(this.searchTerms)) {
            this.list.search(text);
            this.searchTerms = text;
        }

    }

    @FunctionalInterface
    public interface IListEntryClick {
        void call(ListWidget list, ListWidget.Entry entry);
    }

    @FunctionalInterface
    public interface IInitHandler {
        void init();
    }

    @FunctionalInterface
    public interface IListFilter {
        boolean filter(String query, String id, String title, String description);
    }

    @SuppressWarnings("DuplicatedCode")
    public static class ListWidget extends ContainerObjectSelectionList<ListWidget.Entry> {
        private final Minecraft mc;
        private final ListScreen screen;
        private final List<Entry> defaultEntries = new ArrayList<>();
        private String search;

        public ListWidget(ListScreen screen, Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight) {
            super(minecraft, width, height, top, bottom, itemHeight);
            this.mc = minecraft;

            this.screen = screen;

            for (Entry cachedEntry : screen.cachedEntries) {
                addEntry(cachedEntry);
            }

            this.setRenderBackground(false);
            this.setRenderTopAndBottom(false);
        }

        @Override
        protected int addEntry(@NotNull ListScreen.ListWidget.Entry entry) {
            defaultEntries.add(entry);
            if (search != null) {
                super.addEntry(entry);
            }
            return this.defaultEntries.size() - 1;
        }

        protected int getScrollbarPosition() {
            return this.width / 2 + 105; // 124 default
        }

        public void render(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks) {
            double scaleFactor = this.mc.getWindow().getGuiScale();
            RenderSystem.enableScissor((int) ((double) this.getRowLeft() * scaleFactor), (int) ((double) (this.height - this.y1) * scaleFactor), (int) ((double) (this.getScrollbarPosition() + 6) * scaleFactor), (int) ((double) (this.height - (this.height - this.y1) - this.y0 - 4) * scaleFactor));
            super.render(pose, mouseX, mouseY, partialTicks);
            RenderSystem.disableScissor();
        }

        public void search(String text) {
            this.search = Objects.equals(text, "") ? null : text;

            this.clearEntries();

            for (Entry entry : this.defaultEntries) {
                if (screen.listFilter.filter(text, entry.getId(), entry.getTitle(), entry.getDescription().getString())) {
                    super.addEntry(entry);
                }
            }
        }

        public boolean isEmpty() {
            return defaultEntries.isEmpty();
        }

        public void loadCache() {

        }

        @OnlyIn(Dist.CLIENT)
        public static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
            private static final int BUTTON_WIDTH = 60;
            private final Minecraft mc;
            private final List<GuiEventListener> buttons;
            private final String entryTitle;
            private final ListScreen screen;
            private float ticksTooltip;

            public static final int TITLE_COLOR = FastColor.ARGB32.color(255, 255, 255, 255);
            public static final int DESCRIPTION_COLOR = FastColor.ARGB32.color(140, 255, 255, 255);
            public static final int EMPTY_COLOR = FastColor.ARGB32.color(255, 74, 74, 74);
            public static final int NON_EMPTY_COLOR = FastColor.ARGB32.color(255, 48, 48, 48);

            private final Component description;
            private final String id;

            public Entry(Minecraft minecraft, ListScreen screen, String title, String description, String id, Button... buttons) {
                this.mc = minecraft;
                this.entryTitle = title;
                this.description = new TextComponent(description);
                this.id = id;
                this.screen = screen;

                this.buttons = Arrays.asList(buttons);
            }

            public void render(@NotNull PoseStack pose, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
                int i = left + 4;

                Component description = this.getDescription();
                int l;
                if (description == TextComponent.EMPTY) {
                    fill(pose, left, top, left + width, top + height, EMPTY_COLOR);
                    l = top + (height - 9) / 2;
                } else {
                    fill(pose, left, top, left + width, top + height, NON_EMPTY_COLOR);
                    l = top + (height - (9 + 9)) / 2;
                    this.mc.font.draw(pose, description, (float) i, (float) (l + 12), DESCRIPTION_COLOR);
                }

                this.mc.font.draw(pose, this.entryTitle, (float) i, (float) l, TITLE_COLOR);
                float ticksUntilTooltip = this.ticksTooltip;

                int btnIndex = 0;
                for (GuiEventListener guiEventListener : buttons) {
                    if (guiEventListener instanceof Button button) {
                        button.x = left + width - 8 - (BUTTON_WIDTH + 4) * (btnIndex + 1);
                        button.y = top + height / 2 - 10;
                        button.render(pose, mouseX, mouseY, partialTicks);
                        btnIndex++;
                    }
                }

                if (ticksUntilTooltip == this.ticksTooltip) {
                    this.ticksTooltip = 0.0F;
                }
            }

            public String getTitle() {
                return this.entryTitle;
            }

            public Component getDescription() {
                return description;
            }

            public String getId() {
                return id;
            }

            public ListScreen getScreen() {
                return screen;
            }

            @Override
            public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
                boolean wasClicked = super.mouseClicked(p_94695_, p_94696_, p_94697_);
                if (!wasClicked) {
                    screen.onListEntryClick.call(screen.list, this);
                }
                return wasClicked;
            }

            @NotNull
            @Override
            public List<? extends NarratableEntry> narratables() {
                return Collections.emptyList();
            }

            @Override
            public @NotNull List<? extends GuiEventListener> children() {
                return buttons;
            }
        }
    }
}
