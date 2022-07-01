package com.ultreon.mods.guilib.client.gui.screen.window;

import com.ultreon.mods.guilib.UltreonGuiLib;
import com.ultreon.mods.guilib.util.KeyboardHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;
import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = UltreonGuiLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ScreenHooks {
    @SubscribeEvent
    public static void onMouseDrag(ScreenEvent.MouseDragEvent.Pre event) {
        double dragX = event.getDragX();
        double dragY = event.getDragY();
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();
        int button = event.getMouseButton();

        if (button == 0) {
            Window win = WindowManager.INSTANCE.getDraggingWindow();
            if (win != null) {
                event.setCanceled(true);
                win.mouseDragged(mouseX, mouseY, button, dragX, dragY);
            } else {
                WindowManager.INSTANCE.getWindowAt(mouseX, mouseY).ifPresent(w -> {
                    event.setCanceled(true);
                    w.mouseDragged(mouseX, mouseY, button, dragX, dragY);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onMouseClick(ScreenEvent.MouseClickedEvent.Pre event) {
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();
        int button = event.getButton();

        if (button == 0) {
            WindowManager.INSTANCE.getWindowAt(mouseX, mouseY).ifPresent(w -> {
                event.setCanceled(true);
                w.mouseClicked(mouseX, mouseY, button);
                WindowManager.INSTANCE.moveToFront(w);
            });
        }
    }

    @SubscribeEvent
    public static void onMouseRelease(ScreenEvent.MouseReleasedEvent.Pre event) {
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();
        int button = event.getButton();

        if (button == 0) {
            WindowManager.INSTANCE.getWindowAt(mouseX, mouseY).ifPresent(w -> {
                event.setCanceled(true);
                w.mouseReleased(mouseX, mouseY, button);
            });
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(ScreenEvent.MouseScrollEvent.Pre event) {
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();
        double delta = event.getScrollDelta();

        WindowManager.INSTANCE.getWindowAt(mouseX, mouseY).ifPresent(w -> {
            event.setCanceled(true);
            w.mouseScrolled(mouseX, mouseY, delta);
        });
    }

    @SubscribeEvent
    public static void onKeyPress(ScreenEvent.KeyboardKeyPressedEvent.Pre event) {
        int keyCode = event.getKeyCode();
        int scanCode = event.getScanCode();
        int modifiers = event.getModifiers();

        WindowManager.INSTANCE.getForegroundWindow().ifPresent(w -> {
            event.setCanceled(true);
            w.keyPressed(keyCode, scanCode, modifiers);
        });

        if (KeyboardHelper.isCtrlDown() && KeyboardHelper.isAltDown() && KeyboardHelper.isShiftDown() && keyCode == GLFW.GLFW_KEY_ESCAPE) {
            WindowManager.INSTANCE.closeAllWindows();
        }

        if (KeyboardHelper.isCtrlDown() && keyCode == GLFW.GLFW_KEY_DOWN) {
            WindowManager.INSTANCE.getForegroundWindow().ifPresent(Window::hide);
        }

        if (KeyboardHelper.isCtrlDown() && keyCode == GLFW.GLFW_KEY_Q) {
            WindowManager.INSTANCE.getForegroundWindow().ifPresent(Window::close);
        }

        if (keyCode == GLFW.GLFW_KEY_RIGHT_CONTROL) {
//            new Window(5, 5, 100, 100).show();

            Random random = new Random();
            new Window(random.nextInt(5, event.getScreen().width - 105), random.nextInt(5, event.getScreen().height - 105), 100, 100).show();
        }
    }

    public static boolean onMouseMoved(long windowPtr, double mouseX, double mouseY) {
        Optional<Window> windowAt = WindowManager.INSTANCE.getWindowAt(mouseX, mouseY);
        if (windowAt.isPresent()) {
            Window w = windowAt.get();
            w.mouseMoved(mouseX, mouseY);
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void onKeyRelease(ScreenEvent.KeyboardKeyReleasedEvent.Pre event) {
        int keyCode = event.getKeyCode();
        int scanCode = event.getScanCode();
        int modifiers = event.getModifiers();

        WindowManager.INSTANCE.getForegroundWindow().ifPresent(w -> {
            event.setCanceled(true);
            w.keyReleased(keyCode, scanCode, modifiers);
        });
    }

    @SubscribeEvent
    public static void onCharTyped(ScreenEvent.KeyboardCharTypedEvent.Pre event) {
        char codePoint = event.getCodePoint();
        int modifiers = event.getModifiers();

        WindowManager.INSTANCE.getForegroundWindow().ifPresent(w -> {
            event.setCanceled(true);
            w.charTyped(codePoint, modifiers);
        });
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void onDrawScreen(ScreenEvent.DrawScreenEvent.Post event) {
        WindowManager.INSTANCE.renderAllWindows(event.getPoseStack(), event.getMouseX(), event.getMouseY(), event.getPartialTicks());
    }
}
