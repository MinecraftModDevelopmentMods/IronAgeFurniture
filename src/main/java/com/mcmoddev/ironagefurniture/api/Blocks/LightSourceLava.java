package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.GravelBlock;
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
	
//	private void PlaceFireBelow(LevelAccessor levelAccessor, BlockPos pos) {
//		int offset = 1;
//		
//		while (levelAccessor.getBlockState(pos.below(offset)).isAir() && offset < 256) {
//			offset++;
//		}
//		
//		if (offset < 256) {
//			levelAccessor.setBlock(pos.below(offset-1), Blocks.FIRE.defaultBlockState(), UPDATE_ALL_IMMEDIATE, UPDATE_ALL);
//		}
//	}
	
//	public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor levelAccessor, BlockPos pos, BlockPos pos2)
//	{
//		if (direction == Direction.DOWN && !this.canSurvive(state, levelAccessor, pos)) {
//			levelAccessor.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, friction, explosionResistance);
//			
//			PlaceFireBelow(levelAccessor, pos);
//			
//			return Blocks.AIR.defaultBlockState();
//		}
//				 
//		return super.updateShape(state, direction, state2, levelAccessor, pos, pos2);
//	}
	
//	public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos)
//	{
//		return canSupportCenter(levelReader, pos.below(), Direction.UP);
//	}
}