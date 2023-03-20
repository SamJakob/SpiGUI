package com.samjakob.spigui.menu;

import com.samjakob.spigui.SGMenu;
import com.samjakob.spigui.SpiGUI;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.pagination.SGToolbarBuilder;
import com.samjakob.spigui.pagination.SGToolbarButtonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class SGMenuListener implements Listener {

    private final JavaPlugin owner;
    private final SpiGUI spiGUI;

    public SGMenuListener(JavaPlugin owner, SpiGUI spiGUI) {
        this.owner = owner;
        this.spiGUI = spiGUI;
    }

    /**
     * Returns false if the specified inventory exists and is an SGMenu,
     * as that implies the inventory event should be handled by this
     * {@link SGMenuListener} class.
     * <p>
     * Alternatively, if the inventory is null, or the inventory is not
     * associated with the {@link SGMenu} class (i.e., <code>inventory.getHolder</code>
     * is null), this returns true signalling that the event should not
     * be processed.
     *
     * @param inventory The inventory to check.
     * @return False if inventory event should be handled by {@link SGMenuListener}, true if not.
     */
    private static boolean shouldIgnoreInventoryEvent(Inventory inventory) {
        // Note that this is inverted, as all of its uses are inverted.
        // That is, we check if the inventory is a valid SGMenu and then
        // negate that to return false if it is (because a valid SGMenu
        // should be handled by the library).
        return !(inventory != null &&
                 inventory.getHolder() != null &&
                 inventory.getHolder() instanceof SGMenu);
    }

    /**
     * Allows a plugin to determine whether an inventory event will be
     * handled by SpiGUI.
     * <p>
     * You can use this to override SpiGUI's event handlers (in
     * {@link SGMenuListener}) with custom functionality, should you need
     * to.
     * <p>
     * The plugin parameter refers to your plugin, the one using SpiGUI.
     * It needs to be passed in here so that SpiGUI can check whether
     * the {@link SGMenu} inventory belongs to your plugin.
     *
     * @param plugin The {@link JavaPlugin} plugin instance.
     * @param inventory The inventory to check.
     * @return True if it will, otherwise false.
     */
    public static boolean willHandleInventoryEvent(JavaPlugin plugin, Inventory inventory) {
        return !shouldIgnoreInventoryEvent(inventory) && ((SGMenu) inventory.getHolder()).getOwner().equals(plugin);
    }

    /**
     * Overrides the click event for an SGMenu. This is automatically
     * registered into a plugin using SpiGUI when SpiGUI is initialized
     * in that plugin.
     * <p>
     * The respective inventory is first checked to ensure that it is a
     * SpiGUI {@link SGMenu} and, if it is, whether a pagination button
     * was clicked, finally (if not a pagination button) the event is
     * delegated to the inventory the click occurred in.
     *
     * @param event The event to handle.
     * @see SpiGUI#SpiGUI(JavaPlugin)
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        // This should only run for SpiGUI menus, so if the clicked
        // inventory was not a SpiGUI menu (i.e., an SGMenu), don't
        // continue.
        if (shouldIgnoreInventoryEvent(event.getClickedInventory())) return;

        // Get the instance of the SpiGUI that was clicked.
        SGMenu clickedGui = (SGMenu) event.getClickedInventory().getHolder();

        // Check if the GUI is owner by the current plugin
        // (if not, it'll be deferred to the SGMenuListener registered
        // by that plugin that does own the GUI.)
        if (!clickedGui.getOwner().equals(owner)) return;

        // If the default action is to cancel the event (block default interactions)
        // we'll do that now.
        // The inventory's value is checked first, so it can be overridden on a
        // per-inventory basis. If the inventory's value is null, the plugin's
        // default value is checked.
        if (clickedGui.areDefaultInteractionsBlocked() != null) {
            event.setCancelled(clickedGui.areDefaultInteractionsBlocked());
        } else {
            // Note that this can be overridden by a call to #setCancelled(false) in
            // the button's event handler.
            if (spiGUI.areDefaultInteractionsBlocked())
                event.setCancelled(true);
        }

        // If the slot is on the pagination row, get the appropriate pagination handler.
        if (event.getSlot() > clickedGui.getPageSize()) {
            int offset = event.getSlot() - clickedGui.getPageSize();
            SGToolbarBuilder paginationButtonBuilder = spiGUI.getDefaultToolbarBuilder();

            if (clickedGui.getToolbarBuilder() != null) {
                paginationButtonBuilder = clickedGui.getToolbarBuilder();
            }

            SGToolbarButtonType buttonType = SGToolbarButtonType.getDefaultForSlot(offset);
            SGButton paginationButton = paginationButtonBuilder.buildToolbarButton(offset, clickedGui.getCurrentPage(), buttonType, clickedGui);
            if (paginationButton != null) paginationButton.getListener().onClick(event);
            return;
        }

        // If the slot is a stickied slot, get the button from page 0.
        if (clickedGui.isStickiedSlot(event.getSlot())) {
            SGButton button = clickedGui.getButton(0, event.getSlot());
            if (button != null && button.getListener() != null) button.getListener().onClick(event);
            return;
        }

        // Otherwise, get the button normally.
        SGButton button = clickedGui.getButton(clickedGui.getCurrentPage(), event.getSlot());
        if (button != null && button.getListener() != null) {
            button.getListener().onClick(event);
        }

    }

    /**
     * Overrides the drag event for an SGMenu. This is automatically
     * registered into a plugin using SpiGUI when SpiGUI is initialized
     * in that plugin.
     * <p>
     * The respective inventory is first checked to ensure that it is a
     * SpiGUI {@link SGMenu} and, if it is, the drag event is captured
     * and cancelled.
     * <p>
     * If this behavior is undesirable, it can be overridden by creating
     * an {@link InventoryDragEvent} handler of your own - which you can
     * do by registering an event handler and checking
     * {@link SGMenuListener#willHandleInventoryEvent(JavaPlugin, Inventory)}
     * returns true. You can identify the specific inventory with
     * {@link SGMenu#getTag()}.
     *
     * @param event The event to handle.
     * @see SpiGUI#SpiGUI(JavaPlugin) 
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryDrag(InventoryDragEvent event) {

        // This should only run for SpiGUI menus, so if the clicked
        // inventory was not a SpiGUI menu (i.e., an SGMenu), don't
        // continue.
        if (shouldIgnoreInventoryEvent(event.getInventory())) return;

        // Simply cancel the event.
        event.setCancelled(true);

    }

    /**
     * Overrides the close event for an SGMenu. This is automatically
     * registered into a plugin using SpiGUI when SpiGUI is initialized
     * in that plugin.
     * <p>
     * The respective inventory is first checked to ensure that it is a
     * SpiGUI {@link SGMenu} and, if it is, the close event is delegated
     * to a handler for that inventory, if one exists.
     *
     * @param event The event to handle.
     * @see SpiGUI#SpiGUI(JavaPlugin)
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        // This should only run for SpiGUI menus, so if the clicked
        // inventory was not a SpiGUI menu (i.e., an SGMenu), don't
        // continue.
        if (shouldIgnoreInventoryEvent(event.getInventory())) return;

        // Get the instance of the SpiGUI that was clicked.
        SGMenu clickedGui = (SGMenu) event.getInventory().getHolder();

        // Check if the GUI is owner by the current plugin
        // (if not, it'll be deferred to the SGMenuListener registered
        // by that plugin that does own the GUI.)
        if (!clickedGui.getOwner().equals(owner)) return;

        // If all the above is true and the inventory's onClose is not null,
        // call it.
        if (clickedGui.getOnClose() != null)
            clickedGui.getOnClose().accept(clickedGui);
    }

}
