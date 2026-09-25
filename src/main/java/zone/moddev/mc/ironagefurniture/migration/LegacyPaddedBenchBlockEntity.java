package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** Reads the stable colour field used by the 1.10 and 1.12 padded-bench formats. */
public final class LegacyPaddedBenchBlockEntity extends BlockEntity {
	private String colour = "red";

	public LegacyPaddedBenchBlockEntity(BlockPos pos, BlockState state) {
		super(LegacyPaddedMigrationRegistry.padded_bench_colour, pos, state);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		colour = LegacyPaddedBenchIds.normalizeColour(compound.getString(LegacyPaddedBenchIds.COLOR_TAG));
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		compound.putString(LegacyPaddedBenchIds.COLOR_TAG, colour);
		return compound;
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if (level != null && !level.isClientSide) {
			LegacyMigrationEvents.queueBench(level, worldPosition, colour);
		}
	}
}
