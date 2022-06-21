package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext.Builder;

public class LightSourceLava extends LightSourceGlowdust {

	public LightSourceLava(Properties properties) {
		super(properties);
		
		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH).setValue(WATERLOGGED, false));
        this.generateShapes(this.getStateDefinition().getPossibleStates());
        //this.flameParticle = ParticleTypes.FLAME;
	}
	
	public LightSourceLava(float hardness, float blastResistance, SoundType sound, String name) {
		super(Block.Properties.of(Material.METAL).strength(hardness, blastResistance).sound(sound).lightLevel((p_50886_) -> {
		    return 15; }) );

		this.registerDefaultState(this.getStateDefinition().any().setValue(DIRECTION, Direction.NORTH));
		this.generateShapes(this.getStateDefinition().getPossibleStates());
		this.setRegistryName(name);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest,
			FluidState fluid) {
		boolean isSilkTouch = false;
		
		ItemStack hand = player.getHandSlots().iterator().next();
		
		if (hand.isEnchanted())
			for (net.minecraft.nbt.Tag tag : hand.getEnchantmentTags()) {
				if (tag.getAsString().contains("silk_touch"))
					isSilkTouch = true;
			}

		if (isSilkTouch && !player.isCreative()) {
	    	Item item = state.getBlock().asItem();
	    	ItemStack stack = new ItemStack(item, 1); 
			Block.popResource(level, pos, stack);
		}	
		
		boolean destroyed = super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
		
		if (!isSilkTouch)
			level.setBlock(pos, Blocks.FIRE.defaultBlockState(), UPDATE_ALL_IMMEDIATE, UPDATE_ALL);

		
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