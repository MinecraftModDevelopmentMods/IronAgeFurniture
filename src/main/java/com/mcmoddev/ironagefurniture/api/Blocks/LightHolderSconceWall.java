package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.api.Enumerations.Rotation;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LightHolderSconceWall extends LightHolderSconceFloor {
    // A single AABB for your wall sconce’s holder piece
	// the little sconce-holder, EAST-facing by default:
	private static final AxisAlignedBB HOLDER_EAST = new AxisAlignedBB(
	    1.5/16.0, 9/16.0,  5.5/16.0,
	    6.5/16.0, 10/16.0, 10.5/16.0
	);
	// now rotate for the other three horizontal facings:
	private static final AxisAlignedBB HOLDER_NORTH = RotateBB(Rotation.Ninty,    HOLDER_EAST);
	private static final AxisAlignedBB HOLDER_SOUTH = RotateBB(Rotation.OneEighty, HOLDER_EAST);
	private static final AxisAlignedBB HOLDER_WEST  = RotateBB(Rotation.TwoSeventy, HOLDER_EAST);

    public LightHolderSconceWall(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
        // default facing north
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override public boolean isFullCube(IBlockState bs)   { return false; }
    @Override public boolean isOpaqueCube(IBlockState bs) { return false; }

    // Drop the floor item when broken
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList(
            new ItemStack(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron)
        );
    }

    // Pop off if the wall behind is gone
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn) {
        EnumFacing facing = state.getValue(FACING);
        BlockPos behind = pos.offset(facing.getOpposite());
        if (!world.getBlockState(behind).isSideSolid(world, behind, facing)) {
            world.setBlockToAir(pos);
        }
        super.neighborChanged(state, world, pos, blockIn);
    }

    // Bounding box & collision
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    	switch (state.getValue(FACING)) {
	        case NORTH: return HOLDER_NORTH;
	        case SOUTH: return HOLDER_SOUTH;
	        case WEST:  return HOLDER_WEST;
	        default:    return HOLDER_EAST;
    	}
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos,
            AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
    	switch (state.getValue(FACING)) {
	        case NORTH:
	            super.addCollisionBoxToList(pos, entityBox, collidingBoxes, HOLDER_NORTH);
	            break;
	        case SOUTH:
	            super.addCollisionBoxToList(pos, entityBox, collidingBoxes, HOLDER_SOUTH);
	            break;
	        case WEST:
	            super.addCollisionBoxToList(pos, entityBox, collidingBoxes, HOLDER_WEST);
	            break;
	        default: // EAST
	            super.addCollisionBoxToList(pos, entityBox, collidingBoxes, HOLDER_EAST);
	            break;
	    }
    }

    // Variant getters for ActivateSconce in the base class
    @Override protected Block GetGlowVariant()     { return BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron; }
    @Override protected Block GetTorchVariant()    { return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron; }
    @Override protected Block GetLavaVariant()     { return BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron; }
    @Override protected Block GetUnlitTorchVariant() { return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_unlit; }
    @Override protected Block GetRedTorchVariant(){ return BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron; }
    @Override protected Block GetRedVariant()      { return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron; }
    //@Override protected Block GetSoulTorchVariant(){ return BlockObjectHolder.light_metal_ironage_sconce_wall_soultorch_iron; }
}