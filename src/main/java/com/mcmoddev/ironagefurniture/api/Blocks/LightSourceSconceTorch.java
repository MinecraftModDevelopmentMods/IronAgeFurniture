/*package com.mcmoddev.ironagefurniture.api.Blocks;

import com.mcmoddev.ironagefurniture.api.Enumerations.LightType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public abstract class LightSourceSconceTorch extends LightSourceSconce {
	boolean _lit;
	
	protected abstract IBlockState getUnlitVariant(IBlockState initialState);
	protected abstract IBlockState getLitVariant(IBlockState initialState);
	
	
	public LightSourceSconceTorch(Material materialIn, String name, float resistance, float hardness, boolean lit) {
		super(materialIn, name, resistance, hardness);
		
		_lit = lit;
	}

	@Override
	protected LightType getLightType() {
		return LightType.TORCH;
	}
	
	@Override
	protected boolean ShowFlame() {		
		return _lit;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (heldItem == null && _lit) {
			heldItem = new ItemStack(Blocks.TORCH, 1);
			playerIn.setHeldItem(hand, heldItem);
			IBlockState newState = getHolderBlockState(state);
			worldIn.setBlockState(pos, newState);
			worldIn.checkLight(pos); 
			return true;
		}
		
		if (heldItem.getItem() == Items.FLINT_AND_STEEL && !_lit) {
			heldItem.setItemDamage(heldItem.getItemDamage()+1);
			
			IBlockState newState = getLitVariant(state);
			worldIn.setBlockState(pos, newState);
			worldIn.checkLight(pos); 
			return true;
		}
		
		if (heldItem.getItem() == Items.WATER_BUCKET && _lit) {
			
			IBlockState newState = getUnlitVariant(state);
			worldIn.setBlockState(pos, newState);
			worldIn.checkLight(pos); 
			return true;
		}
		
		if (heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {	
			IBlockState newState = getHolderBlockState(state);
			worldIn.setBlockState(pos, newState);
			worldIn.checkLight(pos);
			heldItem.stackSize = heldItem.stackSize + 1;
			return true;
		}
	
		return true;
	}
}*/