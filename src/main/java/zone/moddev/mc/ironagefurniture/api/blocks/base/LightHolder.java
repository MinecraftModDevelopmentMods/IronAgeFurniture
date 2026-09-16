package zone.moddev.mc.ironagefurniture.api.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class LightHolder extends FurnitureBlock
{
	public LightHolder(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
	}

    protected abstract InteractionResult ActivateSconce(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult);

	@Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        return ActivateSconce(state, world, pos, player, InteractionHand.MAIN_HAND, hit);
    }

	@Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos,
                                               Player player, InteractionHand hand, BlockHitResult hit) {
        return ActivateSconce(state, world, pos, player, hand, hit);
    }
}
