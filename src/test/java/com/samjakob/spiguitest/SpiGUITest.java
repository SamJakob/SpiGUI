package com.samjakob.spiguitest;

import com.samjakob.spigui.menu.SGMenu;
import com.samjakob.spigui.SpiGUI;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
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
    private final Map<Player, Integer> gems = new HashMap<>();
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

            // START DEFAULT INVENTORY

            // This is a menu intended to showcase general functionality.

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

            // END DEFAULT INVENTORY

            // The following are additional menus intended to test specific functionality:

            switch (args[0]) {
                case "inventorySizeTest": {
                    int size;

                    if (args.length == 1) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l&oERROR  &cYou must specify an item count as an integer."));
                        return true;
                    }

                    try {
                        size = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ex) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l&oERROR  &cThe item count must be a valid integer."));
                        return true;
                    }

                    // Create a menu with one row, so that pagination values are easy to calculate (each page is a
                    // multiple of 9, then the remainder can just be added to ensure the number of items match up).
                    SGMenu inventorySizeTest = SpiGUITest.getSpiGUI().create("Test Menu", 1);

                    IntStream.range(0, size).forEach(i -> inventorySizeTest.addButton(new SGButton(
                            new ItemBuilder(Material.GOLD_ORE).name(String.format("&6Item %d", i + 1))
                                    .build()
                    )));

                    player.openInventory(inventorySizeTest.getInventory());
                    return true;
                }

                case "refreshTest": {

                    SGMenu refreshTestMenu = SpiGUITest.getSpiGUI().create("&bMatches", 1);

                    // Generate 3 to 8 random matches.
                    List<Match> matches = IntStream.range(0, ThreadLocalRandom.current().nextInt(5) + 3)
                            .mapToObj((i) -> Match.generateFakeMatch(true))
                            .collect(Collectors.toList());

                    for (int i = 0; i < matches.size(); i++) {
                        Match match = matches.get(i);

                        refreshTestMenu.setButton(i, new SGButton(new ItemBuilder(match.getKit().getIcon())
                                .name(match.getKit().getName())
                                .lore(
                                    String.format("&a%s &evs. &a%s", match.getPlayerNames()[0], match.getPlayerNames()[1]),
                                    String.format("&fTime: &b%s", match.getTime()),
                                    "",
                                    String.format("&fKit: &b%s", match.getKit().getName()),
                                    String.format("&fArena: &b%s &7(%s)", match.getArena(), match.getKit().getName())
                                )
                                .build()));
                    }

                    // Start a refresh task for the menu.
                    AtomicReference<BukkitTask> refreshMatchesTask = new AtomicReference<>(new BukkitRunnable(){
                        @Override
                        public void run() {
                            for (int i = 0; i < matches.size(); i++) {
                                Match match = matches.get(i);

                                refreshTestMenu.setButton(i, new SGButton(new ItemBuilder(match.getKit().getIcon())
                                        .flag(ItemFlag.HIDE_ATTRIBUTES)
                                        .flag(ItemFlag.HIDE_DESTROYS)
                                        .flag(ItemFlag.HIDE_PLACED_ON)
                                        .flag(ItemFlag.HIDE_POTION_EFFECTS)
                                        .name(match.getKit().getName())
                                        .lore(
                                                String.format("&a%s &evs. &a%s", match.getPlayerNames()[0], match.getPlayerNames()[1]),
                                                String.format("&fTime: &b%s", match.getTime()),
                                                "",
                                                String.format("&fKit: &b%s", match.getKit().getName()),
                                                String.format("&fArena: &b%s &7(%s)", match.getArena(), match.getKit().getName())
                                        )
                                        .build()));
                            }

                            refreshTestMenu.refreshInventory(player);
                        }
                    }.runTaskTimer(this, 0L, 20L));

                    // Cancel the refresh task when the inventory is closed.
                    refreshTestMenu.setOnClose(menu -> {
                        if (refreshMatchesTask.get() != null) refreshMatchesTask.get().cancel();
                    });

                    player.openInventory(refreshTestMenu.getInventory());
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

    // The following is mock classes/data for the above test GUIs.

    private static class Kit {
        private final String name;
        private final ItemStack icon;

        public Kit(String name, ItemStack icon) {
            this.name = name;
            this.icon = icon;
        }

        public String getName() {
            return this.name;
        }

        public ItemStack getIcon() {
            return this.icon;
        }
    }

    private static class Match {

        private enum MatchState {
            /** Waiting to start. */
            WAITING,
            /** Currently ongoing. */
            ONGOING,
            /** Ended. */
            ENDED
        }

        // Begin mock data.
        private static final String[] fakePlayerNames = {"MoreHaro", "Pixelle", "SpyPlenty", "Winlink", "Herobrine", "Notch", "Dinnerbone", "CinnamonTown", "TreeMushrooms"};
        private static final Kit[] fakeKits = {
            new Kit("Classic Battle", new ItemBuilder(Material.STONE_SWORD).name("&7Classic Battle").build()),
            new Kit("OP Battle", new ItemBuilder(Material.DIAMOND_SWORD).name("&bOP Battle").build()),
            new Kit("Classic UHC", new ItemBuilder(Material.GOLDEN_APPLE).name("&eClassic UHC").build()),
            new Kit("OP UHC", new ItemBuilder(Material.GOLDEN_APPLE).data((short) 1).name("&6OP UHC").build()),
        };
        private static final String[] fakeArenas = {"King's Road", "Ilios", "Fort Starr", "The Hopper"};

        /** Generates a Match with fake data. */
        public static Match generateFakeMatch() { return generateFakeMatch(false); }

        public static Match generateFakeMatch(boolean alreadyStarted) {
            // Ensure unique values are generated for player1 and player2.
            int player1 = ThreadLocalRandom.current().nextInt(fakePlayerNames.length);
            int player2;
            do {
                player2 = ThreadLocalRandom.current().nextInt(fakePlayerNames.length);
            } while (player2 == player1);

            Match fakeMatch = new Match(
                new String[]{fakePlayerNames[player1], fakePlayerNames[player2]},
                fakeKits[ThreadLocalRandom.current().nextInt(fakeKits.length)],
                fakeArenas[ThreadLocalRandom.current().nextInt(fakeArenas.length)]
            );

            if (alreadyStarted) {
                // If alreadyStarted specified to true, then generate a match with current time minus up to 5 minutes.
                fakeMatch.matchStartTime = System.currentTimeMillis()
                        - ThreadLocalRandom.current().nextLong(5 * 60000);
            }

            return fakeMatch;
        }
        // End mock data.

        /** List of players in match. Two players implies a duel. */
        private final String[] playerNames;

        public String[] getPlayerNames() { return playerNames; }

        /** Match start time in UNIX milliseconds. */
        private Long matchStartTime;

        /** Match end time in UNIX milliseconds. */
        private Long matchEndTime;

        /** Name of the kit used for the duel. */
        private final Kit kit;

        public Kit getKit() { return kit; }

        /** Name of the arena used for the duel. */
        private final String arena;

        public String getArena() { return arena; }

        public String getTime() {
            switch (getState()) {
                case WAITING: return "Waiting...";
                case ONGOING:
                case ENDED: {
                    long duration = (matchEndTime != null ? matchEndTime : System.currentTimeMillis()) - matchStartTime;

                    long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes);
                    return String.format("%02d:%02d", minutes, seconds);
                }
            }

            return "ERROR";
        }

        public Match(String[] playerNames, Kit kit, String arena) {
            this.playerNames = playerNames;
            this.kit = kit;
            this.arena = arena;
        }

        public void start() {
            if (this.matchStartTime != null) throw new IllegalStateException("Match already started!");
            this.matchStartTime = System.currentTimeMillis();
        }

        public void stop() {
            if (this.matchEndTime != null) throw new IllegalStateException("Match already finished!");
            this.matchEndTime = System.currentTimeMillis();
        }

        public MatchState getState() {
            if (this.matchStartTime == null) return MatchState.WAITING;
            else if (this.matchEndTime == null) return MatchState.ONGOING;
            return MatchState.ENDED;
        }

    }

}
