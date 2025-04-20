package com.samjakob.spigui.toolbar;

import java.util.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents pre-defined types for toolbar buttons. These can be used to easily re-define the buttons used in a
 * toolbar, without creating an entirely custom toolbar implementation.
 */
public enum SGToolbarButtonType {

    /** The "previous page" pagination button. */
    PREV_BUTTON(3),

    /** The "current page" indicator button (doesn't necessarily have an action associated). */
    CURRENT_BUTTON(4),

    /** The "next page" pagination button. */
    NEXT_BUTTON(5),

    /** No pre-defined action or button. */
    UNASSIGNED(null);

    /** The default slot for the button, or null. */
    @Nullable
    private final Integer defaultSlot;

    /**
     * A pre-defined toolbar type mapping. These can be used to easily re-define the buttons used in a toolbar, without
     * creating an entirely custom toolbar implementation.
     *
     * @param defaultSlot to position the button with the specified type in (or null).
     */
    SGToolbarButtonType(@Nullable Integer defaultSlot) {
        this.defaultSlot = defaultSlot;
    }

    /**
     * Returns the default slot mapping for the given toolbar button type.
     *
     * @return the default slot for the toolbar button type.
     */
    @Nullable
    public Integer getDefaultSlot() {
        return defaultSlot;
    }

    /**
     * Convenience method that unboxes {@link #getDefaultSlot()} after null-checking it. Use this method when you can
     * guarantee that the type should have an assigned slot (i.e., that it will never be {@link #UNASSIGNED}).
     *
     * <p>If in doubt, use {@link #getDefaultSlot()} and handle the case where it is null.
     *
     * @return the default slot.
     * @throws NullPointerException if the button type does not have a default slot assigned (i.e.,
     *     {@link #UNASSIGNED}).
     */
    public int requireDefaultSlot() {
        return Objects.requireNonNull(
                defaultSlot, "#requireDefaultSlot called but the button did not have a default slot.");
    }

    /**
     * Returns the default button type for a slot, given the mapping between a given toolbar slot number (from 0 to 8),
     * and {@link SGToolbarButtonType}.
     *
     * <p>This intended for use in setting (or falling back to) defaults for toolbar buttons, or for minor tweaks to
     * existing buttons in a toolbar, as opposed to entirely new custom toolbars.
     *
     * @param slot to get the default button type mapping for.
     * @return The default button type mapping for the specified slot. Alternatively,
     *     {@link SGToolbarButtonType#UNASSIGNED} if there isn't one.
     */
    @Nonnull
    public static SGToolbarButtonType getDefaultForSlot(int slot) {
        return Arrays.stream(values())
                .filter(type -> type.defaultSlot != null)
                .filter(type -> type.defaultSlot == slot)
                .findFirst()
                .orElse(SGToolbarButtonType.UNASSIGNED);
    }
}
