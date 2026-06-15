package com.mcmoddev.ironagefurniture.api.Blocks;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper.MetalVariant;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityMetalVariant;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import java.util.List;
import com.google.common.collect.Lists;

public class ChainTop extends Block {
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

    private static final AxisAlignedBB AABB = new AxisAlignedBB(
        6.0D / 16.0D, 0.0D, 6.0D / 16.0D,
        10.0D / 16.0D, 1.0D, 10.0D / 16.0D
    );

    public ChainTop(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn);
        this.setSoundType(SoundType.METAL);
        this.blockResistance = resistance;
        this.blockHardness = hardness;
        this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, Integer.valueOf(0))
            .withProperty(MetalVariantHelper.METAL, MetalVariant.IRON));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(POWER, Integer.valueOf(meta & 15));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((Integer) state.getValue(POWER)).intValue();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { POWER, MetalVariantHelper.METAL });
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MetalVariantHelper.withMetal(state, worldIn, pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityMetalVariant();
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return MetalVariantHelper.getHardness(worldIn, pos, this.blockHardness);
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN && canHangFrom(worldIn, pos.up());
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return canHangFrom(worldIn, pos.up());
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        notifyPowerNeighbors(worldIn, pos);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        notifyPowerNeighbors(worldIn, pos);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if (!canHangFrom(worldIn, pos.up())) {
            if (!worldIn.isRemote) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }

            worldIn.setBlockToAir(pos);
            notifyPowerNeighbors(worldIn, pos);
            return;
        }

        if (!worldIn.isUpdateScheduled(pos, this)) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, java.util.Random rand) {
        if (worldIn.isRemote) {
            return;
        }

        if (!canHangFrom(worldIn, pos.up())) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            notifyPowerNeighbors(worldIn, pos);
            return;
        }

        int currentPower = ((Integer) state.getValue(POWER)).intValue();
        int nextPower = getPowerFromAbove(worldIn, pos);

        if (currentPower != nextPower) {
            MetalVariantHelper.replaceBlockPreservingMetal(worldIn, pos,
                state.withProperty(POWER, Integer.valueOf(nextPower)), 3);
            notifyPowerNeighbors(worldIn, pos);
        }
    }

    @Override
    public int tickRate(World worldIn) {
        return 2;
    }

    protected boolean canHangFrom(World worldIn, BlockPos supportPos) {
        IBlockState supportState = worldIn.getBlockState(supportPos);
        return supportState.getBlock() == BlockObjectHolder.chain_top
            || supportState.isSideSolid(worldIn, supportPos, EnumFacing.DOWN);
    }

    protected int getPowerFromAbove(World worldIn, BlockPos pos) {
        BlockPos abovePos = pos.up();
        IBlockState aboveState = worldIn.getBlockState(abovePos);
        int power = worldIn.getRedstonePower(abovePos, EnumFacing.UP);

        if (aboveState.getBlock() == BlockObjectHolder.chain_top && power > 0) {
            power -= MetalVariantHelper.getMetal(worldIn, pos).getChainPowerLoss();
        }

        return Math.max(0, Math.min(15, power));
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP ? ((Integer) state.getValue(POWER)).intValue() : 0;
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return this.getWeakPower(state, blockAccess, pos, side);
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN || side == null;
    }

    protected void notifyPowerNeighbors(World worldIn, BlockPos pos) {
        worldIn.notifyNeighborsOfStateChange(pos, this);
        worldIn.notifyNeighborsOfStateChange(pos.down(), this);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList(MetalVariantHelper.getDrop(this, world, pos));
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
