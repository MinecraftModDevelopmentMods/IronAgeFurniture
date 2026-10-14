package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import org.junit.Test;

/** Optional providers must not bypass vanilla's original container schema. */
public class LegacyVanillaContainerSchemaTest {
    @Test public void oldContainerNamesKeepTheirCompleteInventory() {
        CompoundNBT root = chunk(512);
        CompoundNBT tile = root.getCompound("Level").getList("TileEntities", 10).getCompound(0);
        CompoundNBT before = tile.copy();
        LegacyWorldDataHook.prepareVanillaTileEntitySchema(root);
        assertEquals("Chest", tile.getString("id"));
        before.putString("id", "Chest");
        assertEquals(before, tile);
        CompoundNBT first = root.copy();
        LegacyWorldDataHook.prepareVanillaTileEntitySchema(root);
        assertEquals(first, root);
    }

    @Test public void namespacedSchemasRemainUntouched() {
        for (int version : new int[]{704, 1343, 2230, 2586}) {
            CompoundNBT root = chunk(version), before = root.copy();
            LegacyWorldDataHook.prepareVanillaTileEntitySchema(root);
            assertEquals(before, root);
        }
    }

    @Test public void nativeFixerRetainsTheRestoredChestItems() {
        CompoundNBT root = chunk(512);
        CompoundNBT section = new CompoundNBT(); section.putByte("Y", (byte)4);
        byte[] blocks = new byte[4096]; blocks[0] = 54;
        section.putByteArray("Blocks", blocks); section.putByteArray("Data", new byte[2048]);
        section.putByteArray("BlockLight", new byte[2048]); section.putByteArray("SkyLight", new byte[2048]);
        ListNBT sections = new ListNBT(); sections.add(section);
        CompoundNBT level = root.getCompound("Level"); level.put("Sections", sections);
        level.putInt("xPos", 0); level.putInt("zPos", 0); level.putBoolean("TerrainPopulated", true);
        level.putBoolean("LightPopulated", true); level.put("Entities", new ListNBT());
        LegacyWorldDataHook.prepareVanillaTileEntitySchema(root);
        CompoundNBT fixed = fix(root);
        CompoundNBT tile = fixed.getCompound("Level").getList("TileEntities", 10).getCompound(0);
        assertEquals("minecraft:chest", tile.getString("id"));
        CompoundNBT item = tile.getList("Items", 10).getCompound(0);
        assertEquals("ironagefurniture:chair_wood_ironage_bench_log_single_oak", item.getString("id"));
        assertEquals(4, item.getByte("Count"));
        assertEquals("Stored bench", item.getCompound("tag").getString("Name"));
    }

    private static CompoundNBT fix(CompoundNBT root) {
        return (CompoundNBT)net.minecraft.util.datafix.DataFixesManager.getDataFixer().update(
                net.minecraft.util.datafix.TypeReferences.CHUNK,
                new com.mojang.serialization.Dynamic<>(net.minecraft.nbt.NBTDynamicOps.INSTANCE, root),
                512, 2586).getValue();
    }

    private static CompoundNBT chunk(int version) {
        CompoundNBT item = new CompoundNBT();
        item.putString("id", "ironagefurniture:chair_wood_ironage_bench_log_single_oak");
        item.putByte("Count", (byte)4);
        item.putByte("Slot", (byte)0);
        CompoundNBT custom = new CompoundNBT(); custom.putString("Name", "Stored bench");
        item.put("tag", custom);
        ListNBT items = new ListNBT(); items.add(item);
        CompoundNBT tile = new CompoundNBT(); tile.putString("id", "minecraft:chest"); tile.put("Items", items);
        tile.putInt("x", 0); tile.putInt("y", 64); tile.putInt("z", 0);
        ListNBT tiles = new ListNBT(); tiles.add(tile);
        CompoundNBT level = new CompoundNBT(); level.put("TileEntities", tiles);
        CompoundNBT root = new CompoundNBT(); root.putInt("DataVersion", version); root.put("Level", level);
        return root;
    }
}
