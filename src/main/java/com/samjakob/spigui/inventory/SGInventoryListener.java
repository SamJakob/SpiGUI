package com.samjakob.spigui.inventory;

import com.samjakob.spigui.SGInventory;
import com.samjakob.spigui.SpiGUI;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.pagination.SGPaginationButtonBuilder;
import com.samjakob.spigui.pagination.SGPaginationButtonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SGInventoryListener implements Listener {

    private final JavaPlugin owner;
    private final SpiGUI spiGUI;

    public SGInventoryListener(JavaPlugin owner, SpiGUI spiGUI) {
        this.owner = owner;
        this.spiGUI = spiGUI;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        // Determine if the inventory was a SpiGUI.
        if (event.getInventory().getHolder() != null
            && event.getInventory().getHolder() instanceof SGInventory) {

            // Get the instance of the SpiGUI that was clicked.
            SGInventory clickedGui = (SGInventory) event.getInventory().getHolder();

            // Check if the GUI is owner by the current plugin
            // (if not, it'll be deferred to the SGInventoryListener registered
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
                SGPaginationButtonBuilder paginationButtonBuilder = spiGUI.getDefaultPaginationButtonBuilder();

                if (clickedGui.getPaginationButtonBuilder() != null) {
                    paginationButtonBuilder = clickedGui.getPaginationButtonBuilder();
                }

                SGPaginationButtonType buttonType = SGPaginationButtonType.forSlot(offset);
                paginationButtonBuilder.buildPaginationButton(buttonType, clickedGui).getListener().onClick(event);
                return;
            }
            
            // If the slot is a stickied slot, get the button from page 0.
            if (clickedGui.isStickiedSlot(event.getSlot())) {
                SGButton button = clickedGui.getButton(0, event.getSlot());
                if (button != null && button.getListener() != null) button.getListener().onClick(event);
                return;
            }

            // Otherwise, get the button normally.
            SGButton button = clickedGui.getButton(event.getSlot());
            if (button != null && button.getListener() != null) {
                button.getListener().onClick(event);
            }

        }

    }

}