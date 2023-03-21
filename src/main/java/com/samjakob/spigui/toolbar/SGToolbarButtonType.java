package com.samjakob.spigui.toolbar;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SGToolbarButtonType {

    PREV_BUTTON,
    CURRENT_BUTTON,
    NEXT_BUTTON,
    UNASSIGNED;

    /**
     * The default mappings between slot number and {@link SGToolbarButtonType}.
     * This intended for use in setting (or falling back to) defaults for toolbar buttons,
     * or for minor tweaks to existing buttons in a toolbar, as opposed to entirely new
     * custom toolbars.
     */
    private static final Map<Integer, SGToolbarButtonType> DEFAULT_MAPPINGS = Stream.of(
        new AbstractMap.SimpleImmutableEntry<>(3, PREV_BUTTON),
        new AbstractMap.SimpleImmutableEntry<>(4, CURRENT_BUTTON),
        new AbstractMap.SimpleImmutableEntry<>(5, NEXT_BUTTON)
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    /**
     * Returns the default mapping between a given toolbar slot number (from 0 - 8),
     * and {@link SGToolbarButtonType}. This intended for use in setting (or falling
     * back to) defaults for toolbar buttons, or for minor tweaks to existing buttons
     * in a toolbar, as opposed to entirely new custom toolbars.
     * @param slot The slot number to get the default button type mapping for.
     * @return The default button type mapping for the specified slot. Or SGToolbarButtonType.UNASSIGNED,
     * if there isn't one.
     */
    public static SGToolbarButtonType getDefaultForSlot(int slot) {
        return DEFAULT_MAPPINGS.getOrDefault(slot, SGToolbarButtonType.UNASSIGNED);
    }

}
