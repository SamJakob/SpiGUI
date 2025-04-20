package com.samjakob.spigui;

import com.samjakob.spigui.item.ModernItemBuilder;
import com.samjakob.spigui.toolbar.SGDefaultToolbarBuilder;

/** Internal helper class used to automatically initialize factories required for SpiGUI. */
final class InitializeSpiGUI {

    static {
        ModernItemBuilder.register();
        SGDefaultToolbarBuilder.register();
    }

    /** Private constructor. */
    private InitializeSpiGUI() {}
}
