package zone.moddev.mc.ironagefurniture.migration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.BitStorage;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Recovers supported wooden CFM chairs without converting unrelated furniture. */
public final class CfmChairMigration {
    private static final String CHANGED = "IronAgeFurnitureCfmChairsChanged";
    private static final Map<String, String> CHAIRS;
    private static final AtomicLong BLOCKS = new AtomicLong(), STACKS = new AtomicLong();
    static {
        Map<String, String> ids = new LinkedHashMap<>();
        for (String wood : new String[]{"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "crimson", "warped"}) {
            String target = "ironagefurniture:chair_wood_ironage_classic_" + wood;
            if (!wood.equals("crimson") && !wood.equals("warped"))
                ids.put("cfm:chair_" + wood, target); // Published 1.10/1.12 IDs.
            ids.put("cfm:" + wood + "_chair", target); // Published 1.14–1.17 IDs.
        }
        CHAIRS = Collections.unmodifiableMap(ids);
    }
    private CfmChairMigration() { }
    public static Map<String, String> chairMappings() { return CHAIRS; }
    static boolean enabled() {
        net.neoforged.fml.ModList mods = net.neoforged.fml.ModList.get();
        return mods != null && (!mods.isLoaded("cfm") || IronAgeFurnitureConfiguration.enabled(IronAgeFurnitureConfiguration.FORCE_CFM_CONVERSION));
    }
    private static boolean targetExists(String id) { return net.minecraft.core.registries.BuiltInRegistries.BLOCK.containsKey(Identifier.parse(id)); }
    static String legacyTarget(String id) {
        String target = CHAIRS.get(id);
        return enabled() && target != null && targetExists(target) ? target : null;
    }
    static void recordLegacyBlocks(long count) { BLOCKS.addAndGet(count); }

    /** Runs before the chunk's palette and item stacks become live game objects. */
    static void prepareChunk(CompoundTag level) {
        if (!enabled()) return;
        int blocks = rewritePalettes(level, true, CfmChairMigration::targetExists);
        int stacks = rewriteItems(level, true, CfmChairMigration::targetExists);
        if (blocks + stacks > 0) level.putBoolean(CHANGED, true);
        BLOCKS.addAndGet(blocks);
        STACKS.addAndGet(stacks);
    }

    /** Player inventory decoding would otherwise discard a removed mod's items. */
    public static void migratePlayerData(CompoundTag player) {
        if (enabled()) STACKS.addAndGet(rewriteItems(player, true, CfmChairMigration::targetExists));
    }

    /** The separate 1.17 entity-region reader must run the same stack conversion. */
    public static void migrateEntityData(CompoundTag entities) {
        if (enabled()) STACKS.addAndGet(rewriteItems(entities, true, CfmChairMigration::targetExists));
    }

    static int rewriteItems(CompoundTag root, boolean convert, Predicate<String> targetExists) {
        if (!convert) return 0;
        int changed = 0;
        String target = CHAIRS.get(root.getStringOr("id", ""));
        if ((root.contains("Count") || root.contains("count")) && target != null && targetExists.test(target)) {
            root.putString("id", target);
            root.remove("Damage");
            changed++;
        }
        for (String key : new ArrayList<>(root.keySet())) {
            Tag child = root.get(key);
            if (child instanceof CompoundTag) changed += rewriteItems((CompoundTag)child, true, targetExists);
            else if (child instanceof ListTag) changed += rewriteList((ListTag)child, targetExists);
        }
        return changed;
    }
    private static int rewriteList(ListTag list, Predicate<String> targetExists) {
        int changed = 0;
        for (Tag child : list) {
            if (child instanceof CompoundTag) changed += rewriteItems((CompoundTag)child, true, targetExists);
            else if (child instanceof ListTag) changed += rewriteList((ListTag)child, targetExists);
        }
        return changed;
    }

    static int rewritePalettes(CompoundTag level, boolean convert, Predicate<String> targetExists) {
        if (!convert) return 0;
        int changed = 0;
        for (Tag entry : level.getListOrEmpty("Sections")) {
            CompoundTag section = (CompoundTag)entry;
            ListTag palette = section.getListOrEmpty("Palette");
            if (palette.isEmpty()) continue;
            int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            long[] data = section.getLongArray("BlockStates").orElseGet(() -> new long[0]);
            BitStorage states = LegacyPaletteData.read(bits, data);
            if (states == null) continue;
            int[] counts = new int[palette.size()];
            boolean valid = true;
            for (int cell = 0; cell < 4096; cell++) {
                int index = states.get(cell);
                if (index >= counts.length) { valid = false; break; }
                counts[index]++;
            }
            if (!valid) continue;
            for (int index = 0; index < palette.size(); index++) {
                CompoundTag state = palette.getCompoundOrEmpty(index);
                String target = CHAIRS.get(state.getStringOr("Name", ""));
                if (target == null || !targetExists.test(target)) continue;
                state.putString("Name", target);
                CompoundTag properties = state.getCompoundOrEmpty("Properties");
                // Both published chair formats use the same horizontal facing.
                properties.putString("waterlogged", "false");
                state.put("Properties", properties);
                changed += counts[index];
            }
        }
        return changed;
    }

}
