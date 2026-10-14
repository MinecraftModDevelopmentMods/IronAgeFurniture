package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.BitArray;
import org.junit.Test;

public class CfmChairMigrationTest {
    @Test public void publishedNamingSchemesIncludeNetherWoodWithoutInventingOldIds() {
        assertEquals(14, CfmChairMigration.chairMappings().size());
        for (String wood : new String[]{"oak", "spruce", "birch", "jungle", "acacia", "dark_oak"}) {
            String target = "ironagefurniture:chair_wood_ironage_classic_" + wood;
            assertEquals(target, CfmChairMigration.chairMappings().get("cfm:chair_" + wood));
            assertEquals(target, CfmChairMigration.chairMappings().get("cfm:" + wood + "_chair"));
        }
        assertFalse(CfmChairMigration.chairMappings().containsKey("cfm:stone_chair"));
        for (String wood : new String[]{"crimson", "warped"}) {
            assertEquals("ironagefurniture:chair_wood_ironage_classic_" + wood,
                    CfmChairMigration.chairMappings().get("cfm:" + wood + "_chair"));
            assertFalse(CfmChairMigration.chairMappings().containsKey("cfm:chair_" + wood));
        }
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
            BitArray cells = new BitArray(4, 4096); cells.set(47, 1); cells.set(58, 1);
            section.putLongArray("BlockStates", cells.getRaw()); sections.add(section); level.put("Sections", sections);
            assertEquals(0, CfmChairMigration.rewritePalettes(level, false, id -> true));
            assertEquals(2, CfmChairMigration.rewritePalettes(level, true, id -> true));
            assertEquals("ironagefurniture:chair_wood_ironage_classic_spruce", chair.getString("Name"));
            assertEquals(facing, chair.getCompound("Properties").getString("facing"));
            assertEquals("false", chair.getCompound("Properties").getString("waterlogged"));
            assertArrayEquals(cells.getRaw(), section.getLongArray("BlockStates"));
            assertEquals(0, CfmChairMigration.rewritePalettes(level, true, id -> true));
        }
    }
    private static CompoundNBT stack(String id) {
        return namedStack(id);
    }
    @Test public void nativeAndPre116LargePalettesPreservePackedCells() {
        for (boolean padded : new boolean[]{false, true}) {
            int bits = 5, perLong = 64 / bits;
            long[] data = new long[padded ? (4096 + perLong - 1) / perLong : (4096 * bits + 63) / 64];
            ListNBT palette = new ListNBT();
            for (int index = 0; index < 17; index++) {
                CompoundNBT state = new CompoundNBT();
                state.putString("Name", index == 16 ? "cfm:oak_chair" : "minecraft:stone");
                palette.add(state);
            }
            for (int cell : new int[]{12, 25, 4095}) {
                int word = padded ? cell / perLong : cell * bits / 64;
                int offset = padded ? cell % perLong * bits : cell * bits % 64;
                data[word] |= 16L << offset;
                if (!padded && offset + bits > 64) data[word + 1] |= 16L >>> (64 - offset);
            }
            CompoundNBT section = new CompoundNBT(), level = new CompoundNBT();
            section.put("Palette", palette); section.putLongArray("BlockStates", data);
            ListNBT sections = new ListNBT(); sections.add(section); level.put("Sections", sections);
            assertEquals(3, CfmChairMigration.rewritePalettes(level, true, id -> true));
            assertArrayEquals(data, section.getLongArray("BlockStates"));
            assertEquals("ironagefurniture:chair_wood_ironage_classic_oak", palette.getCompound(16).getString("Name"));
            assertEquals(0, CfmChairMigration.rewritePalettes(level, true, id -> true));
        }
    }
    private static CompoundNBT namedStack(String id) {
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
