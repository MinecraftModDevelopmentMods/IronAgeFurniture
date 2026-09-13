package zone.moddev.mc.ironagefurniture.compat;

import java.util.Map;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/**
 * Registers aliases for IronAgeFurniture's own retired optional-wood registry
 * names. This does not attempt to make a complete BYG world compatible with
 * BWG.
 */
public final class LegacyFurnitureMappings {
    private static final DeferredRegister<Block> BLOCK_ALIASES =
            DeferredRegister.create(BuiltInRegistries.BLOCK, Ironagefurniture.MODID);
    private static final DeferredRegister<Item> ITEM_ALIASES =
            DeferredRegister.create(BuiltInRegistries.ITEM, Ironagefurniture.MODID);

    private static final String[] BASIC_FAMILIES = {
            "classic", "shield", "stool_short", "stool_tall", "bench_single",
            "bench_back_single", "bench_log_single", "bench_padded_single",
            "bench_back_padded_single"
    };
    private static final String[] COLORS = {
            "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray",
            "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"
    };
    private static final Map<String, String> BYG_TO_BWG = Map.ofEntries(
            Map.entry("aspen", "aspen"),
            Map.entry("baobab", "baobab"),
            Map.entry("blue_enchanted", "blue_enchanted"),
            Map.entry("cherry", "sakura"),
            Map.entry("cika", "cika"),
            Map.entry("cypress", "cypress"),
            Map.entry("ebony", "ebony"),
            Map.entry("fir", "fir"),
            Map.entry("green_enchanted", "green_enchanted"),
            Map.entry("holly", "holly"),
            Map.entry("ironwood", "ironwood"),
            Map.entry("jacaranda", "jacaranda"),
            Map.entry("mahogany", "mahogany"),
            Map.entry("mangrove", "white_mangrove"),
            Map.entry("maple", "maple"),
            Map.entry("palm", "palm"),
            Map.entry("pine", "pine"),
            Map.entry("rainbow_eucalyptus", "rainbow_eucalyptus"),
            Map.entry("redwood", "redwood"),
            Map.entry("skyris", "skyris"),
            Map.entry("willow", "willow"),
            Map.entry("witch_hazel", "witch_hazel"),
            Map.entry("zelkova", "zelkova"));

    private LegacyFurnitureMappings() {
    }

    public static void registerAliases(IEventBus modBus) {
        for (String family : BASIC_FAMILIES) {
            registerCherryAlias("chair_wood_ironage_" + family + "_cherry");
        }
        for (String color : COLORS) {
            registerCherryAlias("chair_wood_ironage_bench_padded_" + color + "_single_cherry");
            registerCherryAlias("chair_wood_ironage_bench_back_padded_" + color + "_single_cherry");
        }
        BYG_TO_BWG.forEach((oldWood, newWood) -> {
            for (String family : BASIC_FAMILIES) {
                registerAlias("chair_wood_ironage_" + family + "_byg_" + oldWood,
                        "chair_wood_ironage_" + family + "_biomeswevegone_" + newWood);
            }
            for (String color : COLORS) {
                registerAlias("chair_wood_ironage_bench_padded_" + color + "_single_byg_" + oldWood,
                        "chair_wood_ironage_bench_padded_" + color
                                + "_single_biomeswevegone_" + newWood);
                registerAlias("chair_wood_ironage_bench_back_padded_" + color
                                + "_single_byg_" + oldWood,
                        "chair_wood_ironage_bench_back_padded_" + color
                                + "_single_biomeswevegone_" + newWood);
            }
        });
        BLOCK_ALIASES.register(modBus);
        ITEM_ALIASES.register(modBus);
    }

    private static void registerCherryAlias(String targetPath) {
        String oldPath = targetPath.substring(0, targetPath.length() - "_cherry".length())
                + "_biomesoplenty_cherry";
        registerAlias(oldPath, targetPath);
    }

    private static void registerAlias(String oldPath, String targetPath) {
        ResourceLocation oldId = ResourceLocation.fromNamespaceAndPath(Ironagefurniture.MODID, oldPath);
        ResourceLocation targetId = ResourceLocation.fromNamespaceAndPath(
                Ironagefurniture.MODID, targetPath);
        BLOCK_ALIASES.addAlias(oldId, targetId);
        ITEM_ALIASES.addAlias(oldId, targetId);
    }
}
