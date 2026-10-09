package zone.moddev.mc.ironagefurniture.migration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.BitArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Recovers the six wooden CFM chairs without converting unrelated furniture. */
@Mod.EventBusSubscriber(modid = Ironagefurniture.MODID)
public final class CfmChairMigration {
    private static final String CHANGED = "IronAgeFurnitureCfmChairsChanged";
    private static final Map<String, String> CHAIRS;
    private static final AtomicLong BLOCKS = new AtomicLong(), STACKS = new AtomicLong();
    static {
        Map<String, String> ids = new LinkedHashMap<>();
        for (String wood : new String[]{"oak", "spruce", "birch", "jungle", "acacia", "dark_oak"}) {
            String target = "ironagefurniture:chair_wood_ironage_classic_" + wood;
            ids.put("cfm:chair_" + wood, target); // Published 1.10/1.12 IDs.
            ids.put("cfm:" + wood + "_chair", target); // Published 1.14 IDs.
        }
        CHAIRS = Collections.unmodifiableMap(ids);
    }
    private CfmChairMigration() { }
    public static Map<String, String> chairMappings() { return CHAIRS; }
    static boolean enabled() {
        ModList mods = ModList.get();
        return mods != null && (!mods.isLoaded("cfm") || IronAgeFurnitureConfiguration.CLIENT.FORCE_CFM_CHAIR_CONVERSION.get());
    }
    private static boolean targetExists(String id) { return ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(id)); }
    static String legacyTarget(String id) {
        String target = CHAIRS.get(id);
        return enabled() && target != null && targetExists(target) ? target : null;
    }
    static void recordLegacyBlocks(long count) { BLOCKS.addAndGet(count); }

    /** Runs before the chunk's palette and item stacks become live game objects. */
    static void prepareChunk(CompoundNBT level) {
        if (!enabled()) return;
        int blocks = rewritePalettes(level, true, CfmChairMigration::targetExists);
        int stacks = rewriteItems(level, true, CfmChairMigration::targetExists);
        if (blocks + stacks > 0) level.putBoolean(CHANGED, true);
        BLOCKS.addAndGet(blocks);
        STACKS.addAndGet(stacks);
    }

    /** Player inventory decoding would otherwise discard a removed mod's items. */
    public static void migratePlayerData(CompoundNBT player) {
        if (enabled()) STACKS.addAndGet(rewriteItems(player, true, CfmChairMigration::targetExists));
    }

    static int rewriteItems(CompoundNBT root, boolean convert, Predicate<String> targetExists) {
        if (!convert) return 0;
        int changed = 0;
        String target = CHAIRS.get(root.getString("id"));
        if (root.contains("Count", 99) && target != null && targetExists.test(target)) {
            root.putString("id", target);
            root.remove("Damage");
            changed++;
        }
        for (String key : new ArrayList<>(root.keySet())) {
            INBT child = root.get(key);
            if (child instanceof CompoundNBT) changed += rewriteItems((CompoundNBT)child, true, targetExists);
            else if (child instanceof ListNBT) changed += rewriteList((ListNBT)child, targetExists);
        }
        return changed;
    }
    private static int rewriteList(ListNBT list, Predicate<String> targetExists) {
        int changed = 0;
        for (INBT child : list) {
            if (child instanceof CompoundNBT) changed += rewriteItems((CompoundNBT)child, true, targetExists);
            else if (child instanceof ListNBT) changed += rewriteList((ListNBT)child, targetExists);
        }
        return changed;
    }

    static int rewritePalettes(CompoundNBT level, boolean convert, Predicate<String> targetExists) {
        if (!convert) return 0;
        int changed = 0;
        for (INBT entry : level.getList("Sections", 10)) {
            CompoundNBT section = (CompoundNBT)entry;
            ListNBT palette = section.getList("Palette", 10);
            if (palette.isEmpty()) continue;
            int bits = Math.max(4, 32 - Integer.numberOfLeadingZeros(palette.size() - 1));
            long[] data = section.getLongArray("BlockStates");
            if (data.length != (4096 * bits + 63) / 64) continue;
            BitArray states = new BitArray(bits, 4096, data);
            int[] counts = new int[palette.size()];
            boolean valid = true;
            for (int cell = 0; cell < 4096; cell++) {
                int index = states.getAt(cell);
                if (index >= counts.length) { valid = false; break; }
                counts[index]++;
            }
            if (!valid) continue;
            for (int index = 0; index < palette.size(); index++) {
                CompoundNBT state = palette.getCompound(index);
                String target = CHAIRS.get(state.getString("Name"));
                if (target == null || !targetExists.test(target)) continue;
                state.putString("Name", target);
                CompoundNBT properties = state.getCompound("Properties");
                // Both published chair formats use the same horizontal facing.
                properties.putString("waterlogged", "false");
                state.put("Properties", properties);
                changed += counts[index];
            }
        }
        return changed;
    }

    @SubscribeEvent public static void chunkLoaded(ChunkDataEvent.Load event) {
        CompoundNBT level = event.getData().getCompound("Level");
        if (level.getBoolean(CHANGED)) {
            event.getChunk().setModified(true);
            level.remove(CHANGED);
        }
    }
    @SubscribeEvent public static void stopping(FMLServerStoppingEvent event) {
        long blocks = BLOCKS.getAndSet(0), stacks = STACKS.getAndSet(0);
        if (blocks + stacks == 0) return;
        ServerWorld world = event.getServer().getWorld(DimensionType.OVERWORLD);
        Totals totals = world.getSavedData().getOrCreate(Totals::new, Totals.NAME);
        totals.blocks += blocks;
        totals.stacks += stacks;
        totals.markDirty();
        world.getSavedData().save();
        org.apache.logging.log4j.LogManager.getLogger().info(
                "Converted {} CFM wooden chairs and {} chair stacks in '{}'; world totals: {} chairs, {} stacks",
                blocks, stacks, event.getServer().getFolderName(), totals.blocks, totals.stacks);
    }
    private static final class Totals extends WorldSavedData {
        static final String NAME = "ironagefurniture_cfm_chair_migration";
        long blocks, stacks;
        Totals() { super(NAME); }
        @Override public void read(CompoundNBT data) { blocks = data.getLong("Chairs"); stacks = data.getLong("Stacks"); }
        @Override public CompoundNBT write(CompoundNBT data) { data.putLong("Chairs", blocks); data.putLong("Stacks", stacks); return data; }
    }

    /** Only our supported chairs are remapped; other CFM content is left alone. */
    @Mod.EventBusSubscriber(modid = Ironagefurniture.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class MissingChairs {
        @SubscribeEvent public static void blocks(RegistryEvent.MissingMappings<Block> event) {
            for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings()) {
                String target = legacyTarget(mapping.key.toString());
                if (target != null) mapping.remap(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(target)));
            }
        }
        @SubscribeEvent public static void items(RegistryEvent.MissingMappings<Item> event) {
            for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings()) {
                String target = legacyTarget(mapping.key.toString());
                if (target != null) mapping.remap(ForgeRegistries.ITEMS.getValue(new ResourceLocation(target)));
            }
        }
    }
}
