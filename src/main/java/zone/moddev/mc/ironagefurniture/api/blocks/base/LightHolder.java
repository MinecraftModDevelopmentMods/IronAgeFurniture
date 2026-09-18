package zone.moddev.mc.ironagefurniture.api.blocks.base;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockRayTraceResult;

public abstract class LightHolder extends FurnitureBlock
{
	public LightHolder(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
	}

    protected abstract ActionResultType ActivateSconce(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult);

	@Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
    {
        return ActivateSconce(state, world, pos, player, hand, rayTraceResult);
    }
}
