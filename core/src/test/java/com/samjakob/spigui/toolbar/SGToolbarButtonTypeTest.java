package com.samjakob.spigui.toolbar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SGToolbarButtonTypeTest {

    @Test
    void testGetDefaultForSlot() {
        assertEquals(SGToolbarButtonType.PREV_BUTTON, SGToolbarButtonType.getDefaultForSlot(3));
        assertEquals(SGToolbarButtonType.CURRENT_BUTTON, SGToolbarButtonType.getDefaultForSlot(4));
        assertEquals(SGToolbarButtonType.NEXT_BUTTON, SGToolbarButtonType.getDefaultForSlot(5));

        // Invalid values should map to UNASSIGNED.
        assertEquals(SGToolbarButtonType.UNASSIGNED, SGToolbarButtonType.getDefaultForSlot(-1));
        assertEquals(SGToolbarButtonType.UNASSIGNED, SGToolbarButtonType.getDefaultForSlot(Integer.MAX_VALUE));
    }

}
