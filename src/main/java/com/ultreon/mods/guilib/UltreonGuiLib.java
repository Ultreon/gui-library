package com.ultreon.mods.guilib;

import com.ultreon.mods.guilib.client.gui.ReloadsTheme;
import com.ultreon.mods.guilib.client.gui.Theme;
import com.ultreon.mods.guilib.client.gui.screen.TitleStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Main
 */
@Mod(UltreonGuiLib.MOD_ID)
public class UltreonGuiLib implements ReloadsTheme {
    // Mod Constants.
    public static final String MOD_ID = "ultreon_gui_lib";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Boolean MOD_DEV_OVERRIDE = null;

    public static final String MOD_NAME;
    public static final String MOD_VERSION;
    public static final String MOD_DESCRIPTION;
    public static final String MOD_NAMESPACE;
    private static UltreonGuiLib instance;

    static {
        IModInfo info = ModList.get().getMods().stream().filter(modInfo -> Objects.equals(modInfo.getModId(), MOD_ID)).findFirst().orElseThrow(IllegalStateException::new);
        MOD_VERSION = info.getVersion().toString();
        MOD_DESCRIPTION = info.getDescription();
        MOD_NAMESPACE = info.getNamespace();
        MOD_NAME = info.getDisplayName();

    }

    public static boolean isClientSide() {
        return FMLEnvironment.dist.isClient();
    }

    public static boolean isServerSide() {
        return FMLEnvironment.dist.isDedicatedServer();
    }

    public static boolean hasClientSide() {
        return FMLEnvironment.dist.isClient();
    }

    @SuppressWarnings("UnnecessaryDefault")
    public static boolean hasServerSide() {
        return switch (FMLEnvironment.dist) {
            case CLIENT, DEDICATED_SERVER -> true;
            default -> false;
        };
    }

    /**
     * Mod manager construction creator that exists to create the mod instance that is needed for the mod to exist and even work lol.
     */
    public UltreonGuiLib() {
        instance = this;

        if (!hasClientSide()) {
            return;
        }

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        Config.register();

        // Register mod configuration screen.
        Config.registerConfigScreen();
    }

    public static UltreonGuiLib get() {
        return instance;
    }

    /**
     * Get Mod manager's resource location.
     *
     * @param path resource path
     * @return the resource location.
     */
    public static ResourceLocation res(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    /**
     * Check if Vertinox is currently a development build.
     *
     * @return the Mod Dev State.
     */
    @SuppressWarnings("ConstantConditions")
    public static boolean isModDev() {
        if (MOD_DEV_OVERRIDE != null) {
            return MOD_DEV_OVERRIDE;
        }
        return !FMLEnvironment.production;
    }

    @OnlyIn(Dist.CLIENT)
    public static Theme getTheme() {
        return Config.THEME.get();
    }

    @OnlyIn(Dist.CLIENT)
    public static void setTheme(Theme theme) {
        Config.THEME.set(theme);
        Config.THEME.save();

        instance.reloadTheme();
    }

    @OnlyIn(Dist.CLIENT)
    public static TitleStyle getTitleStyle() {
        return Config.TITLE_STYLE.get();
    }

    @OnlyIn(Dist.CLIENT)
    public static void setTitleStyle(TitleStyle style) {
        Config.TITLE_STYLE.set(style);
        Config.TITLE_STYLE.save();

        instance.reloadTheme();
    }

    @OnlyIn(Dist.CLIENT)
    public void reloadTheme() {
        if (Minecraft.getInstance().screen instanceof ReloadsTheme) {
            ((ReloadsTheme) Minecraft.getInstance().screen).reloadTheme();
        }
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Some server is starting I think...");
    }
}
