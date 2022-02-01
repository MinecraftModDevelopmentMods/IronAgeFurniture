package com.mcmoddev.ironagefurniture.api.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;

import java.util.ArrayList;
import java.util.List;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;

public class LightSourceSconceTorchWall extends LightSourceSconceTorchFloor {
	public LightSourceSconceTorchWall(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
        this.flameParticle = ParticleTypes.FLAME;
	}
	
	public LightSourceSconceTorchWall(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> {
		    return 14; }) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
		this.flameParticle = ParticleTypes.FLAME;
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos)
	{
	      Direction direction = state.getValue(DIRECTION);
	      BlockPos blockpos = pos.relative(direction.getOpposite());
	      BlockState blockstate = levelReader.getBlockState(blockpos);
	      return blockstate.isFaceSturdy(levelReader, blockpos, direction);
	}
	
	@Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
    	List<ItemStack> drops;
    	drops = new ArrayList<ItemStack>();
    	
    	Item item = BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron.asItem();
    	ItemStack stack = new ItemStack(item, 1); 
    	
    	Item item2 = Blocks.TORCH.asItem();
    	ItemStack stack2 = new ItemStack(item2, 1); 
    	
    	drops.add(stack);
    	drops.add(stack2);
    	
    	return drops;
    }
}