package com.ultreon.mods.guilib.common;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

/**
 * Scale interface, used to scale GUI elements.
 *
 * @author Qboi123
 * @since 0.0.1.7
 */
@FunctionalInterface
public interface Scale {
    /**
     * Get a scale from a modifier.
     *
     * @param v the modifier.
     * @return the scale.
     * @author Qboi123
     * @since 0.0.1.7
     */
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    static Scale of(double v) {
        return () -> v;
    }

    /**
     * Get a static scale, where the scale of the game doesn't influence it in GUI.
     *
     * @param v the static scale.
     * @return a scale that makes gui elements have an independent scale.
     * @author Qboi123
     * @since 0.0.1.7
     */
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    @OnlyIn(Dist.CLIENT)
    static Scale ofStatic(double v) {
        return () -> 1 / Minecraft.getInstance().getWindow().getGuiScale() * v;
    }

    /**
     * Get the scale from the given supplier.
     *
     * @param supplier the supplier to use to get the scale.
     * @return a scale that uses the supplier to get the scale.
     * @author Qboi123
     * @since 0.0.1.7
     */
    @NotNull
    @Contract(pure = true)
    static Scale of(DoubleSupplier supplier) {
        return supplier::getAsDouble;
    }

    /**
     * Get the scale from the given supplier.
     *
     * @param supplier the supplier to use to get the scale.
     * @return a scale that uses the supplier to get the scale.
     * @author Qboi123
     * @since 0.0.1.7
     */
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    static Scale of(Supplier<Double> supplier) {
        return () -> (double) supplier.get();
    }

    /**
     * Get the scale.
     *
     * @return the scale.
     * @author Qboi123
     * @since 0.0.1.7
     */
    double getScale();
}
