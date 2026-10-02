package zone.moddev.mc.ironagefurniture.api.Blocks;

import static org.junit.Assert.assertEquals;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.EnumFacing;
import org.junit.BeforeClass;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.Enumerations.CanopyBedPart;
import zone.moddev.mc.ironagefurniture.api.Enumerations.WoodBedPart;
import zone.moddev.mc.ironagefurniture.api.Enumerations.WoodBedSide;

public class MultiBlockBedStateTest {
    @BeforeClass
    public static void bootstrapMinecraft() {
        Bootstrap.register();
    }

    @Test
    public void canopyBedPreservesEveryPartAndFacing() {
        MultiBlockBed bed = new MultiBlockBed(Material.WOOD, "test_canopy", 10, 3,
                MultiBlockBed.SINGLE_SIDE);
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            for (CanopyBedPart part : CanopyBedPart.values()) {
                IBlockState state = bed.getDefaultState()
                        .withProperty(MultiBlockBed.FACING, facing)
                        .withProperty(MultiBlockBed.PART, part);
                assertEquals(state, bed.getStateFromMeta(bed.getMetaFromState(state)));
            }
        }
    }

    @Test
    public void woodBedsPreserveFacingPartAndDoubleBedSide() {
        for (boolean doubled : new boolean[] {false, true}) {
            MultiBlockWoodBed bed = new MultiBlockWoodBed(Material.WOOD, "test_wood", 10, 3, doubled);
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                for (WoodBedPart part : WoodBedPart.values()) {
                    for (WoodBedSide side : WoodBedSide.values()) {
                        if (!doubled && side == WoodBedSide.RIGHT) continue;
                        IBlockState state = bed.getDefaultState()
                                .withProperty(MultiBlockWoodBed.FACING, facing)
                                .withProperty(MultiBlockWoodBed.PART, part)
                                .withProperty(MultiBlockWoodBed.SIDE, side);
                        assertEquals(state, bed.getStateFromMeta(bed.getMetaFromState(state)));
                    }
                }
            }
        }
    }
}
