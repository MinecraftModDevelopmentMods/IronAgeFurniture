package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import oshi.util.tuples.Pair;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

public class LightSourceSconceLavaWall extends LightSourceSconceGlowWall {
	public LightSourceSconceLavaWall(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
	}
	
	@Override
	protected boolean HasFlame() {
		return false;
	}
	
	@Override
	protected boolean CanEx() {
		return false;
	}
	
	@Override
	protected Block LightDrop() {
		return BlockObjectHolder.light_metal_ironage_block_floor_lava_clear;
	}
	
	public LightSourceSconceLavaWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> {
		    return 14; }) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}
	
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rnd) {
	      BlockPos blockpos = pos.above();
	      if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
	         if (rnd.nextInt(25) == 0) {
	        	 Direction direction = state.getValue(DIRECTION);
	        	 
	        	 Pair<Double, Double> rotated = rotate(0.6D, 0.5D, state.getValue(DIRECTION));
	        	 
	        	 double x = (double)pos.getX() + rotated.getA();
	        	 double y = (double)pos.getY() + 0.5D;
	        	 double z = (double)pos.getZ() + rotated.getB();
	        	 
	        	 Direction direction1 = direction.getOpposite();
	        	 
	        	 level.addParticle(ParticleTypes.LAVA, x + 0.27D * (double)direction1.getStepX(), y + 0.22D, z + 0.27D * (double)direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
	        	 level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
	         }

	         if (rnd.nextInt(200) == 0) {
	        	 level.playLocalSound((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + rnd.nextFloat() * 0.2F, 0.9F + rnd.nextFloat() * 0.15F, false);
	         }
	      }
		
		super.animateTick(state, level, pos, rnd);
	}
}