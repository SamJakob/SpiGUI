package com.samjakob.spigui.toolbar;

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
}
