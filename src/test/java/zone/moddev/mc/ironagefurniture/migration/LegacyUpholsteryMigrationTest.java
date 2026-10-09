package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.BitArray;
import net.minecraft.util.Direction;
import net.minecraft.util.registry.Bootstrap;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.*;
import zone.moddev.mc.ironagefurniture.api.enumerations.*;

public class LegacyUpholsteryMigrationTest {
    @BeforeClass public static void bootstrap() { Bootstrap.register(); }

    @Test public void unrelatedLegacyItemNamesDoNotBreakFurnitureRecovery() {
        CompoundNBT chest = new CompoundNBT(), other = new CompoundNBT(), furniture = new CompoundNBT();
        other.putString("id", "mca:ToyTrain"); other.putByte("Count", (byte) 2);
        CompoundNBT cargo = new CompoundNBT(); cargo.putString("Name", "Keep this unrelated data"); other.put("tag", cargo);
        CompoundNBT before = other.copy();
        furniture.putString("id", "ironagefurniture:chair_wood_ironage_classic_big_oak");
        furniture.putByte("Count", (byte) 3);
        ListNBT contents = new ListNBT(); contents.add(other); contents.add(furniture); chest.put("Items", contents);
        assertTrue(LegacyPaddedItemMigration.migrateChunkContents(chest));
        assertEquals(before, other);
        assertEquals("ironagefurniture:chair_wood_ironage_classic_dark_oak", furniture.getString("id"));
        assertFalse(LegacyPaddedItemMigration.migrateChunkContents(chest));
    }

    @Test public void ordinaryDarkOakFurnitureItemsAreRenamedBeforeFlattening() {
        for (String form : new String[] {"classic", "shield", "stool_short", "stool_tall", "bench_single", "bench_log_single", "bench_back_single"}) {
            CompoundNBT item = new CompoundNBT(), container = new CompoundNBT(), tag = new CompoundNBT();
            item.putString("id", "ironagefurniture:chair_wood_ironage_" + form + "_big_oak");
            item.putByte("Count", (byte) 3); tag.putString("Keep", "old furniture"); item.put("tag", tag);
            ListNBT items = new ListNBT(); items.add(item); container.put("Items", items);
            assertTrue(LegacyPaddedItemMigration.migrateChunkContents(container));
            assertEquals("ironagefurniture:chair_wood_ironage_" + form + "_dark_oak", item.getString("id"));
            assertEquals(3, item.getByte("Count")); assertEquals("old furniture", tag.getString("Keep"));
            assertFalse(LegacyPaddedItemMigration.migrateChunkContents(container));
        }
    }

    @Test public void everyLegacyBedAndChairPartKeepsItsFacing() {
        WoodenBed wooden = new WoodenBed("bed_wood_foot_left_oak", true);
        WoodenBed single = new WoodenBed("bed_wood_foot_oak", false);
        CanopyBed canopy = new CanopyBed("bed_canopy_foot_right_lower_oak", true);
        ThroneChair throne = new ThroneChair("chair_wood_ironage_throne_oak");
        for (int metadata = 0; metadata < 16; metadata++) {
            BlockState wood = LegacyWorldDataHook.legacyState(wooden, metadata);
            assertEquals(Direction.byHorizontalIndex(metadata & 3), wood.get(FurnitureBed.DIRECTION));
            assertEquals((metadata & 4) == 0 ? net.minecraft.state.properties.BedPart.FOOT
                    : net.minecraft.state.properties.BedPart.HEAD, wood.get(net.minecraft.state.properties.BlockStateProperties.BED_PART));
            assertEquals((metadata & 8) == 0 ? WoodBedSide.LEFT : WoodBedSide.RIGHT, wood.get(FurnitureBed.SIDE));
            assertEquals(WoodBedSide.LEFT, LegacyWorldDataHook.legacyState(single, metadata).get(FurnitureBed.SIDE));
            BlockState canopyState = LegacyWorldDataHook.legacyState(canopy, metadata);
            assertEquals(CanopyBedPart.values()[metadata >> 2], canopyState.get(CanopyBed.PART));
            assertEquals(WoodBedSide.RIGHT, canopyState.get(FurnitureBed.SIDE));
            assertEquals(Direction.byHorizontalIndex(metadata & 3), canopyState.get(FurnitureBed.DIRECTION));
            BlockState tall = LegacyWorldDataHook.legacyState(throne, metadata);
            assertEquals(metadata >> 2 == 2 ? ChairPart.UPPER : metadata >> 2 == 1 ? ChairPart.MIDDLE : ChairPart.LOWER,
                    tall.get(MultiBlockChair.PART));
        }
    }
    @Test public void allColourTilesBecomePaletteStatesIncludingBitWidthGrowth() {
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            CompoundNBT level = flattenedLevel(colour.getName());
            LegacyUpholsteryMigration.prepareTiles(level, false);
            assertEquals("minecraft:sign", level.getList("TileEntities", 10).getCompound(0).getString("id"));
            assertEquals(1, LegacyUpholsteryMigration.finishTiles(level));
            assertTrue(level.getList("TileEntities", 10).isEmpty());
            CompoundNBT section = level.getList("Sections", 10).getCompound(0);
            assertEquals(17, section.getList("Palette", 10).size());
            BitArray bits = new BitArray(5, 4096, section.getLongArray("BlockStates"));
            CompoundNBT migrated = section.getList("Palette", 10).getCompound(bits.getAt(0));
            assertEquals(colour.getName(), migrated.getCompound("Properties").getString("colour"));
            assertEquals("north", migrated.getCompound("Properties").getString("facing"));
            assertEquals("head_upper", migrated.getCompound("Properties").getString("part"));
            assertEquals("right", migrated.getCompound("Properties").getString("side"));
            assertEquals(0, bits.getAt(1));
            CompoundNBT before = level.copy();
            assertEquals(0, LegacyUpholsteryMigration.finishTiles(level));
            assertEquals(before, level);
        }
    }
    @Test public void nestedLegacyItemsKeepNbtAndPreferColourOverDamage() {
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            CompoundNBT item = new CompoundNBT();
            item.putString("id", "ironagefurniture:bed_wood_foot_big_oak");
            item.putByte("Count", (byte) 3);
            item.putShort("Damage", (short) colour.getItemMetadata());
            assertTrue(LegacyUpholsteryMigration.migrateItem(item));
            assertEquals(bedId(colour), item.getString("id"));
            assertFalse(item.getCompound("tag").contains("Color"));
            assertEquals(3, item.getByte("Count"));
            assertFalse(item.contains("Damage"));
            assertFalse(LegacyUpholsteryMigration.migrateItem(item));
            item.putShort("Damage", (short) 15);
            CompoundNBT tag = new CompoundNBT(); tag.putString("OtherData", "keep");
            tag.putString("Color", colour.getName()); item.put("tag", tag);
            CompoundNBT container = new CompoundNBT();
            ListNBT contents = new ListNBT(); contents.add(item); container.put("Items", contents);
            assertTrue(LegacyPaddedItemMigration.migrateChunkContents(container));
            assertEquals(bedId(colour), item.getString("id"));
            assertFalse(item.getCompound("tag").contains("Color"));
            assertEquals("keep", item.getCompound("tag").getString("OtherData"));
            assertFalse(LegacyPaddedItemMigration.migrateChunkContents(container));
        }
        CompoundNBT invalid = flattenedLevel("not_a_colour");
        LegacyUpholsteryMigration.prepareTiles(invalid, true);
        assertEquals("Sign", invalid.getList("TileEntities", 10).getCompound(0).getString("id"));
        assertEquals(1, LegacyUpholsteryMigration.finishTiles(invalid));
    }
    @Test public void legacyMetalTilesBecomeSavedPropertiesWithoutChangingLightState() {
        for (zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal metal
                : zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal.values()) {
            CompoundNBT level = flattenedLevel("red");
            CompoundNBT tile = level.getList("TileEntities", 10).getCompound(0);
            tile.putString("id", "ironagefurniture:sconce_metal"); tile.remove("Color");
            tile.putString("Metal", metal.getName());
            CompoundNBT state = level.getList("Sections", 10).getCompound(0).getList("Palette", 10).getCompound(0);
            state.putString("Name", "ironagefurniture:light_metal_ironage_sconce_wall_red_iron_thirteen");
            state.getCompound("Properties").remove("part"); state.getCompound("Properties").remove("side");
            state.getCompound("Properties").putString("waterlogged", "true");
            LegacyUpholsteryMigration.prepareTiles(level, true);
            assertEquals(1, LegacyUpholsteryMigration.finishTiles(level));
            CompoundNBT section = level.getList("Sections", 10).getCompound(0);
            BitArray bits = new BitArray(5, 4096, section.getLongArray("BlockStates"));
            CompoundNBT migrated = section.getList("Palette", 10).getCompound(bits.getAt(0));
            assertEquals(state.getString("Name"), migrated.getString("Name"));
            assertEquals(metal.getName(), migrated.getCompound("Properties").getString("metal"));
            assertEquals("north", migrated.getCompound("Properties").getString("facing"));
            assertEquals("true", migrated.getCompound("Properties").getString("waterlogged"));
            assertTrue(level.getList("TileEntities", 10).isEmpty());
            assertEquals(0, LegacyUpholsteryMigration.finishTiles(level));
        }
    }
    private static CompoundNBT flattenedLevel(String colour) {
        CompoundNBT level = new CompoundNBT(); level.putInt("xPos", -1); level.putInt("zPos", 1);
        CompoundNBT section = new CompoundNBT(); section.putByte("Y", (byte) 4);
        ListNBT palette = new ListNBT();
        CompoundNBT bed = new CompoundNBT(); bed.putString("Name", "ironagefurniture:bed_canopy_foot_right_lower_oak");
        CompoundNBT properties = new CompoundNBT(); properties.putString("facing", "north");
        properties.putString("part", "head_upper"); properties.putString("side", "right");
        bed.put("Properties", properties); palette.add(bed);
        for (int index = 1; index < 16; index++) {
            CompoundNBT other = new CompoundNBT(); other.putString("Name", "minecraft:stone"); other.putInt("TestIndex", index);
            palette.add(other);
        }
        section.put("Palette", palette); section.putLongArray("BlockStates", new BitArray(4, 4096).getBackingLongArray());
        ListNBT sections = new ListNBT(); sections.add(section); level.put("Sections", sections);
        CompoundNBT tile = new CompoundNBT(); tile.putString("id", "ironagefurniture:upholstery_colour");
        tile.putString("Color", colour); tile.putInt("x", -16); tile.putInt("y", 64); tile.putInt("z", 16);
        ListNBT tiles = new ListNBT(); tiles.add(tile); level.put("TileEntities", tiles);
        return level;
    }
    @Test public void playerBedsKeepColourWhenFlatteningMovesDamageIntoTheTag() {
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            CompoundNBT player = new CompoundNBT(), item = new CompoundNBT(), tag = new CompoundNBT();
            item.putString("id", "ironagefurniture:bed_wood_foot_big_oak"); item.putByte("Count", (byte) 1);
            tag.putInt("Damage", colour.getItemMetadata()); tag.putString("Keep", "bed"); item.put("tag", tag);
            ListNBT ender = new ListNBT(); ender.add(item); player.put("EnderItems", ender);
            LegacyWorldDataHook.preparePlayerData(player);
            assertEquals(bedId(colour), item.getString("id"));
            assertFalse(tag.contains("Color")); assertFalse(tag.contains("Damage"));
            assertEquals("bed", tag.getString("Keep"));
            assertFalse(LegacyPaddedItemMigration.migrateChunkContents(player));
        }
    }
    private static String bedId(UpholsteryColour colour) {
        return zone.moddev.mc.ironagefurniture.api.UpholsteryItemData.itemId(
                new net.minecraft.util.ResourceLocation("ironagefurniture:bed_wood_foot_dark_oak"), colour).toString();
    }
    @Test public void everyTagged114VariantFlattensOnceWithoutChangingOtherData() {
        for (String form : new String[] {"bed_wood_foot_", "bed_wood_foot_left_", "bed_canopy_foot_lower_",
                "bed_canopy_foot_left_lower_", "chair_wood_ironage_wingback_", "chair_wood_ironage_throne_"}) {
            for (UpholsteryColour colour : UpholsteryColour.values()) {
                CompoundNBT stack = new CompoundNBT(), tag = new CompoundNBT(), container = new CompoundNBT();
                String base = "ironagefurniture:" + form + "oak";
                stack.putString("id", base); stack.putByte("Count", (byte) 7);
                tag.putString("Color", colour.getName()); tag.putString("OtherModsData", "preserve");
                stack.put("tag", tag);
                ListNBT inner = new ListNBT(); inner.add(stack); container.put("NestedItems", inner);
                assertTrue(LegacyPaddedItemMigration.migrateChunkContents(container));
                String expected = base + (colour == UpholsteryColour.RED ? "" : "_" + colour.getName());
                assertEquals(expected, stack.getString("id")); assertEquals(7, stack.getByte("Count"));
                assertEquals("preserve", stack.getCompound("tag").getString("OtherModsData"));
                assertFalse(stack.getCompound("tag").contains("Color"));
                CompoundNBT once = container.copy();
                assertFalse(LegacyPaddedItemMigration.migrateChunkContents(container)); assertEquals(once, container);
            }
        }
        for (String invalid : new String[] {"", "not_a_colour"}) {
            CompoundNBT stack = new CompoundNBT(), tag = new CompoundNBT();
            stack.putString("id", "ironagefurniture:bed_wood_foot_oak"); stack.putByte("Count", (byte) 1);
            tag.putString("Color", invalid); tag.putInt("Damage", 15); stack.put("tag", tag);
            assertTrue(LegacyUpholsteryMigration.migrateItem(stack));
            assertEquals("ironagefurniture:bed_wood_foot_oak", stack.getString("id"));
            assertFalse(stack.contains("tag"));
            assertFalse(LegacyUpholsteryMigration.migrateItem(stack));
        }
    }
    @Test public void canonicalIdentityWinsOverObsoleteColourFieldsIncludingLightGray() {
        for (UpholsteryColour colour : UpholsteryColour.values()) if (colour != UpholsteryColour.RED) {
            CompoundNBT stack = new CompoundNBT(), tag = new CompoundNBT();
            stack.putString("id", bedId(colour)); stack.putByte("Count", (byte) 2);
            tag.putString("Color", "red"); stack.put("tag", tag); stack.putShort("Damage", (short) 15);
            assertTrue(LegacyUpholsteryMigration.migrateItem(stack));
            assertEquals(bedId(colour), stack.getString("id"));
            assertFalse(stack.contains("tag")); assertFalse(stack.contains("Damage"));
            assertFalse(LegacyUpholsteryMigration.migrateItem(stack));
        }
    }
}
