package com.samjakob.spigui.item;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Items such as glass panes can have variable color. This color is set using durability values which is understandable
 * from an efficiency perspective however it is rather unintuitive and the values are not clear or memorable.
 *
 * <p>This class allows those damage values to be referred to by the name of the color they represent.
 *
 * <p><b>NOTE:</b> this class is made public for convenience to those using legacy versions of Minecraft (e.g., 1.8).
 * <b>However,</b> if you intend to upgrade or support later versions you should <i>avoid</i> using (and certainly avoid
 * relying on) this class.
 *
 * @author SamJakob
 * @version 2.0.0
 */
public enum LegacyItemDataColor {

    /** Value is 0. Use WHITE_WOOL for 1.13+ */
    WHITE(ItemColor.WHITE, (short) 0),
    /** Value is 1. Use ORANGE_WOOL for 1.13+ */
    ORANGE(ItemColor.ORANGE, (short) 1),
    /** Value is 2. Use MAGENTA_WOOL for 1.13+ */
    MAGENTA(ItemColor.MAGENTA, (short) 2),
    /** Value is 3. Use LIGHT_BLUE_WOOL for 1.13+ */
    LIGHT_BLUE(ItemColor.LIGHT_BLUE, (short) 3),
    /** Value is 4. Use YELLOW_WOOL for 1.13+ */
    YELLOW(ItemColor.YELLOW, (short) 4),
    /** Value is 5. Use LIME_WOOL for 1.13+ */
    LIME(ItemColor.LIME, (short) 5),
    /** Value is 6. Use PINK_WOOL for 1.13+ */
    PINK(ItemColor.PINK, (short) 6),
    /** Value is 7. Use GRAY_WOOL for 1.13+ */
    GRAY(ItemColor.GRAY, (short) 7),
    /** Value is 8. Use LIGHT_GRAY_WOOL for 1.13+ */
    LIGHT_GRAY(ItemColor.LIGHT_GRAY, (short) 8),
    /** Value is 9. Use CYAN_WOOL for 1.13+ */
    CYAN(ItemColor.CYAN, (short) 9),
    /** Value is 10. Use PURPLE_WOOL for 1.13+ */
    PURPLE(ItemColor.PURPLE, (short) 10),
    /** Value is 11. Use BLUE_WOOL for 1.13+ */
    BLUE(ItemColor.BLUE, (short) 11),
    /** Value is 12. Use BROWN_WOOL for 1.13+ */
    BROWN(ItemColor.BROWN, (short) 12),
    /** Value is 13. Use GREEN_WOOL for 1.13+ */
    GREEN(ItemColor.GREEN, (short) 13),
    /** Value is 14. Use RED_WOOL for 1.13+ */
    RED(ItemColor.RED, (short) 14),
    /** Value is 15. Use BLACK_WOOL for 1.13+ */
    BLACK(ItemColor.BLACK, (short) 15);

    /** The public-facing {@link ItemColor}. */
    @Nonnull
    private final ItemColor color;

    /** The durability value of the color. */
    private final short durability;

    /**
     * Define a {@link LegacyItemDataColor} based on a given short value.
     *
     * @param color The public-facing {@link ItemColor} API value.
     * @param durability The color value (as a durability value). Must be between 0 and 15, per Minecraft's color
     *     mapping.
     */
    LegacyItemDataColor(@Nonnull ItemColor color, short durability) {
        this.color = Objects.requireNonNull(color);
        this.durability = sanitizeDurability(durability);
    }

    /**
     * Returns the public-facing {@link ItemColor} value.
     *
     * @return The color value.
     */
    @Nonnull
    public ItemColor getColor() {
        return color;
    }

    /**
     * Returns the durability value that the named color represents.
     *
     * @return The durability value as a 'short'.
     */
    public short getDurability() {
        return durability;
    }

    /**
     * Returns a {@link LegacyItemDataColor} as found by its damage value, or null if there isn't one.
     *
     * @param value The corresponding damage value of the color.
     * @return The {@link LegacyItemDataColor} associated with {@code value}, or null if there isn't one.
     */
    @Nullable
    public static LegacyItemDataColor getByDurability(short value) {
        sanitizeDurability(value);

        return Arrays.stream(values())
                .filter(it -> value == it.durability)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("No LegacyItemDataColor value found for durability value: %d", value)));
    }

    /**
     * Returns a {@link LegacyItemDataColor} as found by the corresponding public-facing {@link ItemColor}, or null if
     * there isn't one.
     *
     * @param color The corresponding public API value of the color.
     * @return The {@link LegacyItemDataColor} associated with {@code color}, or null if there isn't one.
     */
    @Nonnull
    public static LegacyItemDataColor getByColor(@Nonnull ItemColor color) {
        Objects.requireNonNull(color);

        return Arrays.stream(values())
                .filter(it -> it.color == color)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Missing LegacyItemDataColor for ItemColor(%s)", color.name())));
    }

    /**
     * Ensure that the provided {@code durability} value is one that can be interpreted as a color (i.e., between 0 and
     * 15).
     *
     * @param durability value to check
     * @return the same durability value.
     * @throws IllegalArgumentException if the durability value is unknown or invalid.
     */
    private static short sanitizeDurability(final short durability) {
        if (durability > 15 || durability < 0) {
            throw new IllegalArgumentException("Durability value must be between 0 and 15 (inclusive).");
        }

        return durability;
    }
}
