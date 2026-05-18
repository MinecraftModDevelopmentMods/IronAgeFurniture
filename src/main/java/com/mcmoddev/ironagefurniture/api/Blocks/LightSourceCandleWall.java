package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.client.particle.CandleFlameParticle;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class LightSourceCandleWall extends LightSourceCandleFloor {
    protected static final AxisAlignedBB WALL_EAST = new AxisAlignedBB(
        0.0D / 16.0D, 2.0D / 16.0D, 5.5D / 16.0D,
        6.5D / 16.0D, 11.0D / 16.0D, 10.5D / 16.0D
    );

    protected static final AxisAlignedBB WALL_NORTH = rotateCounterClockwise(WALL_EAST);
    protected static final AxisAlignedBB WALL_SOUTH = rotateClockwise(WALL_EAST);
    protected static final AxisAlignedBB WALL_WEST = rotateHalfTurn(WALL_EAST);

    public LightSourceCandleWall(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
    }

    protected static AxisAlignedBB rotateClockwise(AxisAlignedBB bb) {
        return new AxisAlignedBB(
            1.0D - bb.maxZ,
            bb.minY,
            bb.minX,
            1.0D - bb.minZ,
            bb.maxY,
            bb.maxX
        );
    }

    protected static AxisAlignedBB rotateHalfTurn(AxisAlignedBB bb) {
        return new AxisAlignedBB(
            1.0D - bb.maxX,
            bb.minY,
            1.0D - bb.maxZ,
            1.0D - bb.minX,
            bb.maxY,
            1.0D - bb.minZ
        );
    }

    protected static AxisAlignedBB rotateCounterClockwise(AxisAlignedBB bb) {
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
        switch (state.getValue(FACING)) {
            case NORTH:
                return WALL_NORTH;
            case SOUTH:
                return WALL_SOUTH;
            case WEST:
                return WALL_WEST;
            default:
                return WALL_EAST;
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
        if (!IsLit()) {
            return;
        }

        double[] flamePoint = rotateWallPoint(state.getValue(FACING), 7.5D / 16.0D, 1.5D / 16.0D);
        double x = pos.getX() + flamePoint[0];
        double y = pos.getY() + 14.6D / 16.0D;
        double z = pos.getZ() + flamePoint[1];

        if (rand.nextInt(3) == 0) {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y + 0.04D, z, 0.0D, 0.0D, 0.0D);
        }

        CandleFlameParticle.spawn(world, x, y, z);
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
    protected Block GetWallVariant() {
        return BlockObjectHolder.light_metal_ironage_candle_wall;
    }

    @Override
    protected Block GetUnlitVariant() {
        return BlockObjectHolder.light_metal_ironage_candle_wall_unlit;
    }

    @Override
    protected Block GetLitVariant() {
        return BlockObjectHolder.light_metal_ironage_candle_wall;
    }
}
