package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.torch;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

/** Unlit torches use the same server-side interaction and power rules as lit ones. */
public class LightSourceSconceTorchWallUnlit extends LightSourceSconceTorchWall {
    public LightSourceSconceTorchWallUnlit(float hardness, float resistance, SoundType sound, String name) {
        super(Properties.of().strength(hardness, resistance).sound(sound));
    }
    @Override protected boolean isLit() { return false; }
    @Override public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) { }
}
