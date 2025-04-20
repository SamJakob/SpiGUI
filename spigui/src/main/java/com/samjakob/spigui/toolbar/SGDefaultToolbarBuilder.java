package com.samjakob.spigui.toolbar;

import javax.annotation.Nonnull;

import org.bukkit.Material;

import com.samjakob.spigui.item.ItemBuilder;

public class SGDefaultToolbarBuilder extends SGDefaultToolbarBuilderBase {

    /**
     * Ensures that the {@link SGDefaultToolbarBuilder} has been registered on the
     * {@link SGDefaultToolbarBuilderFactory}.
     */
    public static void register() {
        if (!SGDefaultToolbarBuilderFactory.get().hasSupplier()) {
            SGDefaultToolbarBuilderFactory.get().setSupplier(SGDefaultToolbarBuilder::new);
        }
    }

    @Nonnull
    @Override
    protected ItemBuilder initializePreviousPageButton() {
        return ItemBuilder.create(Material.ARROW);
    }

    @Nonnull
    @Override
    protected ItemBuilder initializeCurrentPageIndicator() {
        return ItemBuilder.create(Material.NAME_TAG);
    }

    @Nonnull
    @Override
    protected ItemBuilder initializeNextPageButton() {
        return ItemBuilder.create(Material.ARROW);
    }
}
