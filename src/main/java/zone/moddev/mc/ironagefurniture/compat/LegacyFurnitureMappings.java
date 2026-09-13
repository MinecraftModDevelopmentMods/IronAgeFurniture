package zone.moddev.mc.ironagefurniture.compat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/**
 * Registers aliases for IronAgeFurniture's retired Biomes O' Plenty cherry
 * furniture names. Minecraft now owns the cherry family, so those IDs resolve
 * to the corresponding vanilla-cherry furniture.
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
