package com.samjakob.spigui.toolbar;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents pre-defined types for toolbar buttons. These can be used to easily re-define the buttons used in a
 * toolbar, without creating an entirely custom toolbar implementation.
 */
public enum SGToolbarButtonType {

    /** The "previous page" pagination button. */
    PREV_BUTTON,

    /** The "current page" indicator button (doesn't necessarily have an action associated). */
    CURRENT_BUTTON,

    /** The "next page" pagination button. */
    NEXT_BUTTON,

    /** No pre-defined action or button. */
    UNASSIGNED;

    /**
     * The default mappings between slot number and {@link SGToolbarButtonType}. This intended for use in setting (or
     * falling back to) defaults for toolbar buttons, or for minor tweaks to existing buttons in a toolbar, as opposed
     * to entirely new custom toolbars.
     */
    private static final Map<Integer, SGToolbarButtonType> DEFAULT_MAPPINGS = Stream.of(
                    new AbstractMap.SimpleImmutableEntry<>(3, PREV_BUTTON),
                    new AbstractMap.SimpleImmutableEntry<>(4, CURRENT_BUTTON),
                    new AbstractMap.SimpleImmutableEntry<>(5, NEXT_BUTTON))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    /**
     * Returns the default mapping between a given toolbar slot number (from 0 to 8), and {@link SGToolbarButtonType}.
     * This intended for use in setting (or falling back to) defaults for toolbar buttons, or for minor tweaks to
     * existing buttons in a toolbar, as opposed to entirely new custom toolbars.
     *
     * @param slot The slot number to get the default button type mapping for.
     * @return The default button type mapping for the specified slot. Alternatively,
     *     {@link SGToolbarButtonType#UNASSIGNED} if there isn't one.
     */
    public static SGToolbarButtonType getDefaultForSlot(int slot) {
        return DEFAULT_MAPPINGS.getOrDefault(slot, SGToolbarButtonType.UNASSIGNED);
    }
}
