package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.BitArray;
import org.junit.Test;

public class CfmChairMigrationTest {
    @Test public void bothPublishedNamingSchemesMapOnlyTheSixWoodenChairs() {
        assertEquals(12, CfmChairMigration.chairMappings().size());
        for (String wood : new String[]{"oak", "spruce", "birch", "jungle", "acacia", "dark_oak"}) {
            String target = "ironagefurniture:chair_wood_ironage_classic_" + wood;
            assertEquals(target, CfmChairMigration.chairMappings().get("cfm:chair_" + wood));
            assertEquals(target, CfmChairMigration.chairMappings().get("cfm:" + wood + "_chair"));
        }
        assertFalse(CfmChairMigration.chairMappings().containsKey("cfm:stone_chair"));
        assertFalse(CfmChairMigration.chairMappings().containsKey("cfm:stripped_oak_chair"));
    }
    @Test public void nestedStacksKeepCountNameEnchantmentsAndOtherData() {
        CompoundNBT chair = stack("cfm:chair_dark_oak");
        CompoundNBT root = new CompoundNBT(), box = stack("minecraft:shulker_box");
        ListNBT items = new ListNBT(); items.add(chair);
        box.put("BlockEntityTag", new CompoundNBT());
        box.getCompound("BlockEntityTag").put("Items", items);
        root.put("Inventory", new ListNBT()); root.getList("Inventory", 10).add(box);
        root.put("EnderItems", new ListNBT()); root.getList("EnderItems", 10).add(stack("cfm:oak_chair"));
        CompoundNBT oldTag = chair.getCompound("tag").copy();
        assertEquals(0, CfmChairMigration.rewriteItems(root, false, id -> true));
        assertEquals(0, CfmChairMigration.rewriteItems(root, true, id -> false));
        assertEquals(2, CfmChairMigration.rewriteItems(root, true, id -> true));
        assertEquals("ironagefurniture:chair_wood_ironage_classic_dark_oak", chair.getString("id"));
        assertEquals(7, chair.getByte("Count")); assertEquals(oldTag, chair.getCompound("tag"));
        assertFalse(chair.contains("Damage"));
        assertEquals(0, CfmChairMigration.rewriteItems(root, true, id -> true));
    }
    @Test public void paletteReplacementPreservesAllFacingsAndIsIdempotent() {
        for (String facing : new String[]{"north", "east", "south", "west"}) {
            CompoundNBT level = new CompoundNBT(), section = new CompoundNBT(), chair = new CompoundNBT();
            ListNBT palette = new ListNBT(), sections = new ListNBT();
            CompoundNBT air = new CompoundNBT(); air.putString("Name", "minecraft:air"); palette.add(air);
            chair.putString("Name", "cfm:spruce_chair");
            chair.put("Properties", new CompoundNBT()); chair.getCompound("Properties").putString("facing", facing);
            palette.add(chair); section.put("Palette", palette);
            BitArray cells = new BitArray(4, 4096); cells.setAt(47, 1); cells.setAt(58, 1);
            section.putLongArray("BlockStates", cells.getBackingLongArray()); sections.add(section); level.put("Sections", sections);
            assertEquals(0, CfmChairMigration.rewritePalettes(level, false, id -> true));
            assertEquals(2, CfmChairMigration.rewritePalettes(level, true, id -> true));
            assertEquals("ironagefurniture:chair_wood_ironage_classic_spruce", chair.getString("Name"));
            assertEquals(facing, chair.getCompound("Properties").getString("facing"));
            assertEquals("false", chair.getCompound("Properties").getString("waterlogged"));
            assertArrayEquals(cells.getBackingLongArray(), section.getLongArray("BlockStates"));
            assertEquals(0, CfmChairMigration.rewritePalettes(level, true, id -> true));
        }
    }
    private static CompoundNBT stack(String id) {
        CompoundNBT stack = new CompoundNBT(); stack.putString("id", id); stack.putByte("Count", (byte)7);
        stack.putShort("Damage", (short)0); stack.put("tag", new CompoundNBT());
        CompoundNBT tag = stack.getCompound("tag");
        tag.putString("CustomData", "keep");
        CompoundNBT display = new CompoundNBT(); display.putString("Name", "{\"text\":\"Heirloom\"}");
        tag.put("display", display);
        ListNBT enchantments = new ListNBT(); CompoundNBT enchantment = new CompoundNBT();
        enchantment.putString("id", "minecraft:unbreaking"); enchantment.putShort("lvl", (short)3);
        enchantments.add(enchantment); tag.put("Enchantments", enchantments);
        return stack;
    }
}
