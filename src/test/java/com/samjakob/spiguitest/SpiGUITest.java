package com.samjakob.spiguitest;

import com.samjakob.spigui.SGInventory;
import com.samjakob.spigui.SpiGUI;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpiGUITest extends JavaPlugin {

    private static SpiGUI spiGUI;

    @Override
    public void onEnable() {
        spiGUI = new SpiGUI(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("spigui")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to run this command.");
                return true;
            }

            Player player = (Player) sender;

            // Open a test SpiGUI menu.
            SGInventory spiGUI = SpiGUITest.getSpiGUI().create("&c&lSpiGUI &c(Page {currentPage}/{maxPage})", 3);

            spiGUI.addButton(new SGButton(
                new ItemBuilder(Material.WOOD).name("&aWood").build()
            ).withListener(event -> {
                event.getWhoClicked().sendMessage("wood message");
            }));

            spiGUI.setButton(0, 1, new SGButton(
                    new ItemBuilder(Material.WOOD).name("&aWood 2").build()
            ).withListener(event -> {
                event.getWhoClicked().sendMessage("wood message");
            }));

            spiGUI.setButton(1, 0, new SGButton(
                    new ItemBuilder(Material.WOOD).name("&aWood 3").build()
            ).withListener(event -> {
                event.getWhoClicked().sendMessage("wood message");
            }));

            spiGUI.setButton(28, new SGButton(
                    new ItemBuilder(Material.WOOD).name("&aWood 4").build()
            ).withListener(event -> {
                event.getWhoClicked().sendMessage("wood message");
            }));

            player.openInventory(spiGUI.getInventory());

            return true;
        }

        return false;
    }

    public static SpiGUI getSpiGUI() {
        return spiGUI;
    }
}
