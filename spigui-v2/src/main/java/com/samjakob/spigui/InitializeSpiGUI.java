package com.samjakob.spigui;

import com.samjakob.spigui.item.ModernItemBuilder;

/** Internal helper class used to automatically initialize factories required for SpiGUI. */
final class InitializeSpiGUI {

    static {
        ModernItemBuilder.register();
    }

    /** Private constructor. */
    private InitializeSpiGUI() {}
}
