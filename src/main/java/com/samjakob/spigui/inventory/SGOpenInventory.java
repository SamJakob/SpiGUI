package com.samjakob.spigui.inventory;

import com.samjakob.spigui.SGInventory;
import org.bukkit.entity.Player;

public class SGOpenInventory {

    private final SGInventory gui;
    private final Player player;

    public SGOpenInventory(SGInventory gui, Player player) {
        this.gui = gui;
        this.player = player;
    }

    public SGInventory getGUI() {
        return this.gui;
    }

    public Player getPlayer() {
        return this.player;
    }

}
