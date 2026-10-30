package zone.moddev.mc.ironagefurniture.compat;

import java.util.Map;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/**
 * Registers aliases for IronAgeFurniture's own retired optional-wood registry
 * names. This does not attempt to make a complete BYG world compatible with
 * BWG.
 */
public final class LegacyFurnitureMappings {
    private static final AliasTable<Block> BLOCK_ALIASES = new AliasTable<>(net.minecraft.core.registries.Registries.BLOCK, BuiltInRegistries.BLOCK);
    private static final AliasTable<Item> ITEM_ALIASES = new AliasTable<>(net.minecraft.core.registries.Registries.ITEM, BuiltInRegistries.ITEM);

    /** Retains live registry lookups as well as saved-world missing mappings. */
    private static final class AliasTable<T> {
        private final net.minecraft.resources.ResourceKey<net.minecraft.core.Registry<T>> key;
        private final net.minecraft.core.Registry<T> registry;
        private final java.util.Map<ResourceLocation, ResourceLocation> aliases = new java.util.LinkedHashMap<>();
        AliasTable(net.minecraft.resources.ResourceKey<net.minecraft.core.Registry<T>> key, net.minecraft.core.Registry<T> registry) {
            this.key = key; this.registry = registry;
        }
        void addAlias(ResourceLocation oldId, ResourceLocation target) { aliases.put(oldId, target); }
        void register(IEventBus bus) {
            bus.addListener((net.minecraftforge.registries.RegisterEvent event) -> {
                if (event.getRegistryKey().equals(key)
                        && event.getForgeRegistry() instanceof net.minecraftforge.registries.ForgeRegistry<?> forge) {
                    aliases.forEach((oldId, target) -> {
                        // Forge copies aliases before entries while freezing. An
                        // alias must not hide a registered compatibility block.
                        boolean addonCherry = key.equals(net.minecraft.core.registries.Registries.BLOCK)
                                && net.minecraftforge.fml.ModList.get().isLoaded("iafbopaddon")
                                && (oldId.getPath().equals("chair_wood_ironage_bench_padded_single_cherry")
                                    || oldId.getPath().equals("chair_wood_ironage_bench_back_padded_single_cherry"));
                        if (!addonCherry && !registry.containsKey(oldId)) forge.addAlias(oldId, target);
                    });
                }
            });
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener((net.minecraftforge.registries.MissingMappingsEvent event) -> {
                for (var mapping : event.getMappings(key, Ironagefurniture.MODID)) {
                    ResourceLocation target = aliases.get(mapping.getKey());
                    if (target != null && registry.containsKey(target)) mapping.remap(registry.get(target));
                }
            });
        }
    }

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
        if (net.minecraftforge.fml.ModList.get().isLoaded("biomesoplenty")) {
            for (String wood : zone.moddev.mc.ironagefurniture.init.PhaseFourBopFurniture.WOODS)
                registerPaddedAliases("biomesoplenty_" + wood);
        }
        for (String position : new String[]{"floor", "floor_unlit", "wall", "wall_unlit"}) {
            ResourceLocation old = new ResourceLocation(Ironagefurniture.MODID,
                    "light_metal_ironage_candle_" + position);
            ITEM_ALIASES.addAlias(old, new ResourceLocation("candle"));
            BLOCK_ALIASES.addAlias(old, position.startsWith("floor") ? new ResourceLocation("candle")
                    : new ResourceLocation(Ironagefurniture.MODID, "light_metal_ironage_sconce_wall_candle_iron"));
        }
        for (String position : new String[]{"floor", "wall"}) for (String count : new String[]{"", "_two", "_three", "_four"}) {
            ResourceLocation target = new ResourceLocation(Ironagefurniture.MODID,
                    "light_metal_ironage_sconce_" + position + "_candle_iron");
            if (!count.isEmpty()) BLOCK_ALIASES.addAlias(new ResourceLocation(target + count), target);
            BLOCK_ALIASES.addAlias(new ResourceLocation(target + count + "_unlit"), target);
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
        ResourceLocation oldId = new ResourceLocation(Ironagefurniture.MODID, oldPath);
        ResourceLocation targetId = new ResourceLocation(
                Ironagefurniture.MODID, targetPath);
        BLOCK_ALIASES.addAlias(oldId, targetId);
        ITEM_ALIASES.addAlias(oldId, targetId);
    }
}
