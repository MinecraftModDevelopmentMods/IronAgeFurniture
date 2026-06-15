package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LightSourceSconceGlowWall extends LightSourceSconceTorchWall {
    private static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(
        0.0D / 16.0D, 5.0D / 16.0D, 5.5D / 16.0D,
        6.5D / 16.0D, 11.0D / 16.0D, 10.5D / 16.0D
    );

    private static final AxisAlignedBB AABB_NORTH = rotateCounterClockwise(AABB_EAST);
    private static final AxisAlignedBB AABB_SOUTH = rotateClockwise(AABB_EAST);
    private static final AxisAlignedBB AABB_WEST = rotateHalfTurn(AABB_EAST);

    public LightSourceSconceGlowWall(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
        this.setLightLevel(14.0F / 15.0F);
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
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        // Glow lamps emit steady light but have no flame or smoke particles.
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
            EnumFacing side, float hitX, float hitY, float hitZ) {
        if (tryTakeLightOut(worldIn, pos, state, playerIn, hand, heldItem)) {
            return true;
        }

        if (heldItem != null && heldItem.stackSize > 0 && isBlockedInteractionItem(heldItem)) {
            return true;
        }

        return false;
    }

    private boolean isBlockedInteractionItem(ItemStack heldItem) {
        return heldItem.getItem() == Items.WATER_BUCKET
            || isBlockedFilledSconceItem(heldItem);
    }

    @Override
    protected Block LightDrop() {
        return BlockObjectHolder.light_metal_ironage_block_floor_glow_clear;
    }

    @Override
    protected Block DropVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron;
    }

    @Override
    protected boolean CanEx() {
        return false;
    }

    @Override
    protected boolean HasFlame() {
        return false;
    }

    @Override
    protected Block GetWallVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron;
    }

    @Override
    protected Block GetGlowVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_glow_iron;
    }
}
