package zone.moddev.mc.ironagefurniture.api.blocks.base;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockRayTraceResult;

public abstract class LightHolder extends FurnitureBlock
{
	public LightHolder(Properties properties) {
		super(properties);

		this.setDefaultState(this.getStateContainer().getBaseState() .with(DIRECTION, Direction.NORTH) .with(WATERLOGGED, false));
        this.generateShapes(this.getStateContainer().getValidStates());
	}

    protected abstract boolean ActivateSconce(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult);

	@Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
    {
        return ActivateSconce(state, world, pos, player, hand, rayTraceResult);
    }
}
