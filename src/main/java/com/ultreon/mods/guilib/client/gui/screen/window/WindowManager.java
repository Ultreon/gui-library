package com.ultreon.mods.guilib.client.gui.screen.window;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class WindowManager {
    public static WindowManager INSTANCE = new WindowManager();
    private final List<Window> windows = new ArrayList<>();

    private WindowManager() {

    }

    void addWindow(Window window) {
        if (windows.contains(window)) {
            return;
        }
        this.windows.add(0, window);
    }

    void removeWindow(Window window) {
        this.windows.remove(window);
    }

    void removeAllWindows() {
        this.windows.clear();
    }

    public Optional<Window> getForegroundWindow() {
        List<Window> windows = new ArrayList<>(this.windows);
        for (Window window : windows) {
            if (!window.isValid()) {
                this.windows.remove(window);
            }

            if (window.isVisible()) {
                return Optional.of(window);
            }
        }
        return Optional.empty();
    }

    public Optional<Window> findWindow(String title) {
        List<Window> windows = new ArrayList<>(this.windows);
        for (int i = windows.size() - 1; i >= 0; i--) {
            Window window = windows.get(i);
            if (!window.isValid()) {
                this.windows.remove(window);
            }

            String winTitle = window.getPlainTitle();
            if (winTitle.equals(title)) {
                return Optional.of(window);
            }
        }
        return Optional.empty();
    }

    public List<Window> getWindows() {
        return Collections.unmodifiableList(windows);
    }

    public void closeAllWindows() {
        for (Window window : windows) {
            window.close();
        }
    }

    boolean includesWindow(Window window) {
        return windows.contains(window);
    }

    public void renderAllWindows(@NotNull PoseStack pose, int mouseX, int mouseY, float partialTicks) {
        List<Window> windows = new ArrayList<>(this.windows);
        for (int i = windows.size() - 1; i >= 0; i--) {
            Window window = windows.get(i);
            if (!window.isValid()) {
                this.windows.remove(window);
            }

            if (window.isVisible() && window.isValid()) {
                pose.pushPose();
                window.render(pose, mouseX, mouseY, partialTicks);
                pose.popPose();
            }
        }
    }

    @Nullable
    public Window getDraggingWindow() {
        List<Window> windows = new ArrayList<>(this.windows);
        for (int i = windows.size() - 1; i >= 0; i--) {
            Window window = windows.get(i);
            if (!window.isValid()) {
                this.windows.remove(window);
            }

            if (window.isDragging()) {
                return window;
            }
        }
        return null;
    }

    public Optional<Window> getWindowAt(double mouseX, double mouseY) {
        for (Window window : windows) {
            if (window.isVisible() && window.isValid() && window.isMouseOver(mouseX, mouseY)) {
                return Optional.of(window);
            }
        }
        return Optional.empty();
    }

    public void moveToFront(Window w) {
        if (windows.contains(w)) {
            windows.remove(w);
            w.show();
            windows.add(0, w);
        }
    }
}
