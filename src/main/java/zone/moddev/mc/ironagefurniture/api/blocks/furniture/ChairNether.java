package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import net.minecraft.block.Block;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;

public class ChairNether extends Chair implements IWaterLoggable {
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

	public ChairNether(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
	}

	public ChairNether(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.WOOD).strength(hardness, blastResistance).sound(sound));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}
}
