package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.client.particle.CandleFlameParticle;

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

public class LightSourceSconceCandleFloor extends LightSourceSconceTorchFloor {
    private final int candleCount;

    public LightSourceSconceCandleFloor(Material materialIn, String name, float resistance, float hardness, int candleCount) {
        super(materialIn, name, resistance, hardness);
        this.candleCount = candleCount;
        this.setLightLevel((11.0F + candleCount) / 15.0F);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = Lists.newArrayList();
        drops.add(new ItemStack(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, 1));
        drops.add(new ItemStack(BlockObjectHolder.light_metal_ironage_candle_floor, candleCount));
        return drops;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
            EnumFacing side, float hitX, float hitY, float hitZ) {
        if (heldItem == null || heldItem.stackSize <= 0) {
            if (!worldIn.isRemote) {
                setSconceState(worldIn, pos, state, DropVariant());
                giveCandles(playerIn, hand, heldItem, candleCount);
            }

            return true;
        }

        if (heldItem.getItem() == CandleItem()) {
            if (!worldIn.isRemote) {
                if (candleCount < 4) {
                    setSconceState(worldIn, pos, state, getCandleVariant(candleCount + 1, true));

                    if (!playerIn.capabilities.isCreativeMode) {
                        heldItem.stackSize--;
                    }
                } else {
                    setSconceState(worldIn, pos, state, DropVariant());
                    giveCandles(playerIn, hand, heldItem, candleCount);
                }
            }

            return true;
        }

        if (heldItem.getItem() == Items.WATER_BUCKET) {
            if (!worldIn.isRemote) {
                setSconceState(worldIn, pos, state, getCandleVariant(candleCount, false));
            }

            return true;
        }

        if (isLightSourceItem(heldItem)) {
            return true;
        }

        return false;
    }

    protected void setSconceState(World worldIn, BlockPos pos, IBlockState state, Block block) {
        worldIn.setBlockState(pos, block.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
    }

    protected boolean isLightSourceItem(ItemStack heldItem) {
        return heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH)
            || heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_glow_clear)
            || heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear)
            || heldItem.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)
            || heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_red_clear);
    }

    protected void giveCandles(EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, int count) {
        if (playerIn.capabilities.isCreativeMode) {
            return;
        }

        int remaining = count;

        if (heldItem == null || heldItem.stackSize <= 0) {
            playerIn.setHeldItem(hand, new ItemStack(BlockObjectHolder.light_metal_ironage_candle_floor, remaining));
            return;
        }

        if (heldItem.getItem() == CandleItem() && heldItem.stackSize < heldItem.getMaxStackSize()) {
            int added = Math.min(heldItem.getMaxStackSize() - heldItem.stackSize, remaining);
            heldItem.stackSize += added;
            remaining -= added;
        }

        if (remaining > 0) {
            playerIn.inventory.addItemStackToInventory(
                new ItemStack(BlockObjectHolder.light_metal_ironage_candle_floor, remaining));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        for (int i = 0; i < candleCount; i++) {
            double[] offset = getFloorFlameOffset(i, candleCount);
            double[] rotated = rotateFloorPoint(state.getValue(FACING), offset[0], offset[2]);
            spawnFlame(world, rand, pos.getX() + rotated[0], pos.getY() + offset[1], pos.getZ() + rotated[1]);
        }
    }

    protected double[] getFloorFlameOffset(int index, int count) {
        if (count == 1) {
            return new double[] { 7.5D / 16.0D, 15.6D / 16.0D, 7.5D / 16.0D };
        }
        if (count == 2) {
            return index == 0
                ? new double[] { 6.5D / 16.0D, 15.6D / 16.0D, 7.5D / 16.0D }
                : new double[] { 9.5D / 16.0D, 15.6D / 16.0D, 8.5D / 16.0D };
        }
        if (count == 3) {
            switch (index) {
                case 0:
                    return new double[] { 6.5D / 16.0D, 15.6D / 16.0D, 6.5D / 16.0D };
                case 1:
                    return new double[] { 9.5D / 16.0D, 15.6D / 16.0D, 7.5D / 16.0D };
                default:
                    return new double[] { 6.5D / 16.0D, 14.6D / 16.0D, 9.5D / 16.0D };
            }
        }

        switch (index) {
            case 0:
                return new double[] { 6.5D / 16.0D, 15.6D / 16.0D, 6.5D / 16.0D };
            case 1:
                return new double[] { 9.5D / 16.0D, 15.6D / 16.0D, 9.5D / 16.0D };
            case 2:
                return new double[] { 9.5D / 16.0D, 13.6D / 16.0D, 6.5D / 16.0D };
            default:
                return new double[] { 6.5D / 16.0D, 14.6D / 16.0D, 9.5D / 16.0D };
        }
    }

    protected void spawnFlame(World world, Random rand, double x, double y, double z) {
        if (rand.nextInt(3) == 0) {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y + 0.04D, z, 0.0D, 0.0D, 0.0D);
        }

        CandleFlameParticle.spawn(world, x, y, z);
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

    protected Item CandleItem() {
        return Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_candle_floor);
    }

    protected int CandleCount() {
        return candleCount;
    }

    protected boolean isWallVariant() {
        return false;
    }

    protected Block getCandleVariant(int count, boolean lit) {
        if (isWallVariant()) {
            switch (count) {
                case 2:
                    return lit ? BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_two
                        : BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_two_unlit;
                case 3:
                    return lit ? BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_three
                        : BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_three_unlit;
                case 4:
                    return lit ? BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_four
                        : BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_four_unlit;
                default:
                    return lit ? BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron
                        : BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_unlit;
            }
        }

        switch (count) {
            case 2:
                return lit ? BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_two
                    : BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_two_unlit;
            case 3:
                return lit ? BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_three
                    : BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_three_unlit;
            case 4:
                return lit ? BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_four
                    : BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_four_unlit;
            default:
                return lit ? BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron
                    : BlockObjectHolder.light_metal_ironage_sconce_floor_candle_iron_unlit;
        }
    }

    protected Block GetLitCandleVariant() {
        return getCandleVariant(candleCount, true);
    }

    protected Block GetUnlitCandleVariant() {
        return getCandleVariant(candleCount, false);
    }

    protected Block getWallCandleVariant(int count, boolean lit) {
        switch (count) {
            case 2:
                return lit ? BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_two
                    : BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_two_unlit;
            case 3:
                return lit ? BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_three
                    : BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_three_unlit;
            case 4:
                return lit ? BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_four
                    : BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_four_unlit;
            default:
                return lit ? BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron
                    : BlockObjectHolder.light_metal_ironage_sconce_wall_candle_iron_unlit;
        }
    }

    @Override
    protected Block DropVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
    }

    @Override
    protected Block LightDrop() {
        return BlockObjectHolder.light_metal_ironage_candle_floor;
    }

    @Override
    protected boolean HasFlame() {
        return true;
    }

    @Override
    protected Block GetWallVariant() {
        return getWallCandleVariant(candleCount, true);
    }

    @Override
    protected Block GetUnlitTorchVariant() {
        return getCandleVariant(candleCount, false);
    }
}
