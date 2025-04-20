package com.samjakob.spigui.toolbar;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.menu.SGMenu;

/**
 * An interface for a toolbar button builder.
 *
 * <p>The toolbar button builder is responsible for rendering the toolbar buttons for an {@link SGMenu}. This can be
 * customized to render different pagination items, etc., for a GUI.
 */
public interface SGToolbarBuilder {

    /**
     * Specifies the toolbar button builder for an {@link SGMenu}. This can be customized to render different toolbar
     * buttons for a GUI.
     *
     * <p>This method is called once per toolbar slot every time a page is rendered. To leave a slot empty, return null.
     *
     * @param slot The slot being rendered.
     * @param page The current page of the inventory being rendered.
     * @param defaultType The default button type of the current slot.
     * @param menu The inventory the toolbar is being rendered in.
     * @return The button to be rendered for that slot, or null if no button should be rendered.
     */
    @Nullable
    SGButton buildToolbarButton(int slot, int page, @Nonnull SGToolbarButtonType defaultType, @Nonnull SGMenu menu);

    /** Builds a flat (name) string for the given menu. */
    @FunctionalInterface
    interface NameBuilder {

        /**
         * Builder for the name of a toolbar item.
         *
         * @param menu that the item is being built for.
         * @return the toolbar item name, specified to the menu and its state.
         */
        @Nonnull
        String buildName(SGMenu menu);
    }

    /** Builds a string list (lore) for the given menu. */
    @FunctionalInterface
    interface LoreBuilder {

        /**
         * Builder for the lore of a toolbar item.
         *
         * @param menu that the item is being built for.
         * @return the toolbar item lore (description), specified to the menu and its state.
         */
        @Nonnull
        List<String> buildLore(SGMenu menu);
    }
}
