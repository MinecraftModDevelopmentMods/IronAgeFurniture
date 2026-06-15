package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LightSourceSconceTorchWall extends LightSourceSconceTorchFloor {
    private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(
        0.0 / 16.0, 2.0 / 16.0, 5.5 / 16.0,
        6.5 / 16.0, 13.0 / 16.0, 10.5 / 16.0
    );

    private static final AxisAlignedBB AABB_NORTH = rotateCounterClockwise(AABB_EAST);
    private static final AxisAlignedBB AABB_SOUTH = rotateClockwise(AABB_EAST);
    private static final AxisAlignedBB AABB_WEST = rotateHalfTurn(AABB_EAST);

    public LightSourceSconceTorchWall(Material materialIn, String name, float resistance, float hardness) {
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
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, java.util.Random rand) {
        if (HasFlame()) {
            EnumFacing facing = state.getValue(FACING);
            double baseX;
            double baseZ;

            switch (facing) {
                case WEST:
                    baseX = 0.4D;
                    baseZ = 0.5D;
                    break;
                case NORTH:
                    baseX = 0.5D;
                    baseZ = 0.4D;
                    break;
                case SOUTH:
                    baseX = 0.5D;
                    baseZ = 0.6D;
                    break;
                default:
                    baseX = 0.6D;
                    baseZ = 0.5D;
                    break;
            }

            EnumFacing opposite = facing.getOpposite();
            double x = pos.getX() + baseX + (0.27D * opposite.getFrontOffsetX());
            double y = pos.getY() + 1.02D;
            double z = pos.getZ() + baseZ + (0.27D * opposite.getFrontOffsetZ());

            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected Block DropVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron;
    }

    @Override
    protected Block GetWallVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron;
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
    protected Block GetTwinTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_twin;
    }

    @Override
    protected Block GetLavaVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron;
    }

    @Override
    protected Block GetRedTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron;
    }

    @Override
    protected Block GetRedVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron;
    }

    @Override
    protected Block GetUnlitTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_unlit;
    }
}
