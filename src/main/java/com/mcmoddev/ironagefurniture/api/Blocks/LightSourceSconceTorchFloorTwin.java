package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LightSourceSconceTorchFloorTwin extends LightSourceSconceTorchFloor {
    private static final int TORCH_COUNT = 2;

    public LightSourceSconceTorchFloorTwin(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
        this.setLightLevel(15.0F / 15.0F);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = Lists.newArrayList();
        drops.add(new ItemStack(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, 1));
        drops.add(new ItemStack(Blocks.TORCH, TORCH_COUNT));
        return drops;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
                                    EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if (heldItem == null || heldItem.stackSize <= 0 || heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos,
                    DropVariant().getDefaultState().withProperty(FACING, state.getValue(FACING)),
                    3);
                giveTorches(playerIn, hand, heldItem, TORCH_COUNT);
            }

            return true;
        }

        if (heldItem.getItem() == Items.WATER_BUCKET) {
            if (!worldIn.isRemote) {
                Block unlit = GetUnlitTorchVariant();
                worldIn.setBlockState(pos, unlit.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
            }

            return true;
        }

        if (isBlockedFilledSconceItem(heldItem)) {
            return true;
        }

        return false;
    }

    protected void giveTorches(EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, int count) {
        if (playerIn.capabilities.isCreativeMode) {
            return;
        }

        int remaining = count;
        Item torchItem = Item.getItemFromBlock(Blocks.TORCH);

        if (heldItem == null || heldItem.stackSize <= 0) {
            playerIn.setHeldItem(hand, new ItemStack(Blocks.TORCH, remaining));
            return;
        }

        if (heldItem.getItem() == torchItem && heldItem.stackSize < heldItem.getMaxStackSize()) {
            int added = Math.min(heldItem.getMaxStackSize() - heldItem.stackSize, remaining);
            heldItem.stackSize += added;
            remaining -= added;
        }

        if (remaining > 0) {
            playerIn.inventory.addItemStackToInventory(new ItemStack(Blocks.TORCH, remaining));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        for (int i = 0; i < TORCH_COUNT; i++) {
            double[] offset = getFloorFlameOffset(i);
            double[] rotated = rotateFloorPoint(state.getValue(FACING), offset[0], offset[2]);
            spawnTorchFlame(world, pos.getX() + rotated[0], pos.getY() + offset[1], pos.getZ() + rotated[1]);
        }
    }

    protected double[] getFloorFlameOffset(int index) {
        return index == 0
            ? new double[] { 6.1D / 16.0D, 15.6D / 16.0D, 6.8D / 16.0D }
            : new double[] { 9.9D / 16.0D, 15.6D / 16.0D, 9.2D / 16.0D };
    }

    protected void spawnTorchFlame(World world, double x, double y, double z) {
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D);
        world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    protected double[] rotateFloorPoint(EnumFacing facing, double x, double z) {
        switch (facing) {
            case EAST:
                return new double[] { 1.0D - z, x };
            case SOUTH:
                return new double[] { 1.0D - x, 1.0D - z };
            case WEST:
                return new double[] { z, 1.0D - x };
            default:
                return new double[] { x, z };
        }
    }

    @Override
    protected Block GetWallVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_twin;
    }

    @Override
    protected Block GetTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_twin;
    }

    @Override
    protected Block GetTwinTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_twin;
    }

    @Override
    protected Block GetUnlitTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_torch_iron_twin_unlit;
    }
}
