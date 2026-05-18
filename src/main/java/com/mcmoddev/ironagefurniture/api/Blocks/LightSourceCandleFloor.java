package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.client.particle.CandleFlameParticle;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LightSourceCandleFloor extends BlockHBase {
    protected static final AxisAlignedBB FLOOR_NORTH = new AxisAlignedBB(
        4.0D / 16.0D, 0.0D, 4.0D / 16.0D,
        9.0D / 16.0D, 6.0D / 16.0D, 11.0D / 16.0D
    );
    protected static final AxisAlignedBB FLOOR_EAST = rotateClockwise(FLOOR_NORTH);
    protected static final AxisAlignedBB FLOOR_SOUTH = rotateHalfTurn(FLOOR_NORTH);
    protected static final AxisAlignedBB FLOOR_WEST = rotateCounterClockwise(FLOOR_NORTH);

    public LightSourceCandleFloor(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn);
        this.setSoundType(SoundType.CLOTH);
        this.blockResistance = resistance;
        this.blockHardness = hardness;
        this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setLightLevel(12.0F / 15.0F);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING });
    }

    protected boolean canPlaceOn(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.isSideSolid(worldIn, pos, EnumFacing.UP)) {
            return true;
        }

        return state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        if (this.canPlaceAt(worldIn, pos, EnumFacing.UP)) {
            return true;
        }

        for (EnumFacing facing : FACING.getAllowedValues()) {
            if (this.canPlaceAt(worldIn, pos, facing)) {
                return true;
            }
        }

        return false;
    }

    protected boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing) {
        if (facing == EnumFacing.UP) {
            return this.canPlaceOn(worldIn, pos.down());
        }

        if (!facing.getAxis().isHorizontal()) {
            return false;
        }

        BlockPos behind = pos.offset(facing.getOpposite());
        return worldIn.isSideSolid(behind, facing, true);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side,
            float hitX, float hitY, float hitZ, int meta,
            EntityLivingBase placer, ItemStack stack) {
        if (side.getAxis().isHorizontal() && this.canPlaceAt(world, pos, side)) {
            return GetWallVariant().getDefaultState().withProperty(FACING, side);
        }

        if (this.canPlaceOn(world, pos.down())) {
            return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
        }

        return null;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if (!this.canPlaceOn(worldIn, pos.down())) {
            if (!worldIn.isRemote) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
            worldIn.setBlockToAir(pos);
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
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList(new ItemStack(BlockObjectHolder.light_metal_ironage_candle_floor, 1));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
            EnumFacing side, float hitX, float hitY, float hitZ) {
        if (heldItem != null && heldItem.stackSize > 0 && heldItem.getItem() == Items.WATER_BUCKET) {
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos,
                    GetUnlitVariant().getDefaultState().withProperty(FACING, state.getValue(FACING)),
                    3);
            }

            return true;
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (!IsLit()) {
            return;
        }

        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 6.7D / 16.0D;
        double z = pos.getZ() + 0.5D;
        double[] flamePoint = rotateFloorPoint(state.getValue(FACING), 6.5D / 16.0D, 6.5D / 16.0D);
        x = pos.getX() + flamePoint[0];
        z = pos.getZ() + flamePoint[1];

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

    protected AxisAlignedBB getShape(IBlockState state) {
        switch (state.getValue(FACING)) {
            case EAST:
                return FLOOR_EAST;
            case SOUTH:
                return FLOOR_SOUTH;
            case WEST:
                return FLOOR_WEST;
            default:
                return FLOOR_NORTH;
        }
    }

    protected static AxisAlignedBB rotateClockwise(AxisAlignedBB bb) {
        return new AxisAlignedBB(
            1.0D - bb.maxZ,
            bb.minY,
            bb.minX,
            1.0D - bb.minZ,
            bb.maxY,
            bb.maxX
        );
    }

    protected static AxisAlignedBB rotateHalfTurn(AxisAlignedBB bb) {
        return new AxisAlignedBB(
            1.0D - bb.maxX,
            bb.minY,
            1.0D - bb.maxZ,
            1.0D - bb.minX,
            bb.maxY,
            1.0D - bb.minZ
        );
    }

    protected static AxisAlignedBB rotateCounterClockwise(AxisAlignedBB bb) {
        return new AxisAlignedBB(
            bb.minZ,
            bb.minY,
            1.0D - bb.maxX,
            bb.maxZ,
            bb.maxY,
            1.0D - bb.minX
        );
    }

    protected boolean IsLit() {
        return true;
    }

    protected Item CandleItem() {
        return Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_candle_floor);
    }

    protected Block GetWallVariant() {
        return BlockObjectHolder.light_metal_ironage_candle_wall;
    }

    protected Block GetUnlitVariant() {
        return BlockObjectHolder.light_metal_ironage_candle_floor_unlit;
    }

    protected Block GetLitVariant() {
        return BlockObjectHolder.light_metal_ironage_candle_floor;
    }
}
