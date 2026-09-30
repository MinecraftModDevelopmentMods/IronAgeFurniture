package zone.moddev.mc.ironagefurniture.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Bootstrap;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.Blocks.MultiBlockWoodBed;
import zone.moddev.mc.ironagefurniture.api.Properties.UpholsteryColourProperty;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityUpholstery;

public class BedUpholsteryTest {
    @BeforeClass public static void bootstrap() { Bootstrap.register(); }

    @Test public void plainBedsHaveColourStorageWithoutChangingTheirSavedMetadata() {
        for (boolean doubleBed : new boolean[] { false, true }) {
            MultiBlockWoodBed bed = new MultiBlockWoodBed(Material.WOOD, "test_bed", 10, 3, doubleBed);
            for (int metadata = 0; metadata < (doubleBed ? 16 : 8); metadata++) {
                IBlockState state = bed.getStateFromMeta(metadata);
                assertEquals(metadata, bed.getMetaFromState(state));
                assertTrue(bed.hasTileEntity(state));
                assertTrue(bed.createTileEntity(null, state) instanceof TileEntityUpholstery);
                assertTrue(((IExtendedBlockState)state).getUnlistedNames()
                        .contains(UpholsteryColourProperty.COLOUR));
            }
        }
    }
}
