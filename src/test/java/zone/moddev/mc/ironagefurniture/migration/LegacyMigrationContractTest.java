package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import net.minecraft.nbt.CompoundTag;

/** The native bridge must not rewrite data from a newer game version. */
public class LegacyMigrationContractTest {
    @Test
    public void leavesFutureItemDataUntouched() {
        CompoundTag item = new CompoundTag();
        item.putString("id", "ironagefurniture:chair_wood_ironage_shield_oak");
        item.putByte("Count", (byte) 1);
        CompoundTag payload = new CompoundTag();
        payload.putString("OtherModPayload", "Keep this");
        CompoundTag oldShield = new CompoundTag();
        oldShield.putBoolean("Empty", true);
        payload.put("IAFShieldChair", oldShield);
        item.put("tag", payload);
        CompoundTag before = item.copy();
        NativeLegacyBridge.prepareItems(item, 2731);
        assertEquals(before, item);
    }

    @Test
    public void leavesFutureChunkDataUntouched() {
        CompoundTag chunk = new CompoundTag();
        chunk.putInt("DataVersion", 2731);
        chunk.put("Level", new CompoundTag());
        chunk.putString("OtherModPayload", "Keep this");
        CompoundTag before = chunk.copy();
        NativeLegacyBridge.prepareChunk(chunk, 2731);
        assertEquals(before, chunk);
    }
}
