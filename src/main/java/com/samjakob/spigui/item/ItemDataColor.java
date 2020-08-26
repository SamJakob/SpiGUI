package com.samjakob.spigui.item;

/**
 * Items such as glass panes can have variable color. This color is
 * set using durability values which is understandable from an
 * efficiency perspective however it is rather unintuitive.
 *
 * This class allows those damage values to be referred to by the name
 * of the color they represent.
 *
 * @author SamJakob
 * @version 2.0.0
 */
public enum ItemDataColor {

    WHITE((short) 0),
    ORANGE((short) 1),
    MAGENTA((short) 2),
    LIGHT_BLUE((short) 3),
    YELLOW((short) 4),
    LIME((short) 5),
    PINK((short) 6),
    GRAY((short) 7),
    LIGHT_GRAY((short) 8),
    CYAN((short) 9),
    PURPLE((short) 10),
    BLUE((short) 11),
    BROWN((short) 12),
    GREEN((short) 13),
    RED((short) 14),
    BLACK((short) 15);

    /**
     * The durability value of the color.
     */
    private final short value;

    ItemDataColor(short value) {
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
     * Returns an {@link ItemDataColor} as found by its damage value or
     * null if there isn't one.
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
