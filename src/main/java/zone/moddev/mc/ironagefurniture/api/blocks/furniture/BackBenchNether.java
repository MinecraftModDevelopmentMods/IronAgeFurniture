package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import zone.moddev.mc.ironagefurniture.api.enumerations.BenchType;
import zone.moddev.mc.ironagefurniture.api.properties.BenchTypeProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;

public class BackBenchNether extends BackBench {
	public static final BenchTypeProperty TYPE = BenchTypeProperty.create("type", BenchType.SINGLE, BenchType.LEFT, BenchType.MIDDLE, BenchType.RIGHT);

	public BackBenchNether(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.NETHER_WOOD).strength(hardness, blastResistance).sound(sound));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
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
