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

public class LightSourceSconceCandleWall extends LightSourceSconceCandleFloor {
    public LightSourceSconceCandleWall(Material materialIn, String name, float resistance, float hardness, int candleCount) {
        super(materialIn, name, resistance, hardness, candleCount);
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

    protected AxisAlignedBB getShape(IBlockState state) {
        AxisAlignedBB southShape = new AxisAlignedBB(
            5.0D / 16.0D,
            9.0D / 16.0D,
            0.0D / 16.0D,
            11.0D / 16.0D,
            11.0D / 16.0D,
            7.0D / 16.0D
        );

        switch (state.getValue(FACING)) {
            case NORTH:
                return rotateHalfTurn(southShape);
            case EAST:
                return rotateCounterClockwise(southShape);
            case SOUTH:
                return southShape;
            case WEST:
                return rotateClockwise(southShape);
            default:
                return southShape;
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
        EnumFacing facing = state.getValue(FACING);

        for (int i = 0; i < CandleCount(); i++) {
            double[] offset = getWallFlameOffset(i, CandleCount());
            double[] rotated = rotateWallPoint(facing, offset[0], offset[2]);
            spawnFlame(world, rand, pos.getX() + rotated[0], pos.getY() + offset[1], pos.getZ() + rotated[1]);
        }
    }

    private double[] getWallFlameOffset(int index, int count) {
        if (count == 1) {
            return new double[] { 7.5D / 16.0D, 15.6D / 16.0D, 4.5D / 16.0D };
        }
        if (count == 2) {
            return index == 0
                ? new double[] { 9.5D / 16.0D, 15.6D / 16.0D, 2.5D / 16.0D }
                : new double[] { 6.5D / 16.0D, 13.6D / 16.0D, 5.5D / 16.0D };
        }
        if (count == 3) {
            switch (index) {
                case 0:
                    return new double[] { 9.5D / 16.0D, 15.6D / 16.0D, 2.5D / 16.0D };
                case 1:
                    return new double[] { 9.5D / 16.0D, 14.6D / 16.0D, 5.5D / 16.0D };
                default:
                    return new double[] { 6.5D / 16.0D, 13.6D / 16.0D, 5.5D / 16.0D };
            }
        }

        switch (index) {
            case 0:
                return new double[] { 9.5D / 16.0D, 15.6D / 16.0D, 2.5D / 16.0D };
            case 1:
                return new double[] { 9.5D / 16.0D, 14.6D / 16.0D, 5.5D / 16.0D };
            case 2:
                return new double[] { 6.5D / 16.0D, 16.25D / 16.0D, 2.5D / 16.0D };
            default:
                return new double[] { 6.5D / 16.0D, 13.6D / 16.0D, 5.5D / 16.0D };
        }
    }

    private double[] rotateWallPoint(EnumFacing facing, double x, double z) {
        switch (facing) {
            case WEST:
                return new double[] { 1.0D - z, x };
            case NORTH:
                return new double[] { 1.0D - x, 1.0D - z };
            case EAST:
                return new double[] { z, 1.0D - x };
            default:
                return new double[] { x, z };
        }
    }

    @Override
    protected boolean isWallVariant() {
        return true;
    }

    @Override
    protected Block DropVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron;
    }

    @Override
    protected Block GetWallVariant() {
        return getCandleVariant(CandleCount(), true);
    }
}
