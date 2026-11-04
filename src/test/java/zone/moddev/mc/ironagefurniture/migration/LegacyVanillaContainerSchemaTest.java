package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.junit.Test;

/** Optional providers must not bypass vanilla's original container schema. */
public class LegacyVanillaContainerSchemaTest {
    @Test public void oldContainerNamesKeepTheirCompleteInventory() {
        CompoundTag root = chunk(512);
        CompoundTag tile = root.getCompound("Level").getList("TileEntities", 10).getCompound(0);
        CompoundTag before = tile.copy();
        LegacyWorldDataHook.prepareVanillaTileEntitySchema(root);
        assertEquals("Chest", tile.getString("id"));
        before.putString("id", "Chest");
        assertEquals(before, tile);
        CompoundTag first = root.copy();
        LegacyWorldDataHook.prepareVanillaTileEntitySchema(root);
        assertEquals(first, root);
    }

    @Test public void namespacedSchemasRemainUntouched() {
        for (int version : new int[]{704, 1343, 2230, 2586}) {
            CompoundTag root = chunk(version), before = root.copy();
            LegacyWorldDataHook.prepareVanillaTileEntitySchema(root);
            assertEquals(before, root);
        }
    }

    @Test public void nativeFixerRetainsTheRestoredChestItems() {
        CompoundTag root = chunk(512);
        CompoundTag section = new CompoundTag(); section.putByte("Y", (byte)4);
        byte[] blocks = new byte[4096]; blocks[0] = 54;
        section.putByteArray("Blocks", blocks); section.putByteArray("Data", new byte[2048]);
        section.putByteArray("BlockLight", new byte[2048]); section.putByteArray("SkyLight", new byte[2048]);
        ListTag sections = new ListTag(); sections.add(section);
        CompoundTag level = root.getCompound("Level"); level.put("Sections", sections);
        level.putInt("xPos", 0); level.putInt("zPos", 0); level.putBoolean("TerrainPopulated", true);
        level.putBoolean("LightPopulated", true); level.put("Entities", new ListTag());
        LegacyWorldDataHook.prepareVanillaTileEntitySchema(root);
        CompoundTag fixed = fix(root);
        CompoundTag tile = fixed.getCompound("Level").getList("TileEntities", 10).getCompound(0);
        assertEquals("minecraft:chest", tile.getString("id"));
        CompoundTag item = tile.getList("Items", 10).getCompound(0);
        assertEquals("ironagefurniture:chair_wood_ironage_bench_log_single_oak", item.getString("id"));
        assertEquals(4, item.getByte("Count"));
        assertEquals("Stored bench", item.getCompound("tag").getString("Name"));
    }

    private static CompoundTag fix(CompoundTag root) {
        return (CompoundTag)net.minecraft.util.datafix.DataFixers.getDataFixer().update(
                net.minecraft.util.datafix.fixes.References.CHUNK,
                new com.mojang.serialization.Dynamic<>(net.minecraft.nbt.NbtOps.INSTANCE, root),
                512, 2586).getValue();
    }

    private static CompoundTag chunk(int version) {
        CompoundTag item = new CompoundTag();
        item.putString("id", "ironagefurniture:chair_wood_ironage_bench_log_single_oak");
        item.putByte("Count", (byte)4);
        item.putByte("Slot", (byte)0);
        CompoundTag custom = new CompoundTag(); custom.putString("Name", "Stored bench");
        item.put("tag", custom);
        ListTag items = new ListTag(); items.add(item);
        CompoundTag tile = new CompoundTag(); tile.putString("id", "minecraft:chest"); tile.put("Items", items);
        tile.putInt("x", 0); tile.putInt("y", 64); tile.putInt("z", 0);
        ListTag tiles = new ListTag(); tiles.add(tile);
        CompoundTag level = new CompoundTag(); level.put("TileEntities", tiles);
        CompoundTag root = new CompoundTag(); root.putInt("DataVersion", version); root.put("Level", level);
        return root;
    }
}
