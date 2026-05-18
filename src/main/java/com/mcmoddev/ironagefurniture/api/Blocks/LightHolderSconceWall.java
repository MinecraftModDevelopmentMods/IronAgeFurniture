package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LightHolderSconceWall extends LightHolderSconceFloor {
    // Small physical holder bounds, matching the intended wall sconce footprint.
    private static final AxisAlignedBB SHAPE_EAST = new AxisAlignedBB(
        1.5 / 16.0, 9.0 / 16.0, 5.5 / 16.0,
        6.5 / 16.0, 10.0 / 16.0, 10.5 / 16.0
    );

    // Slightly larger click target so right-clicks still land on the sconce reliably.
    private static final AxisAlignedBB PICK_EAST = new AxisAlignedBB(
         0.0 / 16.0, 2.0 / 16.0, 5.5 / 16.0,
         6.5 / 16.0, 11.0 / 16.0, 10.5 / 16.0
    );

    private static final AxisAlignedBB SHAPE_NORTH = rotateCounterClockwise(SHAPE_EAST);
    private static final AxisAlignedBB SHAPE_SOUTH = rotateClockwise(SHAPE_EAST);
    private static final AxisAlignedBB SHAPE_WEST = rotateHalfTurn(SHAPE_EAST);

    private static final AxisAlignedBB PICK_NORTH = rotateCounterClockwise(PICK_EAST);
    private static final AxisAlignedBB PICK_SOUTH = rotateClockwise(PICK_EAST);
    private static final AxisAlignedBB PICK_WEST = rotateHalfTurn(PICK_EAST);

    public LightHolderSconceWall(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    private static AxisAlignedBB rotateClockwise(AxisAlignedBB bb) {
        return new AxisAlignedBB(
            1.0D - bb.maxZ,
            bb.minY,
            bb.minX,
            1.0D - bb.minZ,
            bb.maxY,
            bb.maxX
        );
    }

    private static AxisAlignedBB rotateHalfTurn(AxisAlignedBB bb) {
        return new AxisAlignedBB(
            1.0D - bb.maxX,
            bb.minY,
            1.0D - bb.maxZ,
            1.0D - bb.minX,
            bb.maxY,
            1.0D - bb.minZ
        );
    }

    private static AxisAlignedBB rotateCounterClockwise(AxisAlignedBB bb) {
        return new AxisAlignedBB(
            bb.minZ,
            bb.minY,
            1.0D - bb.maxX,
            bb.maxZ,
            bb.maxY,
            1.0D - bb.minX
        );
    }

    @Override
    public boolean isFullCube(IBlockState bs) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState bs) {
        return false;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList(new ItemStack(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron));
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn) {
        EnumFacing facing = state.getValue(FACING);
        BlockPos behind = pos.offset(facing.getOpposite());
        if (!world.getBlockState(behind).isSideSolid(world, behind, facing)) {
            if (!world.isRemote) {
                this.dropBlockAsItem(world, pos, state, 0);
            }
            world.setBlockToAir(pos);
        }
    }

    private AxisAlignedBB getShape(IBlockState state) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            default:
                return SHAPE_EAST;
        }
    }

    private AxisAlignedBB getPickShape(IBlockState state) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return PICK_NORTH;
            case SOUTH:
                return PICK_SOUTH;
            case WEST:
                return PICK_WEST;
            default:
                return PICK_EAST;
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return getShape(state);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos,
            AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
        super.addCollisionBoxToList(pos, entityBox, collidingBoxes, getShape(state));
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return getShape(state).offset(pos);
    }

    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(IBlockState state, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return this.rayTrace(pos, start, end, getPickShape(state));
    }

    @Override
    protected Block GetGlowVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron;
    }

    @Override
    protected Block GetTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron;
    }

    @Override
    protected Block GetLavaVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron;
    }

    @Override
    protected Block GetUnlitTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_unlit;
    }

    @Override
    protected Block GetRedTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron;
    }

    @Override
    protected Block GetCandleVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron;
    }

    @Override
    protected Block GetRedVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron;
    }
}
