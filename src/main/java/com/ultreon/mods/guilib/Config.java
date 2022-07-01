package com.ultreon.mods.guilib;

import com.ultreon.mods.guilib.client.InternalConfigScreen;
import com.ultreon.mods.guilib.client.gui.ReloadsTheme;
import com.ultreon.mods.guilib.client.gui.Theme;
import com.ultreon.mods.guilib.client.gui.screen.TitleStyle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = UltreonGuiLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Config {
    public static final ForgeConfigSpec.EnumValue<Theme> THEME;
    public static final ForgeConfigSpec.EnumValue<TitleStyle> TITLE_STYLE;
    private static final ForgeConfigSpec spec;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        THEME = builder
                .comment("The theme to use for the GUI")
                .defineEnum("theme", Theme.DARK);
        TITLE_STYLE = builder
                .comment("The style of the title")
                .defineEnum("title_style", TitleStyle.DETACHED);
        spec = builder.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.spec);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading event) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading event) {
        if (Minecraft.getInstance().screen instanceof ReloadsTheme) {
            ((ReloadsTheme) Minecraft.getInstance().screen).reloadTheme();
        }
    }

    public static void save() {
        spec.save();
    }

    public static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new ConfigGuiHandler.ConfigGuiFactory((minecraft, screen) -> new InternalConfigScreen(screen))
        );
    }
}
