package com.samjakob.spigui.item;

import org.bukkit.Material;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ModernItemBuilderTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE", "YELLOW", "LIME", "PINK", "GRAY", "LIGHT_GRAY", "CYAN", "PURPLE",
        "BLUE", "BROWN", "GREEN", "RED", "BLACK",
    })
    void testGetColor(final String color) {
        final var expectedItemColor = ItemColor.valueOf(color);

        var builder = ItemBuilder.create(Material.valueOf(String.format("%s_WOOL", color)));
        assertEquals(expectedItemColor, builder.getColor());

        builder = ItemBuilder.create(Material.valueOf(String.format("%s_BED", color)));
        assertEquals(expectedItemColor, builder.getColor());

        builder = ItemBuilder.create(Material.valueOf(String.format("%s_SHULKER_BOX", color)));
        assertEquals(expectedItemColor, builder.getColor());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE", "YELLOW", "LIME", "PINK", "GRAY", "LIGHT_GRAY", "CYAN", "PURPLE",
        "BLUE", "BROWN", "GREEN", "RED", "BLACK",
    })
    void testSetColor(final String color) {
        // The color that the item is to begin with (WHITE, unless the color we want to change it to is WHITE).
        final var initialItemColorName = !"WHITE".equals(color) ? "WHITE" : "ORANGE";

        // The color that the item should be changed to.
        final var expectedItemColor = ItemColor.valueOf(color);

        var builder = ItemBuilder.create(Material.valueOf(String.format("%s_WOOL", initialItemColorName)));
        assertNotEquals(expectedItemColor, builder.getColor());
        builder.color(expectedItemColor);
        assertEquals(expectedItemColor, builder.getColor());

        builder = ItemBuilder.create(Material.valueOf(String.format("%s_BED", initialItemColorName)));
        assertNotEquals(expectedItemColor, builder.getColor());
        builder.color(expectedItemColor);
        assertEquals(expectedItemColor, builder.getColor());

        builder = ItemBuilder.create(Material.valueOf(String.format("%s_SHULKER_BOX", initialItemColorName)));
        assertNotEquals(expectedItemColor, builder.getColor());
        builder.color(expectedItemColor);
        assertEquals(expectedItemColor, builder.getColor());
    }

}
