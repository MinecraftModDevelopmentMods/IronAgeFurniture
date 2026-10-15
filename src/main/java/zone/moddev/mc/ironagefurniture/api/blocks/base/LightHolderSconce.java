package zone.moddev.mc.ironagefurniture.api.blocks.base;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Explosion;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;

public abstract class LightHolderSconce extends LightHolder {
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SconceMetalData.METAL);
    }
    public float getDestroySpeed(BlockState state, BlockGetter world, BlockPos pos) {
        return SconceMetalData.get(state).hardness(state.getDestroySpeed(world, pos));
    }
    @Override public float getDestroyProgress(BlockState state, Player player, BlockGetter world, BlockPos pos) {
        // The game caches state hardness, so mining must apply the frame's metal here.
        float hardness = getDestroySpeed(state, world, pos);
        return hardness < 0 ? 0 : player.getDigSpeed(state, pos) / hardness / 30F;
    }
    @Override public float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
        return SconceMetalData.get(state).resistance(super.getExplosionResistance(state, world, pos, explosion));
    }
    @Override public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        // A pickaxe speeds up mining, but hands and other tools must not destroy the frame or its contents.
        return true;
    }
    @Override public ItemStack getPickBlock(BlockState state, HitResult hit, BlockGetter world, BlockPos pos, Player player) {
        // Interaction-only states may have no item of their own. Pick the
        // craftable frame, carrying the metal rather than a hidden state ID.
        return SconceMetalData.create(zone.moddev.mc.ironagefurniture.BlockObjectHolder
                .light_metal_ironage_sconce_floor_empty_iron, SconceMetalData.get(state));
    }
	public LightHolderSconce(Properties properties) {
		super(properties);

		this.registerDefaultState(this.defaultBlockState().setValue(DIRECTION, Direction.NORTH)
                .setValue(WATERLOGGED, false).setValue(SconceMetalData.METAL,
                        zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal.IRON));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
	}
}
