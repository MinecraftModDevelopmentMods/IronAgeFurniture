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
        for (String family : BASIC_FAMILIES) if (!family.contains("padded")) {
            registerAlias("chair_wood_ironage_" + family + "_big_oak",
                    "chair_wood_ironage_" + family + "_dark_oak");
        }
        for (String form : new String[]{"bed_wood_foot_", "bed_wood_foot_left_", "bed_canopy_foot_lower_",
                "bed_canopy_foot_left_lower_", "chair_wood_ironage_wingback_", "chair_wood_ironage_throne_"}) {
            registerAlias(form + "big_oak", form + "dark_oak");
        }
        for (String wood : zone.moddev.mc.ironagefurniture.init.PhaseFourFurniture.VANILLA_WOODS) {
            registerPaddedAliases(wood);
        }
        if (net.neoforged.fml.ModList.get().isLoaded("biomesoplenty")) {
            for (String wood : zone.moddev.mc.ironagefurniture.init.PhaseFourBopFurniture.WOODS)
                registerPaddedAliases("biomesoplenty_" + wood);
        }
        if (net.neoforged.fml.ModList.get().isLoaded("biomeswevegone")) {
            for (String wood : zone.moddev.mc.ironagefurniture.init.PhaseFourBwgFurniture.WOODS)
                registerPaddedAliases("biomeswevegone_" + wood);
        }
        if (net.neoforged.fml.ModList.get().isLoaded("immersiveengineering")) {
            registerPaddedAliases(zone.moddev.mc.ironagefurniture.init.PhaseFourIeFurniture.WOOD);
        }
        for (String position : new String[]{"floor", "floor_unlit", "wall", "wall_unlit"}) {
            ResourceLocation old = ResourceLocation.fromNamespaceAndPath(Ironagefurniture.MODID,
                    "light_metal_ironage_candle_" + position);
            ITEM_ALIASES.addAlias(old, ResourceLocation.withDefaultNamespace("candle"));
            BLOCK_ALIASES.addAlias(old, position.startsWith("floor") ? ResourceLocation.withDefaultNamespace("candle")
                    : ResourceLocation.fromNamespaceAndPath(Ironagefurniture.MODID, "light_metal_ironage_sconce_wall_candle_iron"));
        }
        for (String position : new String[]{"floor", "wall"}) for (String count : new String[]{"", "_two", "_three", "_four"}) {
            ResourceLocation target = ResourceLocation.fromNamespaceAndPath(Ironagefurniture.MODID,
                    "light_metal_ironage_sconce_" + position + "_candle_iron");
            if (!count.isEmpty()) BLOCK_ALIASES.addAlias(ResourceLocation.parse(target + count), target);
            BLOCK_ALIASES.addAlias(ResourceLocation.parse(target + count + "_unlit"), target);
        }
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
                        "chair_wood_ironage_" + family.replace("padded_single", "padded_red_single")
                                + "_biomeswevegone_" + newWood);
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

    private static void registerPaddedAliases(String wood) {
        for (String family : new String[]{"bench_padded", "bench_back_padded"}) {
            String old = "chair_wood_ironage_" + family + "_single_" + wood;
            String target = "chair_wood_ironage_" + family + "_red_single_" + wood;
            registerAlias(old, target);
            if (wood.equals("dark_oak")) registerAlias(old.replace("_dark_oak", "_big_oak"), target);
        }
    }

    /** Furniture-only names; this does not migrate the provider's world generation. */
    public static String currentBwgPath(String path) {
        for (var entry : BYG_TO_BWG.entrySet()) {
            String suffix = "_byg_" + entry.getKey();
            if (path.endsWith(suffix)) return path.substring(0, path.length() - suffix.length())
                    + "_biomeswevegone_" + entry.getValue();
        }
        return path;
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
