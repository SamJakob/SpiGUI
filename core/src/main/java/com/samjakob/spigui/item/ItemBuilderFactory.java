package com.samjakob.spigui.item;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.samjakob.spigui.SpiGUI;

/**
 * A factory for {@link ItemBuilderBase}.
 *
 * <p>This factory is used to auto-register {@link ItemBuilderConstructors} for a compatible {@link ItemBuilderBase}
 * depending on which server API version SpiGUI is targeting.
 */
public final class ItemBuilderFactory implements ItemBuilderConstructors {

    static {
        // Initialize the ItemBuilderFactory before we call ensureFactoriesInitialized.
        // This is the entry point for the ItemBuilder, so there's a chance this will be invoked before the factory
        // would have been initialized with the static initializer (causing a chicken-and-egg situation). This
        // circumvents that by explicitly ensuring the factory is registered first.
        FACTORY = new ItemBuilderFactory();
        SpiGUI.ensureFactoriesInitialized();
    }

    /** The static singleton {@link ItemBuilderFactory} instance. */
    @Nonnull
    private static final ItemBuilderFactory FACTORY;

    /** The constructors to use to make {@link ItemBuilderBase}s. */
    @Nullable
    private ItemBuilderConstructors constructors;

    /** Internal default constructor for the {@link ItemBuilderFactory}. */
    private ItemBuilderFactory() {}

    /**
     * Get the globally registered instance of the {@link ItemBuilderFactory}.
     *
     * @return the {@link ItemBuilderFactory}.
     */
    @Nonnull
    public static ItemBuilderFactory get() {
        return Objects.requireNonNull(FACTORY);
    }

    /**
     * Checks whether the constructors have already been registered elsewhere with
     * {@link #setConstructors(ItemBuilderConstructors)}.
     *
     * @return true if constructors have already been registered (implying new ones will be ignored).
     */
    public boolean hasConstructors() {
        return this.constructors != null;
    }

    /**
     * Set the constructors for the {@link ItemBuilderFactory}.
     *
     * <p>This can be used to dynamically set the {@link ItemBuilderBase} implementation at runtime depending on (e.g.,
     * API versions).
     *
     * @param constructors to use when creating an {@link ItemBuilderBase}.
     */
    public void setConstructors(@Nonnull ItemBuilderConstructors constructors) {
        // Do nothing if the constructors have already been done.
        if (hasConstructors()) {
            return;
        }

        this.constructors = Objects.requireNonNull(constructors);
    }

    /**
     * Get the constructors for the {@link ItemBuilderFactory}.
     *
     * @return constructors to use when creating an {@link ItemBuilderBase}.
     */
    @Nonnull
    private ItemBuilderConstructors getConstructors() {
        return Objects.requireNonNull(
                this.constructors, "The ItemBuilderFactory has not been configured with #setConstructors yet.");
    }

    @Override
    public ItemBuilderBase create(@Nonnull Material material) {
        return getConstructors().create(material);
    }

    @Override
    public ItemBuilderBase from(@Nonnull ItemStack stack) {
        return getConstructors().from(stack);
    }
}
