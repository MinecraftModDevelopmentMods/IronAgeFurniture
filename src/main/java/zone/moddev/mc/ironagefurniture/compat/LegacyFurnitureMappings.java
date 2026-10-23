package zone.moddev.mc.ironagefurniture.compat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/**
 * Registers retired furniture aliases supported by this target. Foreign wood
 * names are preserved when no compatible provider is available.
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

    /** No compatible BWG provider exists on this target; keep foreign saved names intact. */
    public static String currentBwgPath(String path) {
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
