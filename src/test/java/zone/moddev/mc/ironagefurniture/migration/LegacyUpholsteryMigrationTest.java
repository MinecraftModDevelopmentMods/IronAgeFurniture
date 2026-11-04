package zone.moddev.mc.ironagefurniture.migration;

import static org.junit.Assert.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.BitStorage;
import net.minecraft.core.Direction;
import net.minecraft.server.Bootstrap;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.*;
import zone.moddev.mc.ironagefurniture.api.enumerations.*;

public class LegacyUpholsteryMigrationTest {
    @BeforeClass public static void bootstrap() { net.minecraft.SharedConstants.tryDetectVersion(); Bootstrap.bootStrap(); zone.moddev.mc.ironagefurniture.NativeFurnitureFixtures.register(); }

    @Test public void unrelatedLegacyItemNamesDoNotBreakFurnitureRecovery() {
        CompoundTag chest = new CompoundTag(), other = new CompoundTag(), furniture = new CompoundTag();
        other.putString("id", "mca:ToyTrain"); other.putByte("Count", (byte) 2);
        CompoundTag cargo = new CompoundTag(); cargo.putString("Name", "Keep this unrelated data"); other.put("tag", cargo);
        CompoundTag before = other.copy();
        furniture.putString("id", "ironagefurniture:chair_wood_ironage_classic_big_oak");
        furniture.putByte("Count", (byte) 3);
        ListTag contents = new ListTag(); contents.add(other); contents.add(furniture); chest.put("Items", contents);
        assertTrue(LegacyPaddedItemMigration.migrateChunkContents(chest));
        assertEquals(before, other);
        assertEquals("ironagefurniture:chair_wood_ironage_classic_dark_oak", furniture.getString("id"));
        assertFalse(LegacyPaddedItemMigration.migrateChunkContents(chest));
    }

    @Test public void ordinaryDarkOakFurnitureItemsAreRenamedBeforeFlattening() {
        for (String form : new String[] {"classic", "shield", "stool_short", "stool_tall", "bench_single", "bench_log_single", "bench_back_single"}) {
            CompoundTag item = new CompoundTag(), container = new CompoundTag(), tag = new CompoundTag();
            item.putString("id", "ironagefurniture:chair_wood_ironage_" + form + "_big_oak");
            item.putByte("Count", (byte) 3); tag.putString("Keep", "old furniture"); item.put("tag", tag);
            ListTag items = new ListTag(); items.add(item); container.put("Items", items);
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
            BlockState wood = LegacyWorldDataHook.legacyState(new net.minecraft.resources.ResourceLocation("ironagefurniture:bed_wood_foot_left_oak"), wooden, metadata);
            assertEquals(Direction.from2DDataValue(metadata & 3).getOpposite(), wood.getValue(FurnitureBed.DIRECTION));
            assertEquals((metadata & 4) == 0 ? net.minecraft.world.level.block.state.properties.BedPart.FOOT
                    : net.minecraft.world.level.block.state.properties.BedPart.HEAD, wood.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.BED_PART));
            assertEquals((metadata & 8) == 0 ? WoodBedSide.LEFT : WoodBedSide.RIGHT, wood.getValue(FurnitureBed.SIDE));
            assertEquals(WoodBedSide.LEFT, LegacyWorldDataHook.legacyState(new net.minecraft.resources.ResourceLocation("ironagefurniture:bed_wood_foot_oak"), single, metadata).getValue(FurnitureBed.SIDE));
            BlockState canopyState = LegacyWorldDataHook.legacyState(new net.minecraft.resources.ResourceLocation("ironagefurniture:bed_canopy_foot_right_lower_oak"), canopy, metadata);
            assertEquals((metadata & 4) != 0, canopyState.getValue(FurnitureBed.UPPER));
            assertEquals((metadata & 8) == 0 ? net.minecraft.world.level.block.state.properties.BedPart.FOOT : net.minecraft.world.level.block.state.properties.BedPart.HEAD, canopyState.getValue(FurnitureBed.PART));
            assertEquals(WoodBedSide.RIGHT, canopyState.getValue(FurnitureBed.SIDE));
            assertEquals(Direction.from2DDataValue(metadata & 3).getOpposite(), canopyState.getValue(FurnitureBed.DIRECTION));
            BlockState tall = LegacyWorldDataHook.legacyState(new net.minecraft.resources.ResourceLocation("ironagefurniture:chair_wood_ironage_throne_oak"), throne, metadata);
            assertEquals(metadata >> 2 == 2 ? ChairPart.UPPER : metadata >> 2 == 1 ? ChairPart.MIDDLE : ChairPart.LOWER,
                    tall.getValue(MultiBlockChair.PART));
        }
    }
    @Test public void allColourTilesBecomePaletteStatesIncludingBitWidthGrowth() {
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            CompoundTag level = flattenedLevel(colour.getSerializedName());
            LegacyUpholsteryMigration.prepareTiles(level, false);
            assertEquals("minecraft:sign", level.getList("TileEntities", 10).getCompound(0).getString("id"));
            assertEquals(1, LegacyUpholsteryMigration.finishTiles(level));
            assertTrue(level.getList("TileEntities", 10).isEmpty());
            CompoundTag section = level.getList("Sections", 10).getCompound(0);
            assertEquals(17, section.getList("Palette", 10).size());
            BitStorage bits = new BitStorage(5, 4096, section.getLongArray("BlockStates"));
            CompoundTag migrated = section.getList("Palette", 10).getCompound(bits.get(0));
            assertEquals(colour.getSerializedName(), migrated.getCompound("Properties").getString("colour"));
            assertEquals("north", migrated.getCompound("Properties").getString("facing"));
            assertEquals("head_upper", migrated.getCompound("Properties").getString("part"));
            assertEquals("right", migrated.getCompound("Properties").getString("side"));
            assertEquals(0, bits.get(1));
            CompoundTag before = level.copy();
            assertEquals(0, LegacyUpholsteryMigration.finishTiles(level));
            assertEquals(before, level);
        }
    }
    @Test public void nestedLegacyItemsKeepNbtAndPreferColourOverDamage() {
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            CompoundTag item = new CompoundTag();
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
            CompoundTag tag = new CompoundTag(); tag.putString("OtherData", "keep");
            tag.putString("Color", colour.getSerializedName()); item.put("tag", tag);
            CompoundTag container = new CompoundTag();
            ListTag contents = new ListTag(); contents.add(item); container.put("Items", contents);
            assertTrue(LegacyPaddedItemMigration.migrateChunkContents(container));
            assertEquals(bedId(colour), item.getString("id"));
            assertFalse(item.getCompound("tag").contains("Color"));
            assertEquals("keep", item.getCompound("tag").getString("OtherData"));
            assertFalse(LegacyPaddedItemMigration.migrateChunkContents(container));
        }
        CompoundTag invalid = flattenedLevel("not_a_colour");
        LegacyUpholsteryMigration.prepareTiles(invalid, true);
        assertEquals("Sign", invalid.getList("TileEntities", 10).getCompound(0).getString("id"));
        assertEquals(1, LegacyUpholsteryMigration.finishTiles(invalid));
    }
    @Test public void legacyMetalTilesBecomeSavedPropertiesWithoutChangingLightState() {
        for (zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal metal
                : zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal.values()) {
            CompoundTag level = flattenedLevel("red");
            CompoundTag tile = level.getList("TileEntities", 10).getCompound(0);
            tile.putString("id", "ironagefurniture:sconce_metal"); tile.remove("Color");
            tile.putString("Metal", metal.getSerializedName());
            CompoundTag state = level.getList("Sections", 10).getCompound(0).getList("Palette", 10).getCompound(0);
            state.putString("Name", "ironagefurniture:light_metal_ironage_sconce_wall_red_iron_thirteen");
            state.getCompound("Properties").remove("part"); state.getCompound("Properties").remove("side");
            state.getCompound("Properties").putString("waterlogged", "true");
            LegacyUpholsteryMigration.prepareTiles(level, true);
            assertEquals(1, LegacyUpholsteryMigration.finishTiles(level));
            CompoundTag section = level.getList("Sections", 10).getCompound(0);
            BitStorage bits = new BitStorage(5, 4096, section.getLongArray("BlockStates"));
            CompoundTag migrated = section.getList("Palette", 10).getCompound(bits.get(0));
            assertEquals(state.getString("Name"), migrated.getString("Name"));
            assertEquals(metal.getSerializedName(), migrated.getCompound("Properties").getString("metal"));
            assertEquals("north", migrated.getCompound("Properties").getString("facing"));
            assertEquals("true", migrated.getCompound("Properties").getString("waterlogged"));
            assertTrue(level.getList("TileEntities", 10).isEmpty());
            assertEquals(0, LegacyUpholsteryMigration.finishTiles(level));
        }
    }
    private static CompoundTag flattenedLevel(String colour) {
        CompoundTag level = new CompoundTag(); level.putInt("xPos", -1); level.putInt("zPos", 1);
        CompoundTag section = new CompoundTag(); section.putByte("Y", (byte) 4);
        ListTag palette = new ListTag();
        CompoundTag bed = new CompoundTag(); bed.putString("Name", "ironagefurniture:bed_canopy_foot_right_lower_oak");
        CompoundTag properties = new CompoundTag(); properties.putString("facing", "north");
        properties.putString("part", "head_upper"); properties.putString("side", "right");
        bed.put("Properties", properties); palette.add(bed);
        for (int index = 1; index < 16; index++) {
            CompoundTag other = new CompoundTag(); other.putString("Name", "minecraft:stone"); other.putInt("TestIndex", index);
            palette.add(other);
        }
        section.put("Palette", palette); section.putLongArray("BlockStates", new BitStorage(4, 4096).getRaw());
        ListTag sections = new ListTag(); sections.add(section); level.put("Sections", sections);
        CompoundTag tile = new CompoundTag(); tile.putString("id", "ironagefurniture:upholstery_colour");
        tile.putString("Color", colour); tile.putInt("x", -16); tile.putInt("y", 64); tile.putInt("z", 16);
        ListTag tiles = new ListTag(); tiles.add(tile); level.put("TileEntities", tiles);
        return level;
    }
    @Test public void playerBedsKeepColourWhenFlatteningMovesDamageIntoTheTag() {
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            CompoundTag player = new CompoundTag(), item = new CompoundTag(), tag = new CompoundTag();
            item.putString("id", "ironagefurniture:bed_wood_foot_big_oak"); item.putByte("Count", (byte) 1);
            tag.putInt("Damage", colour.getItemMetadata()); tag.putString("Keep", "bed"); item.put("tag", tag);
            ListTag ender = new ListTag(); ender.add(item); player.put("EnderItems", ender);
            LegacyWorldDataHook.preparePlayerData(player);
            assertEquals(bedId(colour), item.getString("id"));
            assertFalse(item.getCompound("tag").contains("Color")); assertFalse(item.getCompound("tag").contains("Damage"));
            assertEquals("bed", tag.getString("Keep"));
            assertFalse(LegacyPaddedItemMigration.migrateChunkContents(player));
        }
    }
    private static String bedId(UpholsteryColour colour) {
        return zone.moddev.mc.ironagefurniture.api.UpholsteryItemData.itemId(
                new net.minecraft.resources.ResourceLocation("ironagefurniture:bed_wood_foot_dark_oak"), colour).toString();
    }
    @Test public void everyTagged114VariantFlattensOnceWithoutChangingOtherData() {
        for (String form : new String[] {"bed_wood_foot_", "bed_wood_foot_left_", "bed_canopy_foot_lower_",
                "bed_canopy_foot_left_lower_", "chair_wood_ironage_wingback_", "chair_wood_ironage_throne_"}) {
            for (UpholsteryColour colour : UpholsteryColour.values()) {
                CompoundTag stack = new CompoundTag(), tag = new CompoundTag(), container = new CompoundTag();
                String base = "ironagefurniture:" + form + "oak";
                stack.putString("id", base); stack.putByte("Count", (byte) 7);
                tag.putString("Color", colour.getSerializedName()); tag.putString("OtherModsData", "preserve");
                stack.put("tag", tag);
                ListTag inner = new ListTag(); inner.add(stack); container.put("NestedItems", inner);
                assertTrue(LegacyPaddedItemMigration.migrateChunkContents(container));
                String expected = base + (colour == UpholsteryColour.RED ? "" : "_" + colour.getSerializedName());
                assertEquals(expected, stack.getString("id")); assertEquals(7, stack.getByte("Count"));
                assertEquals("preserve", stack.getCompound("tag").getString("OtherModsData"));
                assertFalse(stack.getCompound("tag").contains("Color"));
                CompoundTag once = container.copy();
                assertFalse(LegacyPaddedItemMigration.migrateChunkContents(container)); assertEquals(once, container);
            }
        }
        for (String invalid : new String[] {"", "not_a_colour"}) {
            CompoundTag stack = new CompoundTag(), tag = new CompoundTag();
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
            CompoundTag stack = new CompoundTag(), tag = new CompoundTag();
            stack.putString("id", bedId(colour)); stack.putByte("Count", (byte) 2);
            tag.putString("Color", "red"); stack.put("tag", tag); stack.putShort("Damage", (short) 15);
            assertTrue(LegacyUpholsteryMigration.migrateItem(stack));
            assertEquals(bedId(colour), stack.getString("id"));
            assertFalse(stack.contains("tag")); assertFalse(stack.contains("Damage"));
            assertFalse(LegacyUpholsteryMigration.migrateItem(stack));
        }
    }
}
