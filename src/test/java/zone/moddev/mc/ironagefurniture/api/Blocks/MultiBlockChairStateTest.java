package zone.moddev.mc.ironagefurniture.api.Blocks;

import static org.junit.Assert.assertEquals;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.EnumFacing;
import org.junit.Test;
import org.junit.BeforeClass;
import zone.moddev.mc.ironagefurniture.api.Enumerations.ChairPart;

public class MultiBlockChairStateTest {
    @BeforeClass
    public static void bootstrapMinecraft() {
        Bootstrap.register();
    }

    @Test
    public void wingbackAndThronePreservePartAndFacingInMetadata() {
        MultiBlockChair[] forms = {
                new WingbackChair(Material.WOOD, "wingback_test", 10, 1),
                new ThroneChair(Material.WOOD, "throne_test", 10, 1)
        };
        for (MultiBlockChair form : forms) {
            for (ChairPart part : ChairPart.values()) {
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    IBlockState state = form.getDefaultState()
                            .withProperty(MultiBlockChair.FACING, facing)
                            .withProperty(MultiBlockChair.PART, part);
                    assertEquals(state, form.getStateFromMeta(form.getMetaFromState(state)));
                }
            }
        }
    }
}
