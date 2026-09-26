package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.IdentityHashMap;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Bootstrap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import zone.moddev.mc.ironagefurniture.api.Blocks.Chair;

public class CfmChairMigrationTest {
    @BeforeClass
    public static void bootstrap() {
        Bootstrap.register();
    }

    @Test
    public void allSixWoodenChairsHaveLikeForLikeTargets() {
        Map<String, String> chairs = CfmChairMigration.chairMappings();
        assertEquals(6, chairs.size());
        assertEquals("chair_wood_ironage_classic_oak", chairs.get("cfm:chair_oak"));
        assertEquals("chair_wood_ironage_classic_spruce", chairs.get("cfm:chair_spruce"));
        assertEquals("chair_wood_ironage_classic_birch", chairs.get("cfm:chair_birch"));
        assertEquals("chair_wood_ironage_classic_jungle", chairs.get("cfm:chair_jungle"));
        assertEquals("chair_wood_ironage_classic_acacia", chairs.get("cfm:chair_acacia"));
        assertEquals("chair_wood_ironage_classic_big_oak", chairs.get("cfm:chair_dark_oak"));
        assertFalse(chairs.containsKey("cfm:chair_big_oak"));
        assertFalse(chairs.containsKey("cfm:chair_stone"));
    }

    @Test
    public void forcedConversionRequiresBothCfmAndTheOptIn() {
        assertFalse(CfmChairMigration.shouldForceConversion(false, false));
        assertFalse(CfmChairMigration.shouldForceConversion(false, true));
        assertFalse(CfmChairMigration.shouldForceConversion(true, false));
        assertTrue(CfmChairMigration.shouldForceConversion(true, true));
    }

    @Test
    public void chairRotationSurvivesChunkConversion() {
        Chair source = new Chair(Material.WOOD, "source", 1.0f, 1.0f);
        Chair target = new Chair(Material.WOOD, "target", 1.0f, 1.0f);
        ExtendedBlockStorage section = new ExtendedBlockStorage(0, true);
        Map<Block, Block> replacements = new IdentityHashMap<Block, Block>();
        replacements.put(source, target);
        for (int facing = 0; facing < 4; ++facing) {
            section.set(facing, 0, 0, source.getStateFromMeta(facing));
        }

        assertEquals(4, CfmChairMigration.migrateSectionBlocks(section, replacements));
        assertEquals(0, CfmChairMigration.migrateSectionBlocks(section, replacements));
        for (int facing = 0; facing < 4; ++facing) {
            assertEquals(target, section.get(facing, 0, 0).getBlock());
            assertEquals(facing, target.getMetaFromState(section.get(facing, 0, 0)));
        }
    }

    @Test
    public void missingModMigrationReadsOldNumericIdsBeforeForgeReusesThem() {
        Chair target = new Chair(Material.WOOD, "target", 1.0f, 1.0f);
        Chunk chunk = new Chunk(null, 0, 0);
        chunk.getBlockStorageArray()[0] = new ExtendedBlockStorage(0, true);
        NBTTagCompound raw = new NBTTagCompound();
        NBTTagCompound level = new NBTTagCompound();
        NBTTagList sections = new NBTTagList();
        NBTTagCompound section = new NBTTagCompound();
        section.setByte("Y", (byte)0);
        byte[] blocks = new byte[4096];
        byte[] add = new byte[2048];
        byte[] data = new byte[2048];
        for (int facing = 0; facing < 4; ++facing) {
            blocks[facing] = (byte)0x2c;
            add[facing >> 1] |= (byte)(1 << ((facing & 1) * 4));
            data[facing >> 1] |= (byte)(facing << ((facing & 1) * 4));
        }
        section.setByteArray("Blocks", blocks);
        section.setByteArray("Add", add);
        section.setByteArray("Data", data);
        sections.appendTag(section);
        level.setTag("Sections", sections);
        raw.setTag("Level", level);
        Map<Integer, Block> targets = new HashMap<Integer, Block>();
        targets.put(300, target);

        assertEquals(4, CfmChairMigration.migrateMissingChairBlocks(chunk, raw, targets));
        for (int facing = 0; facing < 4; ++facing) {
            assertEquals(target, chunk.getBlockStorageArray()[0].get(facing, 0, 0).getBlock());
            assertEquals(facing, target.getMetaFromState(
                    chunk.getBlockStorageArray()[0].get(facing, 0, 0)));
        }
    }

    @Test
    public void oldNumericIdsSurviveUntilUnopenedChunksAreMigrated() {
        CfmChairMigrationData original = new CfmChairMigrationData(CfmChairMigrationData.NAME);
        Map<Integer, String> ids = new HashMap<Integer, String>();
        ids.put(233, "chair_wood_ironage_classic_oak");
        ids.put(301, "chair_wood_ironage_classic_big_oak");
        original.setBlockIds(ids);
        CfmChairMigrationData reloaded = new CfmChairMigrationData(CfmChairMigrationData.NAME);
        reloaded.readFromNBT(original.writeToNBT(new NBTTagCompound()));
        assertEquals(ids, reloaded.getBlockIds());
    }

    @Test
    public void nestedChairStacksKeepCountAndTags() {
        NBTTagCompound root = new NBTTagCompound();
        NBTTagList items = new NBTTagList();
        NBTTagCompound chair = new NBTTagCompound();
        chair.setString("id", "cfm:chair_dark_oak");
        chair.setByte("Count", (byte) 3);
        chair.setShort("Damage", (short) 2);
        NBTTagCompound custom = new NBTTagCompound();
        custom.setString("display", "keep me");
        chair.setTag("tag", custom);
        items.appendTag(chair);
        root.setTag("Items", items);
        Map<String, String> ids = new java.util.HashMap<String, String>();
        ids.put("cfm:chair_dark_oak", "ironagefurniture:chair_wood_ironage_classic_big_oak");

        assertEquals(1, CfmChairMigration.rewriteItemStacks(root, ids));
        assertEquals("ironagefurniture:chair_wood_ironage_classic_big_oak", chair.getString("id"));
        assertEquals(3, chair.getByte("Count"));
        assertEquals(0, chair.getShort("Damage"));
        assertEquals("keep me", chair.getCompoundTag("tag").getString("display"));
        assertEquals(0, CfmChairMigration.rewriteItemStacks(root, ids));
    }
}
