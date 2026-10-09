package zone.moddev.mc.ironagefurniture.fixture;

import java.util.Arrays;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem;

/** Audits actual tab contents after optional registries and tags have loaded. */
final class CreativeTabsRuntimeProbe {
    private CreativeTabsRuntimeProbe() { }
    static int run() {
        ItemGroup[] groups = {Ironagefurniture.IAF_CHAIRS_GROUP, Ironagefurniture.IAF_BENCHES_GROUP,
                Ironagefurniture.IAF_BEDS_GROUP, Ironagefurniture.IAF_LIGHTS_GROUP};
        require(Ironagefurniture.IAF_GROUP == groups[0], "Legacy alias created a fifth tab");
        require(Arrays.stream(ItemGroup.GROUPS).filter(group -> group != null
                && group.getPath().startsWith("ironagefurniture")).count() == 4, "Expected exactly four IAF tabs");
        for (int index = 0; index < groups.length; index++) {
            if (index > 0) require(Arrays.asList(ItemGroup.GROUPS).indexOf(groups[index])
                    == Arrays.asList(ItemGroup.GROUPS).indexOf(groups[index - 1]) + 1, "Wrong tab order");
        }
        int visible = 0;
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (!"ironagefurniture".equals(item.getRegistryName().getNamespace())) continue;
            String path = item instanceof BlockItem ? ((BlockItem)item).getBlock().getRegistryName().getPath() : item.getRegistryName().getPath();
            ItemGroup expected = null;
            if (path.startsWith("chair_")) expected = path.contains("_bench_") ? groups[1] : groups[0];
            else if (path.startsWith("bed_")) expected = groups[2];
            else if (path.equals("tallow") || path.equals("obsidian_chunk")
                    || path.equals("light_metal_ironage_sconce_floor_empty_iron")
                    || path.equals("light_metal_ironage_block_floor_glow_clear")
                    || path.equals("light_metal_ironage_block_floor_lava_clear")
                    || path.equals("light_metal_ironage_block_floor_red_clear")
                    || path.equals("light_metal_ironage_candle_floor")) expected = groups[3];
            require(item.getGroup() == expected, "Wrong category or hidden state exposed: " + item.getRegistryName());
            boolean available = expected != null && zone.moddev.mc.ironagefurniture.api.items.FurnitureCreativeVisibility.isVisible(item)
                    && (!(item instanceof MetalSconceBlockItem)
                    || SconceMetalData.available(((MetalSconceBlockItem)item).getMetal()));
            int occurrences = 0;
            for (ItemGroup group : groups) {
                NonNullList<ItemStack> entries = NonNullList.create();
                item.fillItemGroup(group, entries);
                require(entries.size() <= 1 && (entries.isEmpty() || group == expected), "Item belongs to multiple categories");
                occurrences += entries.size();
            }
            NonNullList<ItemStack> search = NonNullList.create();
            item.fillItemGroup(ItemGroup.SEARCH, search);
            require(occurrences == (available ? 1 : 0) && search.size() == occurrences,
                    "Wrong optional/search visibility: " + item.getRegistryName());
            visible += occurrences;
        }
        require(visible > 0, "Empty IAF creative inventory");
        org.apache.logging.log4j.LogManager.getLogger().info("IAF CREATIVE TAB AUDIT PASSED: {} visible items, four ordered tabs", visible);
        return visible;
    }
    private static void require(boolean condition, String message) { if (!condition) throw new IllegalStateException(message); }
}
