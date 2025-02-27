package com.samjakob.spigui_classpath_tests;

import com.samjakob.spigui.item.ItemBuilder;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

class SpiGUIClasspathTest {

    @Test
    void testFactories() {
        ItemBuilder.create(Material.WOOD);
    }

}
