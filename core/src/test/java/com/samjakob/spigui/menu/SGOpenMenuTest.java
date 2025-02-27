package com.samjakob.spigui.menu;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SGOpenMenuTest {

    @Mock
    private SGMenu menu;

    @Mock
    private Player player;

    private SGOpenMenu openMenu;

    @BeforeEach
    void setup() {
        openMenu = new SGOpenMenu(menu, player);
    }

    @Test
    void testRequiresMenuAndPlayer() {
        //noinspection DataFlowIssue - using null for test.
        assertThrowsExactly(NullPointerException.class, () -> new SGOpenMenu(null, null));

        //noinspection DataFlowIssue - using null for test.
        assertThrowsExactly(NullPointerException.class, () -> new SGOpenMenu(menu, null));

        //noinspection DataFlowIssue - using null for test.
        assertThrowsExactly(NullPointerException.class, () -> new SGOpenMenu(null, player));
    }

    @Test
    void testConstruct() {
        assertEquals(menu, openMenu.getMenu());
        assertEquals(player, openMenu.getPlayer());
    }

    @Test
    void testEquality() {
        assertEquals(new SGOpenMenu(menu, player), openMenu);
    }

}
