package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import org.junit.Test;

public class LegacyFurnitureItemPresenceTest {
    @Test public void unchangedLogBenchesProtectNestedContainersWithoutPlacedFurniture() {
        CompoundNBT item = new CompoundNBT();
        item.putString("id", "ironagefurniture:chair_wood_ironage_bench_log_single_oak");
        item.putByte("Count", (byte)4);
        ListNBT cargo = new ListNBT(); cargo.add(item);
        CompoundNBT box = new CompoundNBT(); box.put("Items", cargo);
        CompoundNBT level = new CompoundNBT(); level.put("Container", box);
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
