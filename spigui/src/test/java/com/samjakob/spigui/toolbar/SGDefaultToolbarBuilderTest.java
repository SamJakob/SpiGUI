package com.samjakob.spigui.toolbar;

import com.samjakob.spigui.item.ItemBuilder;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SGDefaultToolbarBuilderTest {

    SGDefaultToolbarBuilder builder = (SGDefaultToolbarBuilder) SGDefaultToolbarBuilderFactory.get().newToolbarBuilder();

    @Test
    void testPreviousAndNextButtons() {
        final ItemBuilder previousButton = builder.initializePreviousPageButton();
        final ItemBuilder nextButton = builder.initializeNextPageButton();

        assertEquals(Material.ARROW, previousButton.getType());
        assertEquals(Material.ARROW, nextButton.getType());
    }

    @Test
    void testCurrentPageIndicator() {
        final ItemBuilder currentPageIndicator = builder.initializeCurrentPageIndicator();

        assertEquals(Material.NAME_TAG, currentPageIndicator.getType());
    }

}
