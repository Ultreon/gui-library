package com.ultreon.mods.guilib;

import net.minecraft.resources.ResourceLocation;
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
public class UltreonGuiLib {
    // Mod Constants.
    public static final String MOD_ID = "ultreon_gui_lib";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Boolean MOD_DEV_OVERRIDE = null;

    public static final String MOD_NAME;
    public static final String MOD_VERSION;
    public static final String MOD_DESCRIPTION;
    public static final String MOD_NAMESPACE;

    static {
        IModInfo info = ModList.get().getMods().stream().filter(modInfo -> Objects.equals(modInfo.getModId(), MOD_ID)).findFirst().orElseThrow(IllegalStateException::new);
        MOD_VERSION = info.getVersion().toString();
        MOD_DESCRIPTION = info.getDescription();
        MOD_NAMESPACE = info.getNamespace();
        MOD_NAME = info.getDisplayName();

    }

    /**
     * Mod manager construction creator that exists to create the mod instance that is needed for the mod to exist and even work lol.
     */
    public UltreonGuiLib() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
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

    private void setup(final FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Some server is starting I think...");
    }
}
