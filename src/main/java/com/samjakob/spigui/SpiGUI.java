package com.samjakob.spigui;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.inventory.SGInventoryListener;
import com.samjakob.spigui.inventory.SGOpenInventory;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.pagination.SGPaginationButtonBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * A comprehensive GUI API for Spigot with pages support.<br>
 * <a href="https://github.com/SamJakob/SpiGUI">https://github.com/SamJakob/SpiGUI</a>
 *
 * @author SamJakob
 * @version 2.0.0
 */
public class SpiGUI {

    private final JavaPlugin plugin;

    /**
     * Whether or not the inventory click actions should be
     * cancelled by default.
     *
     * This is typically set to true so events needn't be manually cancelled
     * every time an item is clicked in the inventory as that is the behavior
     * most typically used with an inventory GUI.
     *
     * With this set to true, you can of course use <code>event.setCancelled(false);</code> to
     * allow the default interaction.
     */
    private boolean blockDefaultInteractions = true;

    /**
     * Whether or not automatic pagination should be enabled.
     *
     * This is set to true by default and it means if you set an inventory slot
     * greater than the highest slot on the inventory, a row will automatically
     * be added containing pagination items that allow a user to scroll between
     * different 'pages' to access all of the assigned slots in the inventory.
     *
     * This concept is based on an improved version of the approach taken with
     * my SpigotPaginatedGUI library.
     */
    private boolean enableAutomaticPagination = true;

    /**
     * The defaultPaginationButtonBuilder is the plugin-wide {@link SGPaginationButtonBuilder}
     * called when building pagination buttons for inventory GUIs.
     *
     * This can be overridden per-inventory, as well as per-plugin using the appropriate methods
     * on either the inventory class ({@link SGInventory}) or your plugin's instance of
     * {@link SpiGUI}.
     */
    private SGPaginationButtonBuilder defaultPaginationButtonBuilder = (type, inventory) -> {
        switch (type) {
            case PREV_BUTTON:
                if (inventory.getCurrentPage() > 0) return new SGButton(new ItemBuilder(Material.ARROW)
                        .name("&a&l\u2190 Previous Page")
                        .lore(
                                "&aClick to move back to",
                                "&apage " + inventory.getCurrentPage() + ".")
                        .build()
                ).withListener(event -> {
                    event.setCancelled(true);
                    inventory.previousPage(event.getWhoClicked());
                });
                else return null;

            case CURRENT_BUTTON:
                return new SGButton(new ItemBuilder(Material.NAME_TAG)
                        .name("&7&lPage " + (inventory.getCurrentPage() + 1) + " of " + inventory.getMaxPage())
                        .lore(
                                "&7You are currently viewing",
                                "&7page " + (inventory.getCurrentPage() + 1) + "."
                        ).build()
                ).withListener(event -> event.setCancelled(true));

            case NEXT_BUTTON:
                if (inventory.getCurrentPage() < inventory.getMaxPage() - 1) return new SGButton(new ItemBuilder(Material.ARROW)
                        .name("&a&lNext Page \u2192")
                        .lore(
                                "&aClick to move forward to",
                                "&apage " + (inventory.getCurrentPage() + 2) + "."
                        ).build()
                ).withListener(event -> {
                    event.setCancelled(true);
                    inventory.nextPage(event.getWhoClicked());
                });
                else return null;

            case UNASSIGNED:
            default:
                return null;
        }
    };

    /**
     * Creates an instance of the SpiGUI library associated with a given plugin.
     * <br><br>
     * This is intended to be stored as a static field in your plugin with a public static
     * getter (or a public static field - dealer's choice) and you create inventories through
     * this class by calling {@link #create(String, int)} on the static {@link SpiGUI} field.
     *
     * A lengthy justification of this is provided as an implementation note, should you care
     * to read it.
     *
     * <br><br>
     *
     * @implNote The association with a plugin is an important design decision that was overlooked
     * in this library's predecessor, SpigotPaginatedGUI.
     * <br><br>
     * This library is not designed to act as a standalone plugin because that is inconvenient
     * for both developers and server administrators for such a relatively insignificant task - the
     * library is more just a small convenience measure. However, this library still needs to register
     * a listener under a given plugin, which is where the issue arises; which plugin should the
     * library use to register events with. Previously, it was whichever plugin made the call to
     * <code>PaginatedGUI.prepare</code> first, however this obviously causes problems if that
     * particular plugin is unloaded - as any other plugins using the library no longer have the
     * listener that was registered.
     * <br><br>
     * This approach was therefore considered a viable compromise - each plugin registers its own
     * listener, however the downside of this is that each inventory and the listener must now
     * also be registered with the plugin too.
     * <br><br>
     * Thus, the design whereby this class is registered as a static field on a {@link JavaPlugin}
     * instance and serves as a proxy for creating ({@link SGInventory}) inventories and an instance
     * of the {@link SGInventoryListener} registered with that plugin seemed like a good way to try
     * and minimize the inconvenience of the approach.
     *
     * @param plugin The plugin using SpiGUI.
     */
    public SpiGUI(JavaPlugin plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(
            new SGInventoryListener(plugin, this), plugin
        );
    }

    /**
     * An alias for {@link #create(String, int, String)} with the tag set to null.
     * Use this method if you don't need the tag, or you don't know what it's for.
     *
     * The rows parameter is used in place of the size parameter of the
     * Bukkit/Spigot inventory API. So, if you wanted an inventory of size
     * 27, you would supply 3 as the value of the rows parameter.
     *
     * <br><br>
     *
     * The <code>name</code> parameter supports the following 'placeholders':
     * <ul>
     * <li><code>{currentPage}</code>: the current page the inventory is on.</li>
     * <li><code>{maxPage}</code>: the final page of the inventory.</li>
     * </ul>
     *
     * @param name The display name of the inventory.
     * @param rows The number of rows the inventory should have per page.
     * @return The created inventory.
     */
    public SGInventory create(String name, int rows) {
        return create(name, rows, null);
    }

    /**
     * Creates an inventory with a given name, tag and number of rows.
     * The display name is color code translated.
     *
     * <br><br>
     *
     * The <code>name</code> parameter supports the following 'placeholders':
     * <ul>
     * <li><code>{currentPage}</code>: the current page the inventory is on.</li>
     * <li><code>{maxPage}</code>: the final page of the inventory.</li>
     * </ul>
     *
     * <br><br>
     *
     * The rows parameter is used in place of the size parameter of the
     * Bukkit/Spigot inventory API. So, if you wanted an inventory of size
     * 27, you would supply 3 as the value of the rows parameter.
     *
     * <br><br>
     *
     * The tag is used when getting all open inventories ({@link #findOpenWithTag(String)}) with your chosen tag.
     * An example of where this might be useful is with a permission GUI - when
     * the permissions are updated by one user in the GUI, it would be desirable to
     * refresh the state of the permissions GUI for all users observing the GUI.
     *
     * <br><br>
     *
     * You might give the permissions GUI a tag of 'myPermissionsGUI', then refreshing
     * all the open instances of the GUI would be as simple as getting all open inventories
     * with the aforementioned tag using {@link #findOpenWithTag(String)} and calling refresh
     * on each GUI in the list.
     *
     * <br><br>
     *
     * @param name The display name of the inventory.
     * @param rows The number of rows the inventory should have per page.
     * @return The created inventory.
     */
    public SGInventory create(String name, int rows, String tag) {
        return new SGInventory(plugin, this, name, rows, tag);
    }

    /**
     * @see SpiGUI#blockDefaultInteractions
     *
     * @param blockDefaultInteractions Whether or not default inventory interactions should be cancelled.
     */
    public void setBlockDefaultInteractions(boolean blockDefaultInteractions) {
        this.blockDefaultInteractions = blockDefaultInteractions;
    }

    /**
     * Returns the value of {@link SpiGUI#blockDefaultInteractions} for this plugin.
     *
     * @return Whether or not default inventory interactions should be cancelled.
     */
    public boolean areDefaultInteractionsBlocked() {
        return blockDefaultInteractions;
    }

    /**
     * @see SpiGUI#enableAutomaticPagination
     *
     * @param enableAutomaticPagination Whether or not automatic pagination should be enabled.
     */
    public void setEnableAutomaticPagination(boolean enableAutomaticPagination) {
        this.enableAutomaticPagination = enableAutomaticPagination;
    }

    /**
     * Returns the value of {@link SpiGUI#enableAutomaticPagination} for this plugin.
     *
     * @return Whether or not automatic pagination is enabled.
     */
    public boolean isAutomaticPaginationEnabled() {
        return enableAutomaticPagination;
    }

    /**
     * @see SpiGUI#defaultPaginationButtonBuilder
     *
     * @param defaultPaginationButtonBuilder The default pagination button builder used for GUIs.
     */
    public void setDefaultPaginationButtonBuilder(SGPaginationButtonBuilder defaultPaginationButtonBuilder) {
        this.defaultPaginationButtonBuilder = defaultPaginationButtonBuilder;
    }

    /**
     * @see SpiGUI#defaultPaginationButtonBuilder
     *
     * @return The default pagination button builder used for GUIs.
     */
    public SGPaginationButtonBuilder getDefaultPaginationButtonBuilder() {
        return defaultPaginationButtonBuilder;
    }

    /**
     * Finds a list of all open inventories with a given tag along with the
     * player who has that inventory open.
     *
     * This returns a list of {@link SGOpenInventory} which simply stores the
     * opened inventory along with the player viewing the open inventory.
     *
     * Supplying null as the tag value will get all untagged inventories.
     *
     * @param tag The tag to search for.
     * @return A list of {@link SGOpenInventory} whose inventories have the specified tag.
     */
    public List<SGOpenInventory> findOpenWithTag(String tag) {

        List<SGOpenInventory> foundInventories = new ArrayList<>();

        // Loop through every online player...
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            // ...if that player has an open inventory with a top inventory...
            if (player.getOpenInventory().getTopInventory() != null) {
                // ...get that top inventory.
                Inventory topInventory = player.getOpenInventory().getTopInventory();

                // If the top inventory is an SGInventory,
                if (topInventory.getHolder() != null && topInventory.getHolder() instanceof SGInventory) {
                    // and the SGInventory has the tag matching the one we're checking for,
                    SGInventory inventory = (SGInventory) topInventory.getHolder();
                    if (inventory.getTag().equals(tag))
                        // add the SGInventory to our list of found inventories.
                        foundInventories.add(new SGOpenInventory(inventory, player));
                }
            }
        }

        return foundInventories;

    }

}
