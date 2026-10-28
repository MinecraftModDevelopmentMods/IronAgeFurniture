package zone.moddev.mc.ironagefurniture.client;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

/** Refresh native category/search contents after a server-controlled availability change. */
public final class FurnitureConfigurationClient {
    public static void refresh() {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null || client.getConnection() == null) return;
        var parameters = new CreativeModeTab.ItemDisplayParameters(client.level.enabledFeatures(),
                client.player.canUseGameMasterBlocks() && client.options.operatorItemsTab().get(), client.level.registryAccess());
        // Match vanilla's category-before-search ordering. Its ordinary cache does
        // not include mod configuration, so it cannot notice this change itself.
        for (var tab : CreativeModeTabs.allTabs()) if (tab.getType() == CreativeModeTab.Type.CATEGORY) tab.buildContents(parameters);
        for (var tab : CreativeModeTabs.allTabs()) if (tab.getType() != CreativeModeTab.Type.CATEGORY) tab.buildContents(parameters);
        var searches = client.getConnection().searchTrees();
        // Forge's native methods rebuild every registered tab search from its
        // display items, while the supplied list belongs to the vanilla search tab.
        var items = List.copyOf(CreativeModeTabs.searchTab().getDisplayItems());
        searches.updateCreativeTooltips(client.level.registryAccess(), items);
        searches.updateCreativeTags(items);
        // Native resize preserves the selected tab, search text and scroll row,
        // and refreshes pagination and the current menu without a reconnect.
        if (client.screen instanceof CreativeModeInventoryScreen screen)
            screen.resize(client, screen.width, screen.height);
    }
    private FurnitureConfigurationClient() { }
}
