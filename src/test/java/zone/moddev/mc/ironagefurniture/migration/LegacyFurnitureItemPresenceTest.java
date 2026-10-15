package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.junit.Test;

public class LegacyFurnitureItemPresenceTest {
    @Test public void unchangedLogBenchesProtectNestedContainersWithoutPlacedFurniture() {
        CompoundTag item = new CompoundTag();
        item.putString("id", "ironagefurniture:chair_wood_ironage_bench_log_single_oak");
        item.putByte("Count", (byte)4);
        ListTag cargo = new ListTag(); cargo.add(item);
        CompoundTag box = new CompoundTag(); box.put("Items", cargo);
        CompoundTag level = new CompoundTag(); level.put("AbstractContainerMenu", box);
        assertFalse(LegacyPaddedItemMigration.migrateChunkContents(level));
        assertTrue(LegacyPaddedItemMigration.containsFurnitureItems(level));
        item.putString("id", "minecraft:oak_planks");
        assertFalse(LegacyPaddedItemMigration.containsFurnitureItems(level));
        item.putString("id", "iafbygaddon:chair_wood_ironage_classic_byg_palm");
        assertTrue(LegacyPaddedItemMigration.containsFurnitureItems(level));
        item.remove("Count");
        assertFalse(LegacyPaddedItemMigration.containsFurnitureItems(level));
    }
}
