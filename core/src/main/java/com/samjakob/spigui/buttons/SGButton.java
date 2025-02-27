package com.samjakob.spigui.buttons;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * An SGButton represents a clickable item in an SGMenu (GUI). It consists of an icon ({@link ItemStack}) and a listener
 * ({@link SGButton}).
 *
 * <p>When the icon is clicked in the SGMenu, the listener is called, thus allowing for rudimentary menus to be built by
 * displaying icons and overriding their behavior.
 *
 * <p>This somewhat resembles the point-and-click nature of Graphical User Interfaces (GUIs) popularized by Operating
 * Systems developed in the late 80s and 90s which is where the name of the concept in Spigot plugins was derived.
 */
public class SGButton {

    /** The on-click handler for this button. */
    @Nullable
    private SGButtonListener listener;

    /** The Bukkit {@link ItemStack} that will be used as the button's icon. */
    @Nonnull
    private ItemStack icon;

    /**
     * Creates an SGButton with the specified {@link ItemStack} as it's 'icon' in the inventory.
     *
     * @param icon The desired 'icon' for the SGButton.
     */
    public SGButton(@Nonnull ItemStack icon) {
        this.icon = validateIcon(icon);
    }

    /**
     * Sets the {@link SGButtonListener} to be called when the button is clicked.
     *
     * @param listener The listener to be called when the button is clicked.
     */
    public void setListener(@Nullable SGButtonListener listener) {
        this.listener = listener;
    }

    /**
     * A chainable alias of {@link #setListener(SGButtonListener)}.
     *
     * @param listener The listener to be called when the button is clicked.
     * @return The {@link SGButton} the listener was applied to.
     */
    public SGButton withListener(@Nullable SGButtonListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Returns the {@link SGButtonListener} that is to be executed when the button is clicked.
     *
     * <p>This is typically intended for internal use by the main {@link com.samjakob.spigui.SpiGUI} API.
     *
     * @return The listener to be called when the button is clicked.
     */
    @Nullable
    public SGButtonListener getListener() {
        return listener;
    }

    /**
     * Returns the {@link ItemStack} that will be used as the SGButton's icon in the SGMenu (GUI).
     *
     * @return The icon ({@link ItemStack}) that will be used to represent the button.
     */
    @Nonnull
    public ItemStack getIcon() {
        return icon;
    }

    /**
     * Changes the SGButton's icon.
     *
     * @param icon The icon ({@link ItemStack}) that will be used to represent the button.
     */
    public void setIcon(@Nonnull ItemStack icon) {
        this.icon = validateIcon(icon);
    }

    /**
     * Ensure that the {@link ItemStack} will be a suitable icon.
     *
     * @param icon to check.
     * @return the icon, if it is suitable.
     * @throws IllegalArgumentException if the icon is not suitable.
     * @throws NullPointerException if the icon is null.
     */
    @Nonnull
    private ItemStack validateIcon(@Nonnull ItemStack icon) {
        if (icon.getType() == Material.AIR) {
            throw new IllegalArgumentException("Cannot use AIR as icon.");
        }

        return Objects.requireNonNull(icon, "Don't use a null icon - remove the button instead.");
    }
}
