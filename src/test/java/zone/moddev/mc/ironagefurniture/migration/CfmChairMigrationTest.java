package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.BitStorage;
import org.junit.Test;

public class CfmChairMigrationTest {
    @Test public void separateEntityRegionsPreserveDroppedAndNestedChairData() {
        CompoundTag region = new CompoundTag(), entity = new CompoundTag();
        entity.putString("id", "minecraft:item");
        entity.put("Item", stack("cfm:oak_chair"));
        ListTag entities = new ListTag(); entities.add(entity); region.put("Entities", entities);
        CompoundTag before = entity.getCompound("Item").getCompound("tag").copy();
        assertEquals(1, CfmChairMigration.rewriteItems(region, true, id -> true));
        assertEquals("ironagefurniture:chair_wood_ironage_classic_oak", entity.getCompound("Item").getString("id"));
        assertEquals(7, entity.getCompound("Item").getByte("Count"));
        assertEquals(before, entity.getCompound("Item").getCompound("tag"));
        assertEquals(0, CfmChairMigration.rewriteItems(region, true, id -> true));
    }
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
        CompoundTag chair = stack("cfm:chair_dark_oak");
        CompoundTag root = new CompoundTag(), box = stack("minecraft:shulker_box");
        ListTag items = new ListTag(); items.add(chair);
        box.put("BlockEntityTag", new CompoundTag());
        box.getCompound("BlockEntityTag").put("Items", items);
        root.put("Inventory", new ListTag()); root.getList("Inventory", 10).add(box);
        root.put("EnderItems", new ListTag()); root.getList("EnderItems", 10).add(stack("cfm:oak_chair"));
        CompoundTag oldTag = chair.getCompound("tag").copy();
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
            CompoundTag level = new CompoundTag(), section = new CompoundTag(), chair = new CompoundTag();
            ListTag palette = new ListTag(), sections = new ListTag();
            CompoundTag air = new CompoundTag(); air.putString("Name", "minecraft:air"); palette.add(air);
            chair.putString("Name", "cfm:spruce_chair");
            chair.put("Properties", new CompoundTag()); chair.getCompound("Properties").putString("facing", facing);
            palette.add(chair); section.put("Palette", palette);
            BitStorage cells = new BitStorage(4, 4096); cells.set(47, 1); cells.set(58, 1);
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
    private static CompoundTag stack(String id) {
        return namedStack(id);
    }
    @Test public void nativeAndPre116LargePalettesPreservePackedCells() {
        for (boolean padded : new boolean[]{false, true}) {
            int bits = 5, perLong = 64 / bits;
            long[] data = new long[padded ? (4096 + perLong - 1) / perLong : (4096 * bits + 63) / 64];
            ListTag palette = new ListTag();
            for (int index = 0; index < 17; index++) {
                CompoundTag state = new CompoundTag();
                state.putString("Name", index == 16 ? "cfm:oak_chair" : "minecraft:stone");
                palette.add(state);
            }
            for (int cell : new int[]{12, 25, 4095}) {
                int word = padded ? cell / perLong : cell * bits / 64;
                int offset = padded ? cell % perLong * bits : cell * bits % 64;
                data[word] |= 16L << offset;
                if (!padded && offset + bits > 64) data[word + 1] |= 16L >>> (64 - offset);
            }
            CompoundTag section = new CompoundTag(), level = new CompoundTag();
            section.put("Palette", palette); section.putLongArray("BlockStates", data);
            ListTag sections = new ListTag(); sections.add(section); level.put("Sections", sections);
            assertEquals(3, CfmChairMigration.rewritePalettes(level, true, id -> true));
            assertArrayEquals(data, section.getLongArray("BlockStates"));
            assertEquals("ironagefurniture:chair_wood_ironage_classic_oak", palette.getCompound(16).getString("Name"));
            assertEquals(0, CfmChairMigration.rewritePalettes(level, true, id -> true));
        }
    }
    private static CompoundTag namedStack(String id) {
        CompoundTag stack = new CompoundTag(); stack.putString("id", id); stack.putByte("Count", (byte)7);
        stack.putShort("Damage", (short)0); stack.put("tag", new CompoundTag());
        CompoundTag tag = stack.getCompound("tag");
        tag.putString("CustomData", "keep");
        CompoundTag display = new CompoundTag(); display.putString("Name", "{\"text\":\"Heirloom\"}");
        tag.put("display", display);
        ListTag enchantments = new ListTag(); CompoundTag enchantment = new CompoundTag();
        enchantment.putString("id", "minecraft:unbreaking"); enchantment.putShort("lvl", (short)3);
        enchantments.add(enchantment); tag.put("Enchantments", enchantments);
        return stack;
    }
}
