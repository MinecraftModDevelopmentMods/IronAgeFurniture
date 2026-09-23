package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.Bench;

/** Hidden compatibility state for pre-flattening padded benches. */
public final class LegacyPaddedBench extends Bench implements LegacyPaddedFurnitureBlock {
	private final String legacyPath;

	public LegacyPaddedBench(String legacyPath) {
		super(1, 10, SoundType.WOOD, legacyPath);
		this.legacyPath = legacyPath;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new LegacyPaddedBenchTileEntity();
	}

	@Override
	public String getLegacyPath() {
		return legacyPath;
	}
}
