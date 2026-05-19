package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LightSourceLava extends LightSourceGlowdust {
    public LightSourceLava(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
    }

    @Override
    protected void onStartFalling(EntityFallingBlock fallingEntity) {
        fallingEntity.shouldDropItem = false;
    }

    @Override
    public void onEndFalling(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            breakIntoFire(worldIn, pos, null);
        }
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        BlockPos blockpos = pos.up();

        if (worldIn.isAirBlock(blockpos) && !worldIn.getBlockState(blockpos).isFullBlock()) {
            if (rand.nextInt(25) == 0) {
                double x = (double)pos.getX() + 0.5D;
                double y = (double)pos.getY() + 0.25D;
                double z = (double)pos.getZ() + 0.5D;

                worldIn.spawnParticle(EnumParticleTypes.LAVA, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
                worldIn.playSound(x, y, z, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS,
                    0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }

            if (rand.nextInt(200) == 0) {
                worldIn.playSound((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(),
                    SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS,
                    0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }
        }

        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
            TileEntity te, ItemStack stack) {
        boolean silkTouch = hasSilkTouch(stack);
        super.harvestBlock(worldIn, player, pos, state, te, stack);

        if (!silkTouch && !player.capabilities.isCreativeMode && !worldIn.isRemote) {
            breakIntoFire(worldIn, pos, player);
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList();
    }

    private boolean hasSilkTouch(ItemStack stack) {
        return stack != null && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
    }

    protected void breakIntoFire(World worldIn, BlockPos pos, EntityPlayer player) {
        worldIn.playSound(player, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
        worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
    }

    protected Block LavaLampBlock() {
        return BlockObjectHolder.light_metal_ironage_block_floor_lava_clear;
    }
}
