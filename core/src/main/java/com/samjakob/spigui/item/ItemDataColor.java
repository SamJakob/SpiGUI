package com.samjakob.spigui.item;

/**
 * Items such as glass panes can have variable color. This color is set using durability values which is understandable
 * from an efficiency perspective however it is rather unintuitive and the values are not clear or memorable.
 *
 * <p>This class allows those damage values to be referred to by the name of the color they represent.
 *
 * @author SamJakob
 * @version 2.0.0
 */
public enum ItemDataColor {

    /** Value is 0. Use WHITE_WOOL for 1.13+ */
    WHITE((short) 0),
    /** Value is 1. Use ORANGE_WOOL for 1.13+ */
    ORANGE((short) 1),
    /** Value is 2. Use MAGENTA_WOOL for 1.13+ */
    MAGENTA((short) 2),
    /** Value is 3. Use LIGHT_BLUE_WOOL for 1.13+ */
    LIGHT_BLUE((short) 3),
    /** Value is 4. Use YELLOW_WOOL for 1.13+ */
    YELLOW((short) 4),
    /** Value is 5. Use LIME_WOOL for 1.13+ */
    LIME((short) 5),
    /** Value is 6. Use PINK_WOOL for 1.13+ */
    PINK((short) 6),
    /** Value is 7. Use GRAY_WOOL for 1.13+ */
    GRAY((short) 7),
    /** Value is 8. Use LIGHT_GRAY_WOOL for 1.13+ */
    LIGHT_GRAY((short) 8),
    /** Value is 9. Use CYAN_WOOL for 1.13+ */
    CYAN((short) 9),
    /** Value is 10. Use PURPLE_WOOL for 1.13+ */
    PURPLE((short) 10),
    /** Value is 11. Use BLUE_WOOL for 1.13+ */
    BLUE((short) 11),
    /** Value is 12. Use BROWN_WOOL for 1.13+ */
    BROWN((short) 12),
    /** Value is 13. Use GREEN_WOOL for 1.13+ */
    GREEN((short) 13),
    /** Value is 14. Use RED_WOOL for 1.13+ */
    RED((short) 14),
    /** Value is 15. Use BLACK_WOOL for 1.13+ */
    BLACK((short) 15);

    /** The durability value of the color. */
    private final short value;

    /**
     * Define an {@link ItemDataColor} based on a given short value.
     *
     * @param value The color value (as a durability value). Must be between 0 and 15, per Minecraft's color mapping.
     */
    ItemDataColor(short value) {
        // Sanitize the input value.
        if (value > 15 || value < 0) {
            throw new IllegalArgumentException("Value must be between 0 and 15.");
        }

        this.value = value;
    }

    /**
     * Returns the durability value that the named color represents.
     *
     * @return The durability value as a 'short'.
     */
    public short getValue() {
        return value;
    }

    /**
     * Returns an {@link ItemDataColor} as found by its damage value or null if there isn't one.
     *
     * @param value The corresponding damage value of the color.
     * @return The {@link ItemDataColor} associated with <code>value</code> or null if there isn't one.
     */
    public static ItemDataColor getByValue(short value) {
        for (ItemDataColor color : ItemDataColor.values()) {
            if (value == color.value) return color;
        }

        return null;
    }
}
