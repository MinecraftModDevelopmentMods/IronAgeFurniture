package com.mcmoddev.ironagefurniture.api.Blocks;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LightSourceSconceTorchWallUnlit extends LightSourceSconceTorchWall {

    public LightSourceSconceTorchWallUnlit(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
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
                    worldIn.setBlockState(pos,
                        GetLitVariant().getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);

                    if (!playerIn.capabilities.isCreativeMode) {
                        heldItem.damageItem(1, playerIn);
                    }
                }

                return true;
            }

            if (heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
                if (!worldIn.isRemote) {
                    worldIn.setBlockState(pos,
                        GetLitVariant().getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
                }

                return true;
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    protected Block GetLitVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron;
    }

    @Override
    protected Block GetWallVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_torch_iron_unlit;
    }
}
