package zone.moddev.mc.ironagefurniture.api.blocks.furniture;

import zone.moddev.mc.ironagefurniture.api.enumerations.BenchType;
import zone.moddev.mc.ironagefurniture.api.properties.BenchTypeProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class BackBenchNether extends BackBench {
	public static final net.minecraft.world.level.block.state.properties.EnumProperty<BenchType> TYPE = BenchTypeProperty.create("type", BenchType.SINGLE, BenchType.LEFT, BenchType.MIDDLE, BenchType.RIGHT);

	public BackBenchNether(float hardness, float blastResistance, SoundType sound, String name) {
		super(zone.moddev.mc.ironagefurniture.init.RegistrationProperties.block(Block.Properties.of().strength(hardness, blastResistance).sound(sound), name));

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
	}

	@Override
	public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return false;
	}
}
