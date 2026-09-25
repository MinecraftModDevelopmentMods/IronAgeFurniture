package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.Bench;

/** Hidden compatibility state for pre-flattening padded benches. */
public final class LegacyPaddedBench extends Bench implements EntityBlock, LegacyPaddedFurnitureBlock {
	private final String legacyPath;

	public LegacyPaddedBench(String legacyPath) {
		super(1, 10, SoundType.WOOD, legacyPath);
		this.legacyPath = legacyPath;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LegacyPaddedBenchBlockEntity(pos, state);
	}

	@Override
	public String getLegacyPath() {
		return legacyPath;
	}
}
