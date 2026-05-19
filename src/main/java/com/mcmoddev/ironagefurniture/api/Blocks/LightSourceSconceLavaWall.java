package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LightSourceSconceLavaWall extends LightSourceSconceGlowWall {
    public LightSourceSconceLavaWall(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        EnumFacing facing = state.getValue(FACING);
        BlockPos behind = pos.offset(facing.getOpposite());

        if (!worldIn.getBlockState(behind).isSideSolid(worldIn, behind, facing)) {
            if (!worldIn.isRemote) {
                spawnAsEntity(worldIn, pos, new ItemStack(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, 1));
                IBlockState lavaState = LightDrop().getDefaultState().withProperty(FACING, facing);
                worldIn.setBlockState(pos, lavaState, 3);
                worldIn.scheduleUpdate(pos, LightDrop(), LightDrop().tickRate(worldIn));
            }

            return;
        }

        super.neighborChanged(state, worldIn, pos, blockIn);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList(new ItemStack(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, 1));
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
            TileEntity te, ItemStack stack) {
        boolean silkTouch = hasSilkTouch(stack);

        if (silkTouch && !player.capabilities.isCreativeMode && !worldIn.isRemote) {
            spawnAsEntity(worldIn, pos, new ItemStack(LightDrop(), 1));
        }

        super.harvestBlock(worldIn, player, pos, state, te, stack);

        if (!silkTouch && !player.capabilities.isCreativeMode && !worldIn.isRemote) {
            breakIntoFire(worldIn, pos, player);
        }
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        BlockPos blockpos = pos.up();

        if (world.isAirBlock(blockpos) && !world.getBlockState(blockpos).isFullBlock()) {
            if (rand.nextInt(25) == 0) {
                EnumFacing facing = state.getValue(FACING);
                EnumFacing opposite = facing.getOpposite();
                double x = (double)pos.getX() + 0.5D + 0.27D * opposite.getFrontOffsetX();
                double y = (double)pos.getY() + 0.72D;
                double z = (double)pos.getZ() + 0.5D + 0.27D * opposite.getFrontOffsetZ();

                world.spawnParticle(EnumParticleTypes.LAVA, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
                world.playSound(x, y, z, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS,
                    0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }

            if (rand.nextInt(200) == 0) {
                world.playSound((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(),
                    SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS,
                    0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }
        }
    }

    private boolean hasSilkTouch(ItemStack stack) {
        return stack != null && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
    }

    protected void breakIntoFire(World worldIn, BlockPos pos, EntityPlayer player) {
        worldIn.playSound(player, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
        worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
    }

    @Override
    protected Block LightDrop() {
        return BlockObjectHolder.light_metal_ironage_block_floor_lava_clear;
    }

    @Override
    protected Block GetWallVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron;
    }

    @Override
    protected Block GetLavaVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron;
    }
}
