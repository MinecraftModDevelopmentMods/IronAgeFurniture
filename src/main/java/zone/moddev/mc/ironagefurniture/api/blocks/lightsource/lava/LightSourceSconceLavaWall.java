package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.lava;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowWall;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.LavaSconceBehaviour;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

public class LightSourceSconceLavaWall extends LightSourceSconceGlowWall {
    public LightSourceSconceLavaWall(float hardness, float resistance, SoundType sound, String name) {
        super(Properties.of().strength(hardness, resistance).sound(sound).noOcclusion().lightLevel(state -> 14));
    }
    @Override protected Block LightDrop() { return ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get(); }
    @Override protected boolean ShouldDrop() { return false; }
    @Override public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) { return LavaSconceBehaviour.drops(state, params); }
    @Override public void onPlace(BlockState state, Level level, BlockPos pos, BlockState previous, boolean moving) {
        super.onPlace(state, level, pos, previous, moving);
        if (!level.isClientSide()) level.scheduleTick(pos, this, 2);
    }
    @Override public void neighborChanged(BlockState state, Level level, BlockPos pos, Block changed, BlockPos changedPos, boolean moving) {
        if (!level.isClientSide()) level.scheduleTick(pos, this, 2);
    }
    @Override public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        LavaSconceBehaviour.releasePowered(state, level, pos, true);
    }
    @Override public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            if (level instanceof ServerLevel server && !LavaSconceBehaviour.release(server, pos, state, true,
                    zone.moddev.mc.ironagefurniture.api.CreativeModeBreakTracker.isCreativeSupportBreak(server,
                            pos.relative(state.getValue(DIRECTION).getOpposite())))) return state;
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }
    @Override public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
            boolean willHarvest, FluidState fluid) {
        ItemStack tool = player.getMainHandItem();
        boolean silk = LavaSconceBehaviour.silk(level, tool);
        var data = LavaSconceBehaviour.snapshot(level, pos);
        boolean destroyed = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
        if (destroyed && !level.isClientSide() && !player.isCreative() && !silk) {
            if (!LavaSconceBehaviour.release(level, pos, state, true, false)) {
                LavaSconceBehaviour.restore(level, pos, state, data);
                return false;
            }
        }
        return destroyed;
    }
    @Override public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
        return LavaSconceBehaviour.water(level, pos, state, fluid, true);
    }
    @Override public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        LavaSconceBehaviour.animate(state, level, pos, random);
    }
}
