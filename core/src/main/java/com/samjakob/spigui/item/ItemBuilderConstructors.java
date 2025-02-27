package com.samjakob.spigui.item;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Constructors that can be used by the {@link ItemBuilderFactory}.
 *
 * <p>These can be registered dynamically on the factory depending on which {@link ItemBuilderBase} implementation is
 * compatible.
 */
public interface ItemBuilderConstructors {

    /**
     * Create a new {@link ItemBuilderBase} for the given {@link Material} type.
     *
     * @throws IllegalArgumentException if the material is a non-item type.
     * @param material type of stack to create a builder for.
     * @return the constructed {@link ItemBuilderBase}.
     */
    ItemBuilderBase create(@Nonnull Material material);

    /**
     * Create a new {@link ItemBuilderBase} that uses the provided {@link ItemStack} and associated metadata as the
     * initial configuration.
     *
     * @throws IllegalArgumentException if the item stack's type (material) is a non-item type.
     * @param stack to derive the builder options from.
     * @return the constructed {@link ItemBuilderBase}.
     */
    ItemBuilderBase from(@Nonnull ItemStack stack);
}
