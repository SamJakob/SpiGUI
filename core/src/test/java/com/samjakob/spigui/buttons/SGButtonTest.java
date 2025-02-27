package com.samjakob.spigui.buttons;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SGButtonTest {

    final ItemStack dirt = new ItemStack(Material.DIRT);

    SGButton button;

    @BeforeEach
    void setup() {
        button = new SGButton(dirt);
    }

    @Test
    void testRequiresIcon() {
        //noinspection DataFlowIssue - using null for test.
        assertThrowsExactly(NullPointerException.class, () -> new SGButton(null));
    }

    @Test
    void testConstruction() {
        assertEquals(dirt, button.getIcon());
        assertNull(button.getListener());
    }

    @Test
    void testSetIcon() {
        assertEquals(dirt, button.getIcon());
        final ItemStack stone = new ItemStack(Material.STONE);
        button.setIcon(stone);
        assertEquals(stone, button.getIcon());
    }

    @Test
    void testSetListener() {
        assertNull(button.getListener());
        final SGButtonListener listener = (e) -> {};
        button.setListener(listener);
        assertEquals(listener, button.getListener());
    }

    @Test
    void testDoesNotAllowAirAsIcon() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new SGButton(new ItemStack(Material.AIR)));
    }

}
