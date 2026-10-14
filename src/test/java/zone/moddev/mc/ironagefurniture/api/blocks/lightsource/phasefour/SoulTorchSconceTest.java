package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.util.registry.Bootstrap;
import org.junit.Test;
import static org.junit.Assert.*;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.soultorch.LightSourceSconceSoulTorchFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.soultorch.LightSourceSconceSoulTorchWall;

public class SoulTorchSconceTest {
    @Test public void soulTorchesStartDryAndIronWithTheirNativeLightLevel() {
        Bootstrap.bootStrap();
        for (Block block : new Block[]{
                new LightSourceSconceSoulTorchFloor(1, 10, SoundType.METAL, "soul_floor_test"),
                new LightSourceSconceSoulTorchWall(1, 10, SoundType.METAL, "soul_wall_test")}) {
            assertFalse(block.defaultBlockState().getValue(Candle.WATERLOGGED));
            assertEquals(SconceMetal.IRON, SconceMetalData.get(block.defaultBlockState()));
            assertEquals(12, block.defaultBlockState().getLightEmission());
        }
    }
}
