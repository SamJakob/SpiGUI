package com.samjakob.spigui.menu;

import org.bukkit.entity.Player;

/**
 * Used to refer to an open menu.
 */
public class SGOpenMenu {

    private final SGMenu gui;
    private final Player player;

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
