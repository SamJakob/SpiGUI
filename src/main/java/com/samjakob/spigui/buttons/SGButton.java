package com.samjakob.spigui.buttons;

import org.bukkit.inventory.ItemStack;

/**
 * An SGButton represents a clickable item in an SGMenu (GUI).
 * It consists of an icon ({@link ItemStack}) and a listener ({@link SGButton}).
 *
 * When the icon is clicked in the SGMenu, the listener is called, thus allowing
 * for rudimentary menus to be built by displaying icons and overriding their behavior.
 *
 * This somewhat resembles the point-and-click nature of Graphical User Interfaces (GUIs)
 * popularized by Operating Systems developed in the late 80s and 90s which is where the
 * name of the concept in Spigot plugins was derived.
 */
public class SGButton {

    private SGButtonListener listener;
    private ItemStack icon;

    /**
     * Creates an SGButton with the specified {@link ItemStack} as it's 'icon' in the inventory.
     *
     * @param icon The desired 'icon' for the SGButton.
     */
    public SGButton(ItemStack icon){
        this.icon = icon;
    }

    /**
     * Sets the {@link SGButtonListener} to be called when the button is clicked.
     * @param listener The listener to be called when the button is clicked.
     */
    public void setListener(SGButtonListener listener) {
        this.listener = listener;
    }

    /**
     * A chainable alias of {@link #setListener(SGButtonListener)}.
     *
     * @param listener The listener to be called when the button is clicked.
     * @return The {@link SGButton} the listener was applied to.
     */
    public SGButton withListener(SGButtonListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Returns the {@link SGButtonListener} that is to be executed when the button
     * is clicked.<br>
     * This is typically intended for use by the API.
     *
     * @return The listener to be called when the button is clicked.
     */
    public SGButtonListener getListener() {
        return listener;
    }

    /**
     * Returns the {@link ItemStack} that will be used as the SGButton's icon in the
     * SGMenu (GUI).
     *
     * @return The icon ({@link ItemStack}) that will be used to represent the button.
     */
    public ItemStack getIcon() {
        return icon;
    }

    /**
     * Changes the SGButton's icon.
     *
     * @param icon The icon ({@link ItemStack}) that will be used to represent the button.
     */
    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

}
