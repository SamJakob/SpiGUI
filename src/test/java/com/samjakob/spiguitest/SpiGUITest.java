package com.samjakob.spiguitest;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.SpiGUI;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.pagination.SGToolbarButtonType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * SpiGUITest
 * <p>
 * Simple test plugin to showcase some of the functionality of SpiGUI.
 * You can build this from the main repository with the 'testJar' Gradle task.
 *
 * @author SamJakob
 * @version 1.3.0
 */
public class SpiGUITest extends JavaPlugin {

    /*
    Please feel free to use code from here. Though, do note that it is a very rough proof of concept intended to
    showcase and test some of the functionality of SpiGUI.
    */

    private static SpiGUI spiGUI;

    // Start: variables for demonstration purposes.
    private Map<Player, Integer> gems = new HashMap<>();
    // End: variables for demonstration purposes.

    @Override
    public void onEnable() {
        spiGUI = new SpiGUI(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getLabel().equalsIgnoreCase("spigui")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("[SpiGUI] [ERROR] You must be a player to run this command.");
                return true;
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                // Open a test SpiGUI menu.
                SGMenu myAwesomeMenu = SpiGUITest.getSpiGUI().create("&c&lSpiGUI &c(Page {currentPage}/{maxPage})", 3);

                myAwesomeMenu.setToolbarBuilder((slot, page, defaultType, menu) -> {
                    if (slot == 8) {
                        return new SGButton(
                            new ItemBuilder(Material.EMERALD)
                                .name(String.format("&a&l%d gems", gems.getOrDefault(player, 5)))
                                .lore(
                                    "&aUse gems to buy cosmetics",
                                    "&aand other items in the store!",
                                    "",
                                    "&7&o(Click to add more)"
                                )
                                .build()
                        ).withListener((event) -> {
                            gems.put(player, gems.getOrDefault(player, 5) + 5);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l&oSUCCESS!  &aYou have been given &25 &agems!"));
                            menu.refreshInventory(event.getWhoClicked());
                        });
                    }

                    // Fallback to rendering the default button for a slot.
                    return spiGUI.getDefaultToolbarBuilder().buildToolbarButton(slot, page, defaultType, menu);

                    // Or, alternatively, to render a button when NEITHER a custom per-inventory button OR a fallback
                    // button has been defined:
                    // (Comment above line and uncomment below to enable this)

                    /*

                    // Ensure fallbackButton is not null before rendering. If it is, render an alternative button
                    // instead.
                    SGButton fallbackButton = spiGUI.getDefaultToolbarBuilder().buildToolbarButton(slot, page, defaultType, menu);
                    if (fallbackButton != null) return fallbackButton;

                    return new SGButton(new ItemBuilder(Material.BARRIER).name(" ").build());

                    // You could check if defaultType is UNASSIGNED, however this won't deal with the cases when the
                    // previous or next button is not shown (there will be an empty space).

                     */
                });

                myAwesomeMenu.setButton(0, 10, new SGButton(
                        new ItemBuilder(Material.SKULL_ITEM)
                                .skullOwner(player.getName())
                                .name("&e&l" + player.getDisplayName())
                                .lore(
                                        "&eGame Mode: &6" + player.getGameMode().toString(),
                                        "&eLocation: &6" + String.format(
                                                "%.0f, %.0f, %.0f",
                                                player.getLocation().getX(),
                                                player.getLocation().getY(),
                                                player.getLocation().getZ()
                                        ),
                                        "&eExperience: &6" + player.getTotalExperience()
                                )
                                .build()
                ));

                myAwesomeMenu.setButton(1, 0, new SGButton(
                        new ItemBuilder(Material.GOLD_ORE)
                                .name("&6Get rich quick!")
                                .build()
                ).withListener(event -> {
                    Inventory playerInventory = event.getWhoClicked().getInventory();

                    IntStream.range(0, 9).forEach(hotBarSlot -> playerInventory.setItem(
                            hotBarSlot, new ItemBuilder(
                                    event.getCurrentItem().getType() == Material.GOLD_ORE
                                            ? Material.GOLD_BLOCK
                                            : event.getCurrentItem().getType()
                            ).amount(64).build()
                    ));

                    event.getWhoClicked().sendMessage(
                            ChatColor.translateAlternateColorCodes('&',
                                    event.getCurrentItem().getType() == Material.GOLD_ORE
                                            ? "&e&lYou are now rich!"
                                            : "&7&lYou are now poor."
                            )
                    );

                    Material newMaterial = event.getCurrentItem().getType() == Material.GOLD_ORE
                            ? Material.DIRT
                            : Material.GOLD_ORE;

                    myAwesomeMenu.getButton(1, 0).setIcon(
                            new ItemBuilder(newMaterial).name(
                                    newMaterial == Material.GOLD_ORE ? "&6Get rich quick!" : "&7Get poor quick!"
                            ).amount(1).build()
                    );

                    myAwesomeMenu.refreshInventory(event.getWhoClicked());
                    ((Player) event.getWhoClicked()).updateInventory();
                }));

                AtomicReference<BukkitTask> borderRunnable = new AtomicReference<>();

                myAwesomeMenu.setOnPageChange(inventory -> {
                    if (inventory.getCurrentPage() != 0) {
                        if (borderRunnable.get() != null) borderRunnable.get().cancel();
                    } else borderRunnable.set(
                            inventory.getCurrentPage() != 0
                                    ? null
                                    : new BukkitRunnable(){

                                private final int[] TILES_TO_UPDATE = {
                                        0,  1,  2,  3,  4,  5,  6,  7,  8,
                                        9,                             17,
                                        18, 19, 20, 21, 22, 23, 24, 25, 26
                                };

                                private short currentColor = 1;

                                @Override
                                public void run() {

                                    IntStream.range(0, TILES_TO_UPDATE.length).map(i -> TILES_TO_UPDATE.length - i + -1).forEach(
                                            index -> myAwesomeMenu.setButton(TILES_TO_UPDATE[index], nextColorButton())
                                    );

                                    currentColor++;
                                    if (currentColor >= 15) currentColor = 0;

                                    myAwesomeMenu.refreshInventory(player);

                                }

                                private SGButton nextColorButton() {
                                    return new SGButton(
                                            new ItemBuilder(Material.STAINED_GLASS_PANE)
                                                    .name("&" + Integer.toHexString(currentColor) + "&lSpiGUI!!!")
                                                    .data(currentColor)
                                                    .build()
                                    );
                                }

                            }.runTaskTimer(this, 0L, 20L)
                    );
                });

                myAwesomeMenu.setOnClose(inventory -> {
                    if (borderRunnable.get() != null) borderRunnable.get().cancel();
                });

                myAwesomeMenu.getOnPageChange().accept(myAwesomeMenu);
                player.openInventory(myAwesomeMenu.getInventory());

                return true;
            }

            if (args.length == 2) {
                if (args[0].equals("inventorySizeTest")) {
                    int size;
                    try {
                        size = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ex) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l&oERROR  &cThe inventory size must be a valid integer."));
                        return true;
                    }

                    SGMenu inventorySizeTest = SpiGUITest.getSpiGUI().create("Test Menu", 1);

                    IntStream.range(0, size).forEach(i -> inventorySizeTest.addButton(new SGButton(
                            new ItemBuilder(Material.GOLD_ORE)
                                    .build()
                    )));

                    player.openInventory(inventorySizeTest.getInventory());

                    return true;
                }
            }

            player.sendMessage("Unrecognized command.");
        }

        return false;
    }

    public static SpiGUI getSpiGUI() {
        return spiGUI;
    }

}
