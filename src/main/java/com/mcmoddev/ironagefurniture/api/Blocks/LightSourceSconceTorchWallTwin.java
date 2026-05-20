package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class LightSourceSconceTorchWallTwin extends LightSourceSconceTorchFloorTwin {
    private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(
        0.0D / 16.0D, 2.0D / 16.0D, 5.0D / 16.0D,
        7.0D / 16.0D, 15.0D / 16.0D, 11.0D / 16.0D
    );

    private static final AxisAlignedBB AABB_NORTH = rotateCounterClockwise(AABB_EAST);
    private static final AxisAlignedBB AABB_SOUTH = rotateClockwise(AABB_EAST);
    private static final AxisAlignedBB AABB_WEST = rotateHalfTurn(AABB_EAST);

    public LightSourceSconceTorchWallTwin(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
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

    private AxisAlignedBB getShape(IBlockState state) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return AABB_NORTH;
            case SOUTH:
                return AABB_SOUTH;
            case WEST:
                return AABB_WEST;
            default:
                return AABB_EAST;
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
        return this.rayTrace(pos, start, end, getShape(state));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        EnumFacing facing = state.getValue(FACING);
        BlockPos behind = pos.offset(facing.getOpposite());
        if (!worldIn.getBlockState(behind).isSideSolid(worldIn, behind, facing)) {
            if (!worldIn.isRemote) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        for (int i = 0; i < 2; i++) {
            double[] offset = getWallFlameOffset(i);
            double[] rotated = rotateWallPoint(state.getValue(FACING), offset[0], offset[2]);
            spawnTorchFlame(world, pos.getX() + rotated[0], pos.getY() + offset[1], pos.getZ() + rotated[1]);
        }
    }

    private double[] getWallFlameOffset(int index) {
        return index == 0
            ? new double[] { 3.5D / 16.0D, 15.0D / 16.0D, 9.6D / 16.0D }
            : new double[] { 3.5D / 16.0D, 15.0D / 16.0D, 6.4D / 16.0D };
    }

    private double[] rotateWallPoint(EnumFacing facing, double x, double z) {
        switch (facing) {
            case SOUTH:
                return new double[] { 1.0D - z, x };
            case WEST:
                return new double[] { 1.0D - x, 1.0D - z };
            case NORTH:
                return new double[] { z, 1.0D - x };
            default:
                return new double[] { x, z };
        }
    }

    @Override
    protected Block DropVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron;
    }

    @Override
    protected Block GetWallVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_twin;
    }

    @Override
    protected Block GetTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_twin;
    }

    @Override
    protected Block GetTwinTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_twin;
    }

    @Override
    protected Block GetUnlitTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_twin_unlit;
    }
}
