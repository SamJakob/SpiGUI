package com.samjakob.spigui.buttons;

import org.bukkit.event.inventory.InventoryClickEvent;

/** Holds the event handler for an SGButton. */
public interface SGButtonListener {

    /**
     * The event handler that should be executed when an SGButton is clicked. Implement this with a lambda when you
     * create an SGButton.
     *
     * @param event The Bukkit/Spigot APIs {@link InventoryClickEvent}.
     */
    void onClick(InventoryClickEvent event);
}
