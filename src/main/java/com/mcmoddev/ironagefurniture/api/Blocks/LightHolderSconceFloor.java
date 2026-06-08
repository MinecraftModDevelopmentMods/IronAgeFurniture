package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.Enumerations.Rotation;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper.MetalVariant;
import com.mcmoddev.ironagefurniture.api.MineralogyCompat;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityMetalVariant;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LightHolderSconceFloor extends BlockHBase {
	// ---- the combined overall bounds of the sconce ----
	private static final AxisAlignedBB BB = new AxisAlignedBB(
	    5.5/16.0, 0.0,    5.5/16.0, 
	    10.5/16.0, 9/16.0, 10.5/16.0
	);

	// ---- the top holder piece, facing EAST by default ----
	private static final AxisAlignedBB HOLDER_EAST = new AxisAlignedBB(
	    5.5/16.0, 8/16.0,  5.5/16.0,
	    10.5/16.0, 9/16.0, 10.5/16.0
	);
	
	// ---- the vertical stand piece, facing EAST by default ----
	private static final AxisAlignedBB STAND_EAST = new AxisAlignedBB(
	    7.5/16.0, 0.0,    5.5/16.0,
	    8.5/16.0, 8/16.0, 8.5/16.0
	);

	// rotate them for the other facings:
	private static final AxisAlignedBB HOLDER_NORTH = RotateBB(Rotation.Ninty,    HOLDER_EAST);
	private static final AxisAlignedBB HOLDER_SOUTH = RotateBB(Rotation.OneEighty, HOLDER_EAST);
	private static final AxisAlignedBB HOLDER_WEST  = RotateBB(Rotation.TwoSeventy, HOLDER_EAST);

	private static final AxisAlignedBB STAND_NORTH = RotateBB(Rotation.Ninty,    STAND_EAST);
	private static final AxisAlignedBB STAND_SOUTH = RotateBB(Rotation.OneEighty, STAND_EAST);
	private static final AxisAlignedBB STAND_WEST  = RotateBB(Rotation.TwoSeventy, STAND_EAST);

	public LightHolderSconceFloor(Material materialIn, String name, float resistance, float hardness) {
		super(materialIn);
		InitSconce(materialIn, name, resistance, hardness);
	}

	private void InitSconce(Material materialIn, String name, float resistance, float hardness) {
		this.setSoundType(SoundType.METAL);
        this.setHarvestLevel("pickaxe", 1);
        this.blockResistance = resistance;
        this.blockHardness   = hardness;
        this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
        	.withProperty(MetalVariantHelper.METAL, MetalVariant.IRON));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) 
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { FACING, MetalVariantHelper.METAL });
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return MetalVariantHelper.withMetal(state, worldIn, pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityMetalVariant();
	}

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
		return MetalVariantHelper.getHardness(worldIn, pos, this.blockHardness);
	}
	
	private boolean canPlaceOn(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.isSideSolid(worldIn, pos, EnumFacing.UP))
        {
            return true;
        }
        else
        {
            return state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (EnumFacing enumfacing : FACING.getAllowedValues())
        {
            if (this.canPlaceAt(worldIn, pos, enumfacing))
            {
                return true;
            }
        }

        if (this.canPlaceAt(worldIn, pos, EnumFacing.UP)) 
        {
            return true;
        }
        
        return false;
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing)
    {
    	IBlockState stateAbove = worldIn.getBlockState(pos.up());
    
    	if(stateAbove.getBlock().isBlockSolid(worldIn, pos.up(), facing))
    		return true;
    
        BlockPos blockpos = pos.offset(facing.getOpposite());
        boolean flag = facing.getAxis().isHorizontal();
      
        return flag && worldIn.isSideSolid(blockpos, facing, true) || facing.equals(EnumFacing.UP) && this.canPlaceOn(worldIn, blockpos);
    }
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side,
	        float hitX, float hitY, float hitZ, int meta,
	        EntityLivingBase placer, ItemStack stack) {


	    if (side.getAxis().isHorizontal()) {
	        Block wall = GetWallVariant();
	        EnumFacing attachFace = side; 
	        return wall.getDefaultState().withProperty(FACING, attachFace);
	    }

	    if (this.canPlaceOn(world, pos.down())) {
	        EnumFacing playerFacing = placer.getHorizontalFacing();
	        return this.getDefaultState().withProperty(FACING, playerFacing);
	    }

	    return null;
	}


    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if (!this.canPlaceOn(worldIn, pos.down())) {
            if (!worldIn.isRemote) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
            worldIn.setBlockToAir(pos);
        }
        super.neighborChanged(state, worldIn, pos, blockIn);
    }

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		return BB;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn) {

		  switch (state.getValue(FACING)) {
		    case NORTH:
		      super.addCollisionBoxToList(pos, entityBox, collidingBoxes, STAND_NORTH);
		      super.addCollisionBoxToList(pos, entityBox, collidingBoxes, HOLDER_NORTH);
		      break;
		    case SOUTH:
		      super.addCollisionBoxToList(pos, entityBox, collidingBoxes, STAND_SOUTH);
		      super.addCollisionBoxToList(pos, entityBox, collidingBoxes, HOLDER_SOUTH);
		      break;
		    case WEST:
		      super.addCollisionBoxToList(pos, entityBox, collidingBoxes, STAND_WEST);
		      super.addCollisionBoxToList(pos, entityBox, collidingBoxes, HOLDER_WEST);
		      break;
		    default: // EAST
		      super.addCollisionBoxToList(pos, entityBox, collidingBoxes, STAND_EAST);
		      super.addCollisionBoxToList(pos, entityBox, collidingBoxes, HOLDER_EAST);
		      break;
		  }
	}

	@Override
	public boolean isFullCube(IBlockState bs) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState bs) {
		return false;
	}
	
	 private boolean ActivateSconce(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		// guard null/empty
		if (heldItem == null || heldItem.stackSize <= 0) {
			return false;
		}
		
		Block newBlock = null;
		
		// the sconce is empty, so we can place a light source in it
		
		// regular torch
		if (heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
			newBlock = GetTorchVariant();
		}
		else if (heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_glow_clear)) {
			newBlock = GetGlowVariant(); // glow lamp
		}
		else if (heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear)) {
			newBlock = GetLavaVariant(); 
		}
		else if (heldItem.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)) {
			newBlock = GetRedTorchVariant(); // redstone torch
		}
		else if (heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_candle_floor)) {
			newBlock = GetCandleVariant();
		}
		else if (heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_red_clear)) {
			newBlock = GetRedVariant(); // redstone lamp
		}
		else if (MineralogyCompat.isRockSaltLampItem(heldItem)) {
			newBlock = GetRockSaltVariant();
		}
		
		// if nothing matched, we did not handle it
		if (newBlock == null) {
			return false;
		}
		
		// replace the block, preserve facing
		MetalVariantHelper.replaceBlockPreservingMetal(worldIn, pos,
			newBlock.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3 /* UPDATE_ALL */);
		
		// consume one
		if (!playerIn.capabilities.isCreativeMode) {
			heldItem.stackSize--;
		}
		
		return true;
	}

	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
	        EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

	    if (heldItem == null || heldItem.stackSize <= 0) {
	        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	    }

	    Block newBlock = null;

	    // Torch
	    if (heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
	        newBlock = GetTorchVariant();
	    }
	    // Glow lamp
	    else if (heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_glow_clear)) {
	        newBlock = GetGlowVariant();
	    }
	    // Lava lamp
	    else if (heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear)) {
	        newBlock = GetLavaVariant();
	    }
	    // Redstone torch
	    else if (heldItem.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)) {
	        newBlock = GetRedTorchVariant();
	    }
	    else if (heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_candle_floor)) {
	        newBlock = GetCandleVariant();
	    }
//	    // Soul torch // leaving as a placeholder for now for the candle variant
//	    else if (heldItem.getItem() == Item.getItemFromBlock(Blocks.SOUL_TORCH)) {
//	        newBlock = GetSoulTorchVariant();
//	    }
	    else if (heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_red_clear)) {
	        newBlock = GetRedVariant(); // Redstone lamp
	    }
	    else if (MineralogyCompat.isRockSaltLampItem(heldItem)) {
	        newBlock = GetRockSaltVariant();
	    }

	    if (newBlock != null) {
	        MetalVariantHelper.replaceBlockPreservingMetal(worldIn, pos,
	        	newBlock.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3 /* UPDATE_ALL */);

	        if (!playerIn.capabilities.isCreativeMode) {
	            heldItem.stackSize--;
	        }
	        
	        return true;
	    }

	    return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}
	 
	 @Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		 return Lists.newArrayList(MetalVariantHelper.getDrop(this, world, pos));
	}
 
    protected Block GetWallVariant()		{ return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron; }
    protected Block GetGlowVariant()		{ return BlockObjectHolder.light_metal_ironage_sconce_floor_glow_iron; }
    protected Block GetTorchVariant()		{ return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron; }
    protected Block GetLavaVariant()		{ return BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron; }
    protected Block GetRedTorchVariant()	{ return BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron; }
    protected Block GetCandleVariant()	    { return BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron; }
    protected Block GetSoulTorchVariant()	{ return BlockObjectHolder.light_metal_ironage_sconce_floor_soultorch_iron; }
    protected Block GetRedVariant()   		{ return BlockObjectHolder.light_metal_ironage_sconce_floor_red_iron; }
    protected Block GetRockSaltVariant()	{ return BlockObjectHolder.light_metal_ironage_sconce_floor_rocksalt_iron; }
    protected Block GetSoulVariant()  		{ return BlockObjectHolder.light_metal_ironage_sconce_floor_soultorch_iron; }
    protected Block GetUnlitTorchVariant() 	{ return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_unlit; }
}
