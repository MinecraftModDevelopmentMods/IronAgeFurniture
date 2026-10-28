package zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;
import zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock;
import zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp;
import zone.moddev.mc.ironagefurniture.init.ModVanillaLights;

/** One owner for a vial across retrieval, mining, power, water and support loss. */
public final class LavaSconceBehaviour {
    public static boolean silk(Level level, ItemStack tool) {
        return EnchantmentHelper.getItemEnchantmentLevel(level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(Enchantments.SILK_TOUCH), tool) > 0;
    }
    public static List<ItemStack> drops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(SconceMetalData.frame(state));
        var tool = params.getOptionalParameter(LootContextParams.TOOL);
        if (tool != null && silk(params.getLevel(), tool))
            drops.add(new ItemStack(ModVanillaLights.light_metal_ironage_block_floor_lava_clear.get()));
        return drops;
    }
    public static CompoundTag snapshot(Level level, BlockPos pos) {
        var storage = level.getBlockEntity(pos);
        return storage == null ? null : storage.saveWithoutMetadata(level.registryAccess());
    }
    private static void restoreData(Level level, BlockPos pos, CompoundTag data) {
        if (data != null && level.getBlockEntity(pos) != null) {
            var storage = level.getBlockEntity(pos);
            storage.loadWithComponents(data.copy(), level.registryAccess());
            storage.setChanged();
            var state = level.getBlockState(pos);
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
        }
    }
    public static void restore(Level level, BlockPos pos, BlockState state, CompoundTag data) {
        level.setBlockAndUpdate(pos, state);
        restoreData(level, pos, data);
    }
    public static boolean release(Level level, BlockPos pos, BlockState state, boolean wall, boolean preserve) {
        double x = pos.getX() + .5, z = pos.getZ() + .5;
        if (wall) {
            Direction outward = state.getValue(FurnitureBlock.DIRECTION);
            x -= .25 * outward.getStepX();
            z -= .25 * outward.getStepZ();
        }
        return level.addFreshEntity(new ReleasedLavaLamp(level, x, pos.getY() + .3125, z, pos.getY(), preserve));
    }
    public static void shatter(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1, 1);
        level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
    }
    public static boolean water(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid, boolean wall) {
        if (state.getValue(FurnitureBlock.WATERLOGGED) || !fluid.is(Fluids.WATER)) return false;
        if (!level.isClientSide()) {
            CompoundTag data = level instanceof Level world ? snapshot(world, pos) : null;
            if (!level.setBlock(pos, SconceMetalData.empty(state, wall).setValue(FurnitureBlock.WATERLOGGED, true), Block.UPDATE_ALL)) return false;
            if (level instanceof Level world) restoreData(world, pos, data);
            level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1, 1);
            level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, .5F, 2.6F);
            if (level instanceof ServerLevel server) Block.popResource(server, pos, new ItemStack(ModVanillaLights.obsidian_chunk.get()));
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return true;
    }
    public static void releasePowered(BlockState state, ServerLevel level, BlockPos pos, boolean wall) {
        if (!level.hasNeighborSignal(pos) || !level.isEmptyBlock(pos.below()) || !state.canSurvive(level, pos)) return;
        CompoundTag data = snapshot(level, pos);
        if (!level.setBlockAndUpdate(pos, SconceMetalData.empty(state, wall))) return;
        restoreData(level, pos, data);
        // Redstone has no player cause; an unrelated nearby Creative player is not authority to preserve it.
        if (!release(level, pos, state, wall, false)) restore(level, pos, state, data);
    }
    private LavaSconceBehaviour() { }
    /** Retain the target's existing bounded vial pop/ambient effects. */
    public static void animate(BlockState state, Level level, BlockPos pos, net.minecraft.util.RandomSource random) {
        BlockPos above = pos.above();
        if (!level.getBlockState(above).isAir() || level.getBlockState(above).isSolidRender(level, above)) return;
        if (random.nextInt(25) == 0) {
            Direction outward = state.getValue(FurnitureBlock.DIRECTION).getOpposite();
            var rotated = FurnitureBlock.rotate(.6, .5, state.getValue(FurnitureBlock.DIRECTION));
            double x = pos.getX() + rotated.getA(), y = pos.getY() + .5, z = pos.getZ() + rotated.getB();
            level.addParticle(net.minecraft.core.particles.ParticleTypes.LAVA, x + .27 * outward.getStepX(), y + .22,
                    z + .27 * outward.getStepZ(), 0, 0, 0);
            level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundSource.BLOCKS,
                    .2F + random.nextFloat() * .2F, .9F + random.nextFloat() * .15F, false);
        }
        if (random.nextInt(200) == 0)
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS,
                    .2F + random.nextFloat() * .2F, .9F + random.nextFloat() * .15F, false);
    }
}
