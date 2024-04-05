package com.samjakob.spigui.menu;

import org.bukkit.entity.Player;

/**
 * Used to refer to a player's "viewing session" of a given menu.
 */
public class SGOpenMenu {

    /** The {@link SGMenu} that is currently open. */
    private final SGMenu gui;
    /** The player viewing the menu. */
    private final Player player;

    /**
     * Pairs an {@link SGMenu} instance with a player viewing that menu.
     * @param gui The {@link SGMenu} that is open.
     * @param player The player viewing the menu.
     */
    public SGOpenMenu(SGMenu gui, Player player) {
        this.gui = gui;
        this.player = player;
    }

    /**
     * Get the open {@link SGMenu} instance.
     * @return The menu that is open.
     */
    public SGMenu getMenu() {
        return this.gui;
    }

    /**
     * Get the player viewing the {@link SGMenu}.
     * @return The player viewing the menu.
     */
    public Player getPlayer() {
        return this.player;
    }

}
