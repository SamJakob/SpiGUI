package com.samjakob.spigui;

import com.samjakob.spigui.item.LegacyItemBuilder;
import com.samjakob.spigui.toolbar.SGDefaultToolbarBuilder;

/** Internal helper class used to automatically initialize factories required for SpiGUI. */
final class InitializeSpiGUI {

    static {
        LegacyItemBuilder.register();
        SGDefaultToolbarBuilder.register();
    }

    /** Private constructor. */
    private InitializeSpiGUI() {}
}
