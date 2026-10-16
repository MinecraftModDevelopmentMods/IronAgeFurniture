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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootParams;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowFloor;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.LavaSconceBehaviour;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;
import zone.moddev.mc.ironagefurniture.init.RegistrationProperties;

public class LightSourceSconceLavaFloor extends LightSourceSconceGlowFloor {
    public LightSourceSconceLavaFloor(float hardness, float resistance, SoundType sound, String name) {
        super(RegistrationProperties.block(Properties.of().strength(hardness, resistance).sound(sound).noOcclusion().lightLevel(state -> 14), name));
    }
    @Override protected Block LightDrop() { return ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get(); }
    @Override protected boolean ShouldDrop() { return false; }
    @Override public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) { return LavaSconceBehaviour.drops(state, params); }
    @Override public void onPlace(BlockState state, Level level, BlockPos pos, BlockState previous, boolean moving) {
        super.onPlace(state, level, pos, previous, moving);
        if (!level.isClientSide()) level.scheduleTick(pos, this, 2);
    }
    @Override protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block changed, Orientation orientation, boolean moving) {
        if (!level.isClientSide()) level.scheduleTick(pos, this, 2);
    }
    @Override public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        LavaSconceBehaviour.releasePowered(state, level, pos, false);
    }
    @Override protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
            Direction direction, BlockPos neighborPos, BlockState neighbor, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            if (level instanceof ServerLevel server && !LavaSconceBehaviour.release(server, pos, state, false, false)) return state;
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighbor, random);
    }
    @Override public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
            ItemStack tool, boolean willHarvest, FluidState fluid) {
        boolean destroyed = super.onDestroyedByPlayer(state, level, pos, player, tool, willHarvest, fluid);
        if (destroyed && !level.isClientSide() && !player.isCreative() && !LavaSconceBehaviour.silk(level, tool)) {
            LavaSconceBehaviour.shatter(level, pos);
        }
        return destroyed;
    }
    @Override public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
        return LavaSconceBehaviour.water(level, pos, state, fluid, false);
    }
}
