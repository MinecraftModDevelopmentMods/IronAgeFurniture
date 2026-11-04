package zone.moddev.mc.ironagefurniture.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/** Refresh native search and category contents after server availability changes. */
public final class FurnitureConfigurationClient {
    public static void refresh() {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null || client.getConnection() == null) return;
        client.createSearchTrees();
        if (client.screen instanceof CreativeModeInventoryScreen screen)
            screen.resize(client, screen.width, screen.height);
    }
    private FurnitureConfigurationClient() { }
}
