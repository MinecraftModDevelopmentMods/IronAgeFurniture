package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LightSourceGlowdust extends BlockFalling {
    private static final AxisAlignedBB AABB = new AxisAlignedBB(
        6.0D / 16.0D, 0.0D, 6.0D / 16.0D,
        10.0D / 16.0D, 5.0D / 16.0D, 10.0D / 16.0D
    );

    public LightSourceGlowdust(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn);
        this.setSoundType(SoundType.GLASS);
        this.blockResistance = resistance;
        this.blockHardness = hardness;
        this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHBase.FACING, EnumFacing.NORTH));
        this.setLightLevel(1.0F);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockHBase.FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(BlockHBase.FACING)).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockHBase.FACING });
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ, int meta,
            EntityLivingBase placer, ItemStack stack) {
        return this.getDefaultState().withProperty(BlockHBase.FACING, placer.getHorizontalFacing());
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
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public float getAmbientOcclusionLightValue(IBlockState state) {
        return 1.0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList(new ItemStack(BlockObjectHolder.light_metal_ironage_block_floor_glow_clear, 1));
    }
}
