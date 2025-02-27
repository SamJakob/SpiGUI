package com.samjakob.spigui;

import com.samjakob.spigui.item.LegacyItemBuilder;

/** Internal helper class used to automatically initialize factories required for SpiGUI. */
final class InitializeSpiGUI {

    static {
        LegacyItemBuilder.register();
    }

    /** Private constructor. */
    private InitializeSpiGUI() {}
}
