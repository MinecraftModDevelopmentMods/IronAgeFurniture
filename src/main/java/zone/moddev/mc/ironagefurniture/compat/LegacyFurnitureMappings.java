package zone.moddev.mc.ironagefurniture.compat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/**
 * Keeps retired registry names readable, including old mining and crafting
 * statistics. Aliases point to existing items and do not add creative entries.
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
        for (String family : BASIC_FAMILIES) if (!family.contains("padded"))
            registerAlias("chair_wood_ironage_" + family + "_big_oak", "chair_wood_ironage_" + family + "_dark_oak");
        for (String form : new String[]{"bed_wood_foot_", "bed_wood_foot_left_", "bed_canopy_foot_lower_", "bed_canopy_foot_left_lower_",
                "chair_wood_ironage_wingback_", "chair_wood_ironage_throne_"})
            registerAlias(form + "big_oak", form + "dark_oak");
        for (String wood : zone.moddev.mc.ironagefurniture.init.PhaseFourFurniture.VANILLA_WOODS) {
            for (String family : new String[]{"bench_padded", "bench_back_padded"}) {
                String old = "chair_wood_ironage_" + family + "_single_" + wood;
                String target = "chair_wood_ironage_" + family + "_red_single_" + wood;
                registerAlias(old, target);
                if (wood.equals("dark_oak")) registerAlias(old.replace("_dark_oak", "_big_oak"), target);
            }
        }
        for (String position : new String[]{"floor", "floor_unlit", "wall", "wall_unlit"}) {
            Identifier old = Identifier.fromNamespaceAndPath(Ironagefurniture.MODID, "light_metal_ironage_candle_" + position);
            ITEM_ALIASES.addAlias(old, Identifier.withDefaultNamespace("candle"));
            BLOCK_ALIASES.addAlias(old, position.startsWith("floor") ? Identifier.withDefaultNamespace("candle")
                    : Identifier.fromNamespaceAndPath(Ironagefurniture.MODID, "light_metal_ironage_sconce_wall_candle_iron"));
        }
        for (String position : new String[]{"floor", "wall"}) for (String count : new String[]{"", "_two", "_three", "_four"}) {
            String target = "light_metal_ironage_sconce_" + position + "_candle_iron";
            if (!count.isEmpty()) BLOCK_ALIASES.addAlias(Identifier.fromNamespaceAndPath(Ironagefurniture.MODID, target + count),
                    Identifier.fromNamespaceAndPath(Ironagefurniture.MODID, target));
            BLOCK_ALIASES.addAlias(Identifier.fromNamespaceAndPath(Ironagefurniture.MODID, target + count + "_unlit"),
                    Identifier.fromNamespaceAndPath(Ironagefurniture.MODID, target));
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

    private static void registerCherryAlias(String targetPath) {
        String oldPath = targetPath.substring(0, targetPath.length() - "_cherry".length())
                + "_biomesoplenty_cherry";
        registerAlias(oldPath, targetPath);
    }

    private static void registerAlias(String oldPath, String targetPath) {
        Identifier oldId = Identifier.fromNamespaceAndPath(Ironagefurniture.MODID, oldPath);
        Identifier targetId = Identifier.fromNamespaceAndPath(
                Ironagefurniture.MODID, targetPath);
        BLOCK_ALIASES.addAlias(oldId, targetId);
        ITEM_ALIASES.addAlias(oldId, targetId);
    }
}
