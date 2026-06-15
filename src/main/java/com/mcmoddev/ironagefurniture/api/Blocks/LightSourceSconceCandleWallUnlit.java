package com.mcmoddev.ironagefurniture.api.Blocks;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LightSourceSconceCandleWallUnlit extends LightSourceSconceCandleWall {
    public LightSourceSconceCandleWallUnlit(Material materialIn, String name, float resistance, float hardness, int candleCount) {
        super(materialIn, name, resistance, hardness, candleCount);
        this.setLightLevel(0.0F);
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, java.util.Random rand) {
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
            EnumFacing side, float hitX, float hitY, float hitZ) {
        if (heldItem != null && heldItem.stackSize > 0) {
            if (heldItem.getItem() == Items.FLINT_AND_STEEL) {
                if (!worldIn.isRemote) {
                    setSconceState(worldIn, pos, state, GetLitCandleVariant());

                    if (!playerIn.capabilities.isCreativeMode) {
                        heldItem.damageItem(1, playerIn);
                    }
                }

                return true;
            }

            if (heldItem.getItem() == CandleItem()) {
                if (!worldIn.isRemote) {
                    setSconceState(worldIn, pos, state, GetLitCandleVariant());
                }

                return true;
            }

            if (heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
                if (!worldIn.isRemote) {
                    setSconceState(worldIn, pos, state, GetLitCandleVariant());
                }

                return true;
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        super.neighborChanged(state, worldIn, pos, blockIn);

        if (worldIn.getBlockState(pos).getBlock() != this) {
            return;
        }

        if (worldIn.isBlockPowered(pos) && !worldIn.isUpdateScheduled(pos, this)) {
            worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, java.util.Random rand) {
        if (!worldIn.isBlockPowered(pos)) {
            return;
        }

        setSconceState(worldIn, pos, state, GetLitCandleVariant());
    }

    @Override
    public int tickRate(World worldIn) {
        return 2;
    }

    @Override
    protected boolean HasFlame() {
        return false;
    }
}
