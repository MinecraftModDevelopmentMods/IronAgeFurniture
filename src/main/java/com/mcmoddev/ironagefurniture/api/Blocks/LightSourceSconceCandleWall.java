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
    private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(
        0.0D / 16.0D, 2.0D / 16.0D, 5.5D / 16.0D,
        6.5D / 16.0D, 13.0D / 16.0D, 10.5D / 16.0D
    );

    private static final AxisAlignedBB AABB_NORTH = rotateCounterClockwise(AABB_EAST);
    private static final AxisAlignedBB AABB_SOUTH = rotateClockwise(AABB_EAST);
    private static final AxisAlignedBB AABB_WEST = rotateHalfTurn(AABB_EAST);

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
        EnumFacing facing = state.getValue(FACING);
        EnumFacing opposite = facing.getOpposite();

        for (int i = 0; i < CandleCount(); i++) {
            double spread = getWallFlameSpread(i, CandleCount());
            double x = pos.getX() + 0.5D + 0.29D * opposite.getFrontOffsetX();
            double y = pos.getY() + 0.98D;
            double z = pos.getZ() + 0.5D + 0.29D * opposite.getFrontOffsetZ();

            if (facing.getAxis() == EnumFacing.Axis.X) {
                z += spread;
            } else {
                x += spread;
            }

            spawnFlame(world, rand, x, y, z);
        }
    }

    private double getWallFlameSpread(int index, int count) {
        if (count == 1) {
            return 0.0D;
        }
        if (count == 2) {
            return index == 0 ? -0.08D : 0.08D;
        }
        if (count == 3) {
            return (index - 1) * 0.08D;
        }

        return (index - 1.5D) * 0.07D;
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
