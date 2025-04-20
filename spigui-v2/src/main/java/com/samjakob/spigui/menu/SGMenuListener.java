package com.samjakob.spigui.menu;

import javax.annotation.Nonnull;

import com.samjakob.spigui.SpiGUI;

/**
 * The listener for SpiGUI inventory events.
 *
 * @see SGMenuListenerBase
 */
public final class SGMenuListener extends SGMenuListenerBase {

    /**
     * Initialize an SGBaseMenuListener for the specified {@link SpiGUI} instance.
     *
     * @param spiGUI that this listener is registered for.
     */
    public SGMenuListener(@Nonnull SpiGUI spiGUI) {
        super(spiGUI);
    }
}
