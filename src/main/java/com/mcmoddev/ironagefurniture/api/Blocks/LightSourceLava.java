package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;

public class LightSourceLava extends LightSourceGlowdust{

	public LightSourceLava(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
	}
	
	@Override
	public void onLand(Level level, BlockPos pos, BlockState state, BlockState state2,
			FallingBlockEntity fallingEntity) {
		
		level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, friction, explosionResistance);
		level.setBlock(pos, Blocks.FIRE.defaultBlockState(), UPDATE_ALL_IMMEDIATE, UPDATE_ALL);
	}
	
	public LightSourceLava(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> {
		    return 15; }) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rnd) {
	      BlockPos blockpos = pos.above();
	      if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
	         if (rnd.nextInt(100) == 0) {
	
	            double x = (double)pos.getX() + 0.5;
	            double y = (double)pos.getY() + 0.25D;
	            double z = (double)pos.getZ() + 0.5;
	            
	            level.addParticle(ParticleTypes.LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
	            level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
	         }

	         if (rnd.nextInt(200) == 0) {
	        	 level.playLocalSound((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
	         }
	      }
		
		super.animateTick(state, level, pos, rnd);
	}
	
	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest,
			FluidState fluid) {
		
		boolean isSilkTouch = false;

		ItemStack tool = player.getHandSlots().iterator().next();
		
		if (tool != null) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(tool);
			
			if (enchantments != null && !enchantments.isEmpty())
				isSilkTouch =  enchantments.get(Enchantments.SILK_TOUCH) > 0;
		}
		
		if (isSilkTouch && !player.isCreative())
			Block.popResource(level, pos, new ItemStack(state.getBlock().asItem(), 1));
		
		boolean destroyed = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
		
		if (!isSilkTouch && !player.isCreative()) {
			level.playSound(player, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, friction, explosionResistance);
			level.setBlock(pos, Blocks.FIRE.defaultBlockState(), UPDATE_ALL_IMMEDIATE, UPDATE_ALL);
		}
		
		return destroyed;
	}
	
	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return false;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		// drops handled elsewhere
		List<ItemStack> drops;
		drops = new ArrayList<ItemStack>();
		
		return drops;
	}
}