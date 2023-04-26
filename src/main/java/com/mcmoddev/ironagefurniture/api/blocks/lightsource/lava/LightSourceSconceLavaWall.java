package com.mcmoddev.ironagefurniture.api.blocks.lightsource.lava;

import com.mcmoddev.ironagefurniture.api.blocks.base.FurnitureBlock;
import com.mcmoddev.ironagefurniture.api.blocks.lightsource.glow.LightSourceSconceGlowWall;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import oshi.util.tuples.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class LightSourceSconceLavaWall extends LightSourceSconceGlowWall {
	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor levelAccessor, BlockPos pos, BlockPos pos2) {
		if (direction.getOpposite() == state.getValue(FurnitureBlock.DIRECTION) && !state.canSurvive(levelAccessor, pos)) {
			levelAccessor.destroyBlock(pos, true);
			return LightDrop().defaultBlockState().setValue(FurnitureBlock.WATERLOGGED, false);
		}

		return super.updateShape(state, direction, state2, levelAccessor, pos, pos2);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest,
									   FluidState fluid) {

		boolean isSilkTouch = false;

		ItemStack tool = player.getInventory().getSelected();

		if (tool != null) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(tool);

			if (enchantments != null && !enchantments.isEmpty())
				isSilkTouch = enchantments.get(Enchantments.SILK_TOUCH) > 0;
		}

		if (isSilkTouch && !player.isCreative())
			Block.popResource(level, pos, new ItemStack(LightDrop(), 1));

		boolean destroyed = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);

		if (!isSilkTouch && !player.isCreative()) {
			level.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, friction, explosionResistance);
			level.setBlock(pos, LightDrop().defaultBlockState().setValue(FurnitureBlock.WATERLOGGED, false), Block.UPDATE_ALL_IMMEDIATE, Block.UPDATE_ALL);
		}

		return destroyed;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> drops;
		drops = new ArrayList<>();

		Item item = EmptyVariant().asItem();
		ItemStack stack = new ItemStack(item, 1);

		drops.add(stack);

		return drops;
	}

	@Override
	protected Block LightDrop() {
		return BlockObjectHolder.light_metal_ironage_block_floor_lava_clear;
	}

	public LightSourceSconceLavaWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> 14));

		this.registerDefaultState(this.getStateDefinition().any().setValue(FurnitureBlock.DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rnd) {
		BlockPos blockpos = pos.above();

		if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
			if (rnd.nextInt(25) == 0) {
				Direction direction = state.getValue(FurnitureBlock.DIRECTION);

				Pair<Double, Double> rotated = FurnitureBlock.rotate(0.6D, 0.5D, state.getValue(FurnitureBlock.DIRECTION));

				double x = (double) pos.getX() + rotated.getA();
				double y = (double) pos.getY() + 0.5D;
				double z = (double) pos.getZ() + rotated.getB();

				Direction direction1 = direction.getOpposite();

				level.addParticle(ParticleTypes.LAVA, x + 0.27D * (double) direction1.getStepX(), y + 0.22D, z + 0.27D * (double) direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
				level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
			}

			if (rnd.nextInt(200) == 0)
				level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
		}

		super.animateTick(state, level, pos, rnd);
	}
}
