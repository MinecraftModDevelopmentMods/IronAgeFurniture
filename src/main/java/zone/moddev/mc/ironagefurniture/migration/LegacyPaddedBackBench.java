package zone.moddev.mc.ironagefurniture.migration;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.BackBench;

/** Hidden compatibility state for pre-flattening padded back benches. */
public final class LegacyPaddedBackBench extends BackBench implements LegacyPaddedFurnitureBlock {
	private final String legacyPath;

	public LegacyPaddedBackBench(String legacyPath) {
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
