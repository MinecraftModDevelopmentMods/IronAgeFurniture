package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** The released 1.12 add-on's beds and tall chairs must cross the flattening too. */
public class BygPhaseFourMigrationTest {
    private static Map<String, String> woods() {
        Map<String, String> woods = new LinkedHashMap<>();
        for (String wood : new String[] {"aspen", "baobab", "blue_enchanted", "cherry", "cika", "cypress",
                "ebony", "fir", "green_enchanted", "holly", "jacaranda", "mahogany", "mangrove", "maple",
                "pine", "rainbow_eucalyptus", "redwood", "skyris", "willow", "witch_hazel", "zelkova"})
            woods.put(wood, "byg_" + wood);
        woods.put("frozen_oak", "byg_aspen"); woods.put("great_oak", "oak");
        woods.put("hawthorn", "byg_cherry"); woods.put("ironwood", "byg_ebony");
        woods.put("palm", "byg_palm"); woods.put("rowan", "byg_maple");
        return woods;
    }

    @Test public void everyWoodFormAndColourKeepsNestedDataAndConvertsOnlyOnce() {
        int cases = 0;
        for (Map.Entry<String, String> wood : woods().entrySet())
            for (String form : new String[] {"bed_wood_foot_", "bed_wood_foot_left_",
                    "bed_canopy_foot_lower_", "bed_canopy_foot_left_lower_",
                    "chair_wood_ironage_wingback_", "chair_wood_ironage_throne_"})
                for (UpholsteryColour colour : UpholsteryColour.values()) {
                    CompoundNBT item = new CompoundNBT(), tag = new CompoundNBT(), container = new CompoundNBT();
                    item.putString("id", "iafbygaddon:" + form + "byg_" + wood.getKey());
                    item.putByte("Count", (byte) 7);
                    item.putShort("Damage", (short) ((colour.getItemMetadata() + 1) % 16));
                    tag.putString("Color", colour.getName()); tag.putString("Name", "Saved furniture");
                    CompoundNBT shield = new CompoundNBT(); shield.putString("id", "minecraft:shield");
                    shield.putByte("Count", (byte) 1); shield.putString("CustomData", "Preserve complete shield data");
                    tag.put("ShieldItem", shield); item.put("tag", tag);
                    ListNBT nested = new ListNBT(); nested.add(item); container.put("Items", nested);
                    CompoundNBT root = new CompoundNBT(); root.put("BlockEntityTag", container);
                    assertTrue(LegacyPaddedItemMigration.migrateChunkContents(root));
                    String suffix = colour == UpholsteryColour.RED ? "" : "_" + colour.getName();
                    assertEquals("ironagefurniture:" + form + wood.getValue() + suffix, item.getString("id"));
                    assertEquals(7, item.getByte("Count")); assertFalse(item.contains("Damage"));
                    assertFalse(item.getCompound("tag").contains("Color"));
                    assertEquals("Saved furniture", item.getCompound("tag").getString("Name"));
                    assertEquals(shield, item.getCompound("tag").getCompound("ShieldItem"));
                    CompoundNBT saved = root.copy();
                    assertFalse(LegacyPaddedItemMigration.migrateChunkContents(root));
                    assertEquals(saved, root);
                    cases++;
                }
        assertEquals(27 * 6 * 16, cases);
    }
}
