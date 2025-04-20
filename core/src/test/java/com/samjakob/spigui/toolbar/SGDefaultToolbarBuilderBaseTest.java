package com.samjakob.spigui.toolbar;

import com.samjakob.spigui.MockItemBuilder;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * These tests check the labelling of buttons.
 *
 * <p>As such, they are fairly specific tests, so if these labels are changed often, this methodology might need to be
 * changed.
 *
 * <p>This is mostly here as a regression test.
 */
@ExtendWith(MockitoExtension.class)
class SGDefaultToolbarBuilderBaseTest {

    private static final int MOCK_CURRENT_PAGE_INDEX = 2;
    private static final int MOCK_MAX_PAGE_INDEX = 4;

    @Mock
    private SGMenu menu;

    private final SGDefaultToolbarBuilderBase builder = new SGDefaultToolbarBuilderBase() {
        @Nonnull
        @Override
        protected ItemBuilder initializePreviousPageButton() {
            return new MockItemBuilder(Material.ARROW).asItemBuilder();
        }

        @Nonnull
        @Override
        protected ItemBuilder initializeCurrentPageIndicator() {
            return new MockItemBuilder(Material.NAME_TAG).asItemBuilder();
        }

        @Nonnull
        @Override
        protected ItemBuilder initializeNextPageButton() {
            return new MockItemBuilder(Material.ARROW).asItemBuilder();
        }
    };

    @BeforeEach
    void setup() {
        given(menu.getCurrentPage()).willReturn(MOCK_CURRENT_PAGE_INDEX);
    }

    @Test
    void testPreviousPageButtonLabelling() {
        assertTrue(builder.getPreviousPageLabelBuilder().buildName(menu).toLowerCase().contains("previous"));
        assertTrue(String.join("\n", builder.getPreviousPageDescriptionBuilder().buildLore(menu)).toLowerCase().contains("page 2"));
    }

    @Test
    void testCurrentPageButtonLabelling() {
        given(menu.getMaxPageNumber()).willReturn(MOCK_MAX_PAGE_INDEX + 1);

        assertTrue(builder.getCurrentPageLabelBuilder().buildName(menu).toLowerCase().contains("page 3 of 5"));
        assertTrue(String.join("\n", builder.getCurrentPageDescriptionBuilder().buildLore(menu)).toLowerCase().contains("page 3"));
    }

    @Test
    void testNextPageButtonLabelling() {
        assertTrue(builder.getNextPageLabelBuilder().buildName(menu).toLowerCase().contains("next"));
        assertTrue(String.join("\n", builder.getNextPageDescriptionBuilder().buildLore(menu)).toLowerCase().contains("page 4"));
    }

    @Test
    void testConstructPreviousPageButton() {
        final String expectedPreviousButtonLabel = builder.getPreviousPageLabelBuilder().buildName(menu);
        final List<String> expectedPreviousButtonDescription = builder.getPreviousPageDescriptionBuilder().buildLore(menu);

        // Build the button and ensure that a valid result has been produced for the "PREV_BUTTON" slot.
        final SGButton previousButton = builder.buildToolbarButton(SGToolbarButtonType.PREV_BUTTON.requireDefaultSlot(), MOCK_CURRENT_PAGE_INDEX, SGToolbarButtonType.PREV_BUTTON, menu);
        assertNotNull(previousButton);
        assertNotNull(previousButton.getIcon());
        assertNotNull(previousButton.getListener());

        // Check that the icon has been built correctly.
        assertEquals(Material.ARROW, previousButton.getIcon().getType());
        assertEquals(expectedPreviousButtonLabel, previousButton.getIcon().getItemMeta().getDisplayName());
        assertEquals(expectedPreviousButtonDescription, previousButton.getIcon().getItemMeta().getLore());
    }

    @Test
    void testConstructCurrentPageButton() {
        final String expectedCurrentButtonLabel = builder.getCurrentPageLabelBuilder().buildName(menu);
        final List<String> expectedCurrentButtonDescription = builder.getCurrentPageDescriptionBuilder().buildLore(menu);

        // Build the button and ensure that a valid result has been produced for the "CURRENT_BUTTON" slot.
        final SGButton currentButton = builder.buildToolbarButton(SGToolbarButtonType.CURRENT_BUTTON.requireDefaultSlot(), MOCK_CURRENT_PAGE_INDEX, SGToolbarButtonType.CURRENT_BUTTON, menu);
        assertNotNull(currentButton);
        assertNotNull(currentButton.getIcon());
        assertNotNull(currentButton.getListener());

        // Check that the icon has been built correctly.
        assertEquals(Material.NAME_TAG, currentButton.getIcon().getType());
        assertEquals(expectedCurrentButtonLabel, currentButton.getIcon().getItemMeta().getDisplayName());
        assertEquals(expectedCurrentButtonDescription, currentButton.getIcon().getItemMeta().getLore());
    }

    @Test
    void testConstructNextPageButton() {
        given(menu.getMaxPageNumber()).willReturn(MOCK_MAX_PAGE_INDEX + 1);
        given(menu.getMaxPageIndex()).willCallRealMethod();

        final String expectedNextButtonLabel = builder.getNextPageLabelBuilder().buildName(menu);
        final List<String> expectedNextButtonDescription = builder.getNextPageDescriptionBuilder().buildLore(menu);

        // Build the button and ensure that a valid result has been produced for the "NEXT_BUTTON" slot.
        final SGButton nextButton = builder.buildToolbarButton(SGToolbarButtonType.NEXT_BUTTON.requireDefaultSlot(), MOCK_CURRENT_PAGE_INDEX, SGToolbarButtonType.NEXT_BUTTON, menu);
        assertNotNull(nextButton);
        assertNotNull(nextButton.getIcon());
        assertNotNull(nextButton.getListener());

        // Check that the icon has been built correctly.
        assertEquals(Material.ARROW, nextButton.getIcon().getType());
        assertEquals(expectedNextButtonLabel, nextButton.getIcon().getItemMeta().getDisplayName());
        assertEquals(expectedNextButtonDescription, nextButton.getIcon().getItemMeta().getLore());
    }

    @Test
    void testPreviousPageButtonHandler() {
        // Construct the button to test with.
        final SGButton previousButton = builder.buildToolbarButton(SGToolbarButtonType.PREV_BUTTON.requireDefaultSlot(), MOCK_CURRENT_PAGE_INDEX, SGToolbarButtonType.PREV_BUTTON, menu);
        assertNotNull(previousButton);
        assertNotNull(previousButton.getListener());

        // Invoke the listener and check that the correct behaviors are observed.
        final InventoryClickEvent event = mock(InventoryClickEvent.class);

        final HumanEntity viewer = mock(HumanEntity.class);
        given(event.getWhoClicked()).willReturn(viewer);

        // Click the "previous page" button.
        previousButton.getListener().onClick(event);

        // We consider verifying that "previousPage" is called to be good enough here - as we assume that the test for
        // the menu itself will ensure that invoking previousPage reduces the current page index by one where possible.
        verify(menu, times(1)).previousPage(viewer);

        verify(event, times(1)).setResult(Event.Result.DENY);
        verifyNoMoreInteractions(event);
        verifyNoMoreInteractions(menu);
    }

    @Test
    void testCurrentPageIndicatorHandler() {
        given(menu.getMaxPageNumber()).willReturn(MOCK_MAX_PAGE_INDEX + 1);

        // Construct the button to test with.
        final SGButton currentPageIndicator = builder.buildToolbarButton(SGToolbarButtonType.CURRENT_BUTTON.requireDefaultSlot(), MOCK_CURRENT_PAGE_INDEX, SGToolbarButtonType.CURRENT_BUTTON, menu);
        assertNotNull(currentPageIndicator);
        assertNotNull(currentPageIndicator.getListener());

        // Invoke the listener and check that the correct behaviors are observed.
        final InventoryClickEvent event = mock(InventoryClickEvent.class);

        final HumanEntity viewer = mock(HumanEntity.class);

        // Click the "current page" indicator.
        currentPageIndicator.getListener().onClick(event);
        verify(event, times(1)).setResult(Event.Result.DENY);
        verifyNoMoreInteractions(event);
        verifyNoMoreInteractions(menu);
    }

    @Test
    void testNextPageButtonHandler() {
        given(menu.getMaxPageNumber()).willReturn(MOCK_MAX_PAGE_INDEX + 1);
        given(menu.getMaxPageIndex()).willCallRealMethod();

        // Construct the button to test with.
        final SGButton nextButton = builder.buildToolbarButton(SGToolbarButtonType.NEXT_BUTTON.requireDefaultSlot(), MOCK_CURRENT_PAGE_INDEX, SGToolbarButtonType.NEXT_BUTTON, menu);
        assertNotNull(nextButton);
        assertNotNull(nextButton.getListener());

        // Invoke the listener and check that the correct behaviors are observed.
        final InventoryClickEvent event = mock(InventoryClickEvent.class);

        final HumanEntity viewer = mock(HumanEntity.class);
        given(event.getWhoClicked()).willReturn(viewer);

        // Click the "next page" button.
        nextButton.getListener().onClick(event);

        // As with previous page.
        verify(menu, times(1)).nextPage(viewer);

        verify(event, times(1)).setResult(Event.Result.DENY);
        verifyNoMoreInteractions(event);
        verifyNoMoreInteractions(menu);
    }

    @Test
    void testUnassigned() {
        assertNull(builder.buildToolbarButton(0, MOCK_CURRENT_PAGE_INDEX, SGToolbarButtonType.UNASSIGNED, menu));
        assertNull(builder.buildToolbarButton(9, MOCK_CURRENT_PAGE_INDEX, SGToolbarButtonType.UNASSIGNED, menu));

        // Alternatively, we may wish to throw for completely invalid values.
        // It's a consideration, but I haven't seen a reason to do this (yet), so I'll leave it as UNASSIGNED.
        assertNull(builder.buildToolbarButton(-1, MOCK_CURRENT_PAGE_INDEX, SGToolbarButtonType.UNASSIGNED, menu));
    }

    @Test
    void testPageBounds() {
        // With the given initial state that all of the tests are performed with, the toolbar should render both
        // previous and next buttons (current page is always rendered).
        given(menu.getMaxPageNumber()).willReturn(MOCK_MAX_PAGE_INDEX + 1);
        given(menu.getMaxPageIndex()).willCallRealMethod();
        assertNotNull(builder.buildToolbarButton(SGToolbarButtonType.PREV_BUTTON.requireDefaultSlot(), MOCK_CURRENT_PAGE_INDEX, SGToolbarButtonType.PREV_BUTTON, menu));
        assertNotNull(builder.buildToolbarButton(SGToolbarButtonType.NEXT_BUTTON.requireDefaultSlot(), MOCK_CURRENT_PAGE_INDEX, SGToolbarButtonType.NEXT_BUTTON, menu));

        // Now, if we're at the first page (index = 0), only the next button should show.
        given(menu.getCurrentPage()).willReturn(0);
        assertNull(builder.buildToolbarButton(SGToolbarButtonType.PREV_BUTTON.requireDefaultSlot(), 0, SGToolbarButtonType.PREV_BUTTON, menu));
        assertNotNull(builder.buildToolbarButton(SGToolbarButtonType.NEXT_BUTTON.requireDefaultSlot(), 0, SGToolbarButtonType.NEXT_BUTTON, menu));

        // Similarly, if we're at the last page (index = MOCK_MAX_PAGE_INDEX), only the previous button should show.
        given(menu.getCurrentPage()).willReturn(MOCK_MAX_PAGE_INDEX);
        assertNotNull(builder.buildToolbarButton(SGToolbarButtonType.PREV_BUTTON.requireDefaultSlot(), MOCK_MAX_PAGE_INDEX, SGToolbarButtonType.PREV_BUTTON, menu));
        assertNull(builder.buildToolbarButton(SGToolbarButtonType.NEXT_BUTTON.requireDefaultSlot(), MOCK_MAX_PAGE_INDEX, SGToolbarButtonType.NEXT_BUTTON, menu));
    }

}
