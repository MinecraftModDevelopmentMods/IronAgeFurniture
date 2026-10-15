package zone.moddev.mc.ironagefurniture.api.blocks.base;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;

public abstract class LightHolderSconce extends LightHolder implements net.minecraft.world.level.block.EntityBlock {
    @Override public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(net.minecraft.core.BlockPos pos, BlockState state) {
        return new zone.moddev.mc.ironagefurniture.api.tile.MetalSconceBlockEntity(pos, state);
    }
    @Override public float getExplosionResistance(BlockState state, net.minecraft.world.level.BlockGetter level,
            net.minecraft.core.BlockPos pos, net.minecraft.world.level.Explosion explosion) {
        return SconceMetalData.get(state).resistance(super.getExplosionResistance(state, level, pos, explosion));
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SconceMetalData.METAL);
    }

    @Override protected float getDestroyProgress(BlockState state, net.minecraft.world.entity.player.Player player,
            net.minecraft.world.level.BlockGetter world, net.minecraft.core.BlockPos pos) {
        return super.getDestroyProgress(state, player, world, pos) / SconceMetalData.get(state).hardness(1);
    }
	public LightHolderSconce(Properties properties) {
		super(properties);

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
	}
}
