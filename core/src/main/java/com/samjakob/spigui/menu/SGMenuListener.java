package com.samjakob.spigui.menu;

import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

import com.samjakob.spigui.SpiGUI;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.toolbar.SGToolbarBuilder;
import com.samjakob.spigui.toolbar.SGToolbarButtonType;

/**
 * The {@link SGMenuListener} handles SpiGUI events on behalf of a plugin that uses SpiGUI.
 *
 * <p>You must register this class as an event listener in your plugin's <code>onEnable</code> method by initializing
 * SpiGUI there and passing your plugin instance to SpiGUI's constructor.
 */
public class SGMenuListener implements Listener {

    /** The plugin that this listener is registered for. */
    private final JavaPlugin owner;

    /** The instance of {@link SpiGUI} this listener is operating for. */
    private final SpiGUI spiGUI;

    /**
     * Initialize an SGMenuListener for the specified plugin.
     *
     * @param owner The plugin that this listener is registered for.
     * @param spiGUI The instance of {@link SpiGUI} this listener is operating for.
     */
    public SGMenuListener(@Nonnull JavaPlugin owner, @Nonnull SpiGUI spiGUI) {
        this.owner = Objects.requireNonNull(owner);
        this.spiGUI = Objects.requireNonNull(spiGUI);
    }

    /**
     * Returns false if the specified inventory exists and is an SGMenu, as that implies the inventory event should be
     * handled by this {@link SGMenuListener} class.
     *
     * <p>Alternatively, if the inventory is null, or the inventory is not associated with the {@link SGMenu} class
     * (i.e., <code>inventory.getHolder</code> is null), this returns true signalling that the event should not be
     * processed.
     *
     * @param inventory The inventory to check.
     * @return False if inventory event should be handled by {@link SGMenuListener}, true if not.
     */
    private static boolean shouldIgnoreInventoryEvent(@Nullable Inventory inventory) {
        // Note that this is inverted, as all of its uses are inverted.
        // That is, we check if the inventory is a valid SGMenu and then
        // negate that to return false if it is (because a valid SGMenu
        // should be handled by the library).
        return !(inventory != null && inventory.getHolder() != null && inventory.getHolder() instanceof SGMenu);
    }

    /**
     * Allows a plugin to determine whether an inventory event will be handled by SpiGUI.
     *
     * <p>You can use this to override SpiGUI's event handlers (in {@link SGMenuListener}) with custom functionality,
     * should you need to.
     *
     * <p>The plugin parameter refers to your plugin, the one using SpiGUI. It needs to be passed in here so that SpiGUI
     * can check whether the {@link SGMenu} inventory belongs to your plugin. The plugin cannot be null as this would
     * prevent any inventory event from matching, so this method will throw if plugin is null.
     *
     * @param plugin The {@link JavaPlugin} plugin instance.
     * @param inventory The inventory to check.
     * @return True if it will, otherwise false.
     */
    public static boolean willHandleInventoryEvent(@Nonnull JavaPlugin plugin, @Nullable Inventory inventory) {
        // If the event should not be ignored (i.e., it is a SpiGUI menu) and
        // if the inventory's holder is an SGMenu owned by the same plugin as
        // this listener, return true to indicate that this listener will
        // handle the event.
        return !shouldIgnoreInventoryEvent(inventory)
                && Objects.equals(((SGMenu) inventory.getHolder()).getOwner(), Objects.requireNonNull(plugin));
    }

    /**
     * Overrides the click event for an SGMenu. This is automatically registered into a plugin using SpiGUI when SpiGUI
     * is initialized in that plugin.
     *
     * <p>The respective inventory is first checked to ensure that it is a SpiGUI {@link SGMenu} and, if it is, whether
     * a pagination button was clicked, finally (if not a pagination button) the event is delegated to the inventory the
     * click occurred in.
     *
     * @param event The event to handle.
     * @see SpiGUI#SpiGUI(JavaPlugin)
     */
    @EventHandler
    public void onInventoryClick(@Nonnull InventoryClickEvent event) {
        // This should only run for SpiGUI menus, so if the clicked
        // inventory was not a SpiGUI menu (i.e., an SGMenu), don't
        // continue.
        if (shouldIgnoreInventoryEvent(event.getClickedInventory())) return;

        // Get the instance of the SpiGUI that was clicked.
        SGMenu clickedGui = (SGMenu) event.getClickedInventory().getHolder();

        // If the click type is not permitted, instantly deny the event and
        // do nothing else.
        if (clickedGui.getPermittedMenuClickTypes().stream().noneMatch(type -> type == event.getClick())) {
            event.setResult(Event.Result.DENY);
            return;
        }

        // If the action is blocked, instantly deny the event
        if (clickedGui.getBlockedMenuActions().stream().anyMatch(action -> action == event.getAction())) {
            event.setResult(Event.Result.DENY);
        }

        // Check if the GUI is owner by the current plugin
        // (if not, it'll be deferred to the SGMenuListener registered
        // by that plugin that does own the GUI.)
        if (!clickedGui.getOwner().equals(owner)) return;

        // If the default action is to cancel the event (block default interactions)
        // we'll do that now.
        // The inventory's value is checked first, so it can be overridden on a
        // per-inventory basis. If the inventory's value is null, the plugin's
        // default value is checked.
        boolean shouldCancel =
                (clickedGui.areDefaultInteractionsBlocked() != null && clickedGui.areDefaultInteractionsBlocked())
                        || spiGUI.areDefaultInteractionsBlocked();

        if (shouldCancel) {
            event.setResult(Event.Result.DENY);
        }

        // If the slot is on the pagination row, get the appropriate pagination handler.
        if (event.getSlot() > clickedGui.getPageSize()) {
            int offset = event.getSlot() - clickedGui.getPageSize();
            SGToolbarBuilder paginationButtonBuilder = spiGUI.getDefaultToolbarBuilder();

            if (clickedGui.getToolbarBuilder() != null) {
                paginationButtonBuilder = clickedGui.getToolbarBuilder();
            }

            SGToolbarButtonType buttonType = SGToolbarButtonType.getDefaultForSlot(offset);
            SGButton paginationButton = paginationButtonBuilder.buildToolbarButton(
                    offset, clickedGui.getCurrentPage(), buttonType, clickedGui);
            if (paginationButton != null && paginationButton.getListener() != null)
                paginationButton.getListener().onClick(event);
            return;
        }

        // If the slot is a stickied slot, get the button from page 0.
        if (clickedGui.isStickiedSlot(event.getSlot())) {
            SGButton button = clickedGui.getButton(0, event.getSlot());
            if (button != null && button.getListener() != null)
                button.getListener().onClick(event);
            return;
        }

        // Otherwise, get the button normally.
        SGButton button = clickedGui.getButton(clickedGui.getCurrentPage(), event.getSlot());
        if (button != null && button.getListener() != null) {
            button.getListener().onClick(event);
        }
    }

    /**
     * Blocks events that occur in adjacent inventories to an SGMenu when those events would affect the SGMenu. (For
     * example, double-clicking on an item in the player's inventory that is the same as an item in the SGMenu.)
     *
     * @param event The event to handle.
     * @see SpiGUI#SpiGUI(JavaPlugin)
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAdjacentInventoryClick(@Nonnull InventoryClickEvent event) {
        // If the clicked inventory is not adjacent to a SpiGUI menu, ignore
        // the click event.
        if (event.getView().getTopInventory() == null
                || shouldIgnoreInventoryEvent(event.getView().getTopInventory())) return;

        // If the clicked inventory is the SpiGUI menu (the top inventory),
        // ignore the click event.
        if (event.getClickedInventory() == event.getView().getTopInventory()) return;

        // Get the instance of the SpiGUI that was clicked.
        SGMenu clickedGui = (SGMenu) event.getView().getTopInventory().getHolder();

        if (clickedGui == null) return;

        // If the clicked inventory is not a SpiGUI menu, block the event if
        // it is one of the blocked actions.
        if (clickedGui.getBlockedAdjacentActions().stream().anyMatch(action -> action == event.getAction())) {
            event.setResult(Event.Result.DENY);
        }
    }

    /**
     * Overrides the drag event for an SGMenu. This is automatically registered into a plugin using SpiGUI when SpiGUI
     * is initialized in that plugin.
     *
     * <p>The respective inventory is first checked to ensure that it is a SpiGUI {@link SGMenu} and, if it is, the drag
     * event is captured and cancelled.
     *
     * <p>If this behavior is undesirable, it can be overridden by creating an {@link InventoryDragEvent} handler of
     * your own - which you can do by registering an event handler and checking
     * {@link SGMenuListener#willHandleInventoryEvent(JavaPlugin, Inventory)} returns true. You can identify the
     * specific inventory with {@link SGMenu#getTag()}.
     *
     * @param event The event to handle.
     * @see SpiGUI#SpiGUI(JavaPlugin)
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryDrag(@Nonnull InventoryDragEvent event) {

        // This should only run for SpiGUI menus, so if the clicked
        // inventory was not a SpiGUI menu (i.e., an SGMenu), don't
        // continue.
        if (shouldIgnoreInventoryEvent(event.getInventory())) return;

        // Get the instance of the SpiGUI that was clicked.
        SGMenu clickedGui = (SGMenu) event.getInventory().getHolder();

        // Cancel the drag event if any of the affected slots are in the
        // SpiGUI menu (the top inventory).
        if (slotsIncludeTopInventory(event.getView(), event.getRawSlots())) {
            event.setResult(Event.Result.DENY);
        }
    }

    /**
     * Overrides the close event for an SGMenu. This is automatically registered into a plugin using SpiGUI when SpiGUI
     * is initialized in that plugin.
     *
     * <p>The respective inventory is first checked to ensure that it is a SpiGUI {@link SGMenu} and, if it is, the
     * close event is delegated to a handler for that inventory, if one exists.
     *
     * @param event The event to handle.
     * @see SpiGUI#SpiGUI(JavaPlugin)
     */
    @EventHandler
    public void onInventoryClose(@Nonnull InventoryCloseEvent event) {

        // This should only run for SpiGUI menus, so if the clicked
        // inventory was not a SpiGUI menu (i.e., an SGMenu), don't
        // continue.
        if (shouldIgnoreInventoryEvent(event.getInventory())) return;

        // Get the instance of the SpiGUI that was clicked.
        SGMenu clickedGui = (SGMenu) event.getInventory().getHolder();

        // Check if the GUI is owner by the current plugin
        // (if not, it'll be deferred to the SGMenuListener registered
        // by that plugin that does own the GUI.)
        if (!Objects.equals(clickedGui.getOwner(), owner)) return;

        // If all the above is true and the inventory's onClose is not null,
        // call it.
        if (clickedGui.getOnClose() != null) clickedGui.getOnClose().accept(clickedGui);
    }

    /**
     * Checks whether the specified set of slots includes any slots in the top inventory of the specified
     * {@link InventoryView}.
     *
     * @param view The relevant {@link InventoryView}.
     * @param slots The set of slots to check.
     * @return True if the set of slots includes any slots in the top inventory, otherwise false.
     */
    private boolean slotsIncludeTopInventory(@Nonnull InventoryView view, @Nonnull Set<Integer> slots) {
        return slots.stream().anyMatch(slot -> {
            // If the slot is bigger than the SpiGUI menu's page size,
            // it's a pagination button, so we'll ignore it.
            if (slot >= view.getTopInventory().getSize()) return false;
            // Otherwise, we'll check if the slot's converted value matches
            // its raw value. If it matches, it means the slot is in the
            // SpiGUI menu, so we'll return true.
            return slot == view.convertSlot(slot);
        });
    }
}
