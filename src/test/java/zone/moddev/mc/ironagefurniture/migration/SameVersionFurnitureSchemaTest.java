package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.junit.Test;
import static org.junit.Assert.*;

public class SameVersionFurnitureSchemaTest {
    private static CompoundTag chunk(String part, boolean current) {
        CompoundTag root = new CompoundTag(), level = new CompoundTag(), section = new CompoundTag();
        root.putInt("DataVersion", 2730); root.put("Level", level);
        CompoundTag state = new CompoundTag(), properties = new CompoundTag();
        state.putString("Name", part.contains("_") ? "ironagefurniture:bed_canopy_foot_lower_oak" : "ironagefurniture:bed_wood_foot_oak");
        properties.putString("facing", "north"); properties.putString("part", part);
        if (current) properties.putString("colour", "blue");
        state.put("Properties", properties); ListTag palette = new ListTag(); palette.add(state); section.put("Palette", palette);
        ListTag sections = new ListTag(); sections.add(section); level.put("Sections", sections);
        return root;
    }
    private static CompoundTag properties(CompoundTag root) {
        return root.getCompound("Level").getList("Sections", 10).getCompound(0).getList("Palette", 10).getCompound(0).getCompound("Properties");
    }
    @Test public void earlierSameVersionBedsConvertExactlyOnce() {
        for (String part : new String[]{"foot", "head_upper"}) {
            CompoundTag saved = chunk(part, false);
            NativeLegacyBridge.prepareChunk(saved, 2730);
            assertEquals("south", properties(saved).getString("facing"));
            assertEquals("red", properties(saved).getString("colour"));
            if (part.equals("head_upper")) {
                assertEquals("head", properties(saved).getString("part"));
                assertEquals("true", properties(saved).getString("upper"));
            }
            CompoundTag once = saved.copy();
            NativeLegacyBridge.prepareChunk(saved, 2730);
            assertEquals(once, saved);
        }
    }
    @Test public void currentSameVersionBedsKeepTheirFacingAndColour() {
        CompoundTag saved = chunk("head", true), before = saved.copy();
        NativeLegacyBridge.prepareChunk(saved, 2730);
        assertEquals(before, saved);
    }
}
