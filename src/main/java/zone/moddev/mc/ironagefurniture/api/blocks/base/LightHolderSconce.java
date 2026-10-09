package zone.moddev.mc.ironagefurniture.api.blocks.base;

import net.minecraft.util.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.ToolType;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;

public abstract class LightHolderSconce extends LightHolder {
    @Override protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(SconceMetalData.METAL);
    }
    @Override public float getBlockHardness(BlockState state, IBlockReader world, BlockPos pos) {
        return SconceMetalData.get(state).hardness(super.getBlockHardness(state, world, pos));
    }
    @Override public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, Entity exploder, Explosion explosion) {
        return SconceMetalData.get(state).resistance(super.getExplosionResistance(state, world, pos, exploder, explosion));
    }
    @Override public int getHarvestLevel(BlockState state) { return SconceMetalData.get(state).harvestLevel(); }
    @Override public ToolType getHarvestTool(BlockState state) { return ToolType.PICKAXE; }
    @Override public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) {
        // A pickaxe speeds up mining, but hands and other tools must not destroy the frame or its contents.
        return true;
    }
    @Override public ItemStack getPickBlock(BlockState state, RayTraceResult hit, IBlockReader world, BlockPos pos, PlayerEntity player) {
        // Interaction-only states may have no item of their own. Pick the
        // craftable frame, carrying the metal rather than a hidden state ID.
        return SconceMetalData.create(zone.moddev.mc.ironagefurniture.BlockObjectHolder
                .light_metal_ironage_sconce_floor_empty_iron, SconceMetalData.get(state));
    }
	public LightHolderSconce(Properties properties) {
		super(properties);

		this.setDefaultState(this.getDefaultState().with(DIRECTION, Direction.NORTH)
                .with(WATERLOGGED, false).with(SconceMetalData.METAL,
                        zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal.IRON));
        this.generateShapes(this.getStateContainer().getValidStates());
	}
}
