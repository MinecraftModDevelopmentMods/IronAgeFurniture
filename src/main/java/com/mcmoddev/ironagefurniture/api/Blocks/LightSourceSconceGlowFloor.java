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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LightSourceSconceGlowFloor extends LightSourceSconceTorchFloor {
    private static final AxisAlignedBB AABB = new AxisAlignedBB(
        6.0D / 16.0D, 0.0D, 6.0D / 16.0D,
        10.0D / 16.0D, 10.0D / 16.0D, 10.0D / 16.0D
    );

    public LightSourceSconceGlowFloor(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
        this.setLightLevel(14.0F / 15.0F);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos,
            AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
        super.addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB);
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

    protected boolean isBlockedInteractionItem(ItemStack heldItem) {
        return heldItem.getItem() == Items.WATER_BUCKET
            || isSconceLightSourceItem(heldItem);
    }

    @Override
    protected Block LightDrop() {
        return BlockObjectHolder.light_metal_ironage_block_floor_glow_clear;
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
        return BlockObjectHolder.light_metal_ironage_sconce_floor_glow_iron;
    }
}
