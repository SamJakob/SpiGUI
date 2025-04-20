package com.samjakob.spigui.standalone;

import com.samjakob.spigui.SpiGUI;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenuListenerBase;
import com.samjakob.spigui.toolbar.SGDefaultToolbarBuilderFactory;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SpiGUIInitializeTest {

    @Mock
    private JavaPlugin plugin;

    @Mock
    private Server server;

    @Mock
    private PluginManager pluginManager;

    @Test
    void testFactories() {
        assertNotNull(ItemBuilder.create(Material.OAK_PLANKS));
        assertNotNull(SGDefaultToolbarBuilderFactory.get().newToolbarBuilder());
    }

    @Test
    void testListener() {
        given(plugin.getServer()).willReturn(server);
        given(server.getPluginManager()).willReturn(pluginManager);

        final var spiGUI = new SpiGUI(plugin);

        final ArgumentCaptor<SGMenuListenerBase> listenerCaptor = ArgumentCaptor.captor();
        verify(pluginManager).registerEvents(listenerCaptor.capture(), eq(plugin));
        assertNotNull(listenerCaptor.getValue());
    }

}
