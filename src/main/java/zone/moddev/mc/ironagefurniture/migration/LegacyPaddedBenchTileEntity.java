package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

/** Reads the stable colour field used by the 1.10 and 1.12 padded-bench formats. */
public final class LegacyPaddedBenchTileEntity extends TileEntity {
	private String colour = "red";

	public LegacyPaddedBenchTileEntity() {
		super(LegacyPaddedMigrationRegistry.padded_bench_colour);
	}

	@Override
	public void load(CompoundNBT compound) {
		super.load(compound);
		colour = LegacyPaddedBenchIds.normalizeColour(compound.getString(LegacyPaddedBenchIds.COLOR_TAG));
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
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
