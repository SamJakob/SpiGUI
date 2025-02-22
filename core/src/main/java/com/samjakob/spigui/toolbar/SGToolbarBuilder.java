package com.samjakob.spigui.toolbar;

import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.buttons.SGButton;

/**
 * An interface for a toolbar button builder.
 *
 * <p>
 * The toolbar button builder is responsible for rendering the toolbar buttons
 * for an {@link SGMenu}. This can be customized to render different pagination
 * items, etc., for a GUI.
 * </p>
 */
public interface SGToolbarBuilder {

    /**
     * Specifies the toolbar button builder for an {@link SGMenu}.
     * This can be customized to render different toolbar buttons for a GUI.
     *
     * @param slot The slot being rendered.
     * @param page The current page of the inventory being rendered.
     * @param defaultType The default button type of the current slot.
     * @param menu The inventory the toolbar is being rendered in.
     * @return The button to be rendered for that slot, or null if no
     * button should be rendered.
     */
    SGButton buildToolbarButton(int slot, int page, SGToolbarButtonType defaultType, SGMenu menu);

}
