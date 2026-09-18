package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BenchNether extends Bench {

	public BenchNether(float hardness, float blastResistance, SoundType sound, String name) {
		super(hardness, blastResistance, sound, name);
	}

	@Override
	public boolean isFlammable(BlockState state, IBlockReader level, BlockPos pos, Direction direction) {
		return false;
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader level, BlockPos pos, Direction direction) {
		return 0;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, IBlockReader level, BlockPos pos, Direction direction) {
		return 0;
	}
}
