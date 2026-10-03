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
            assertEquals("ironagefurniture:bed_wood_foot_dark_oak", item.getString("id"));
            assertEquals(colour.getName(), item.getCompound("tag").getString("Color"));
            assertEquals(3, item.getByte("Count"));
            assertFalse(item.contains("Damage"));
            assertFalse(LegacyUpholsteryMigration.migrateItem(item));
            item.putShort("Damage", (short) 15);
            item.getCompound("tag").putString("OtherData", "keep");
            CompoundNBT container = new CompoundNBT();
            ListNBT contents = new ListNBT(); contents.add(item); container.put("Items", contents);
            assertTrue(LegacyPaddedItemMigration.migrateChunkContents(container));
            assertEquals(colour.getName(), item.getCompound("tag").getString("Color"));
            assertEquals("keep", item.getCompound("tag").getString("OtherData"));
            assertFalse(LegacyPaddedItemMigration.migrateChunkContents(container));
        }
        CompoundNBT invalid = flattenedLevel("not_a_colour");
        LegacyUpholsteryMigration.prepareTiles(invalid, true);
        assertEquals("Sign", invalid.getList("TileEntities", 10).getCompound(0).getString("id"));
        assertEquals(1, LegacyUpholsteryMigration.finishTiles(invalid));
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
}
