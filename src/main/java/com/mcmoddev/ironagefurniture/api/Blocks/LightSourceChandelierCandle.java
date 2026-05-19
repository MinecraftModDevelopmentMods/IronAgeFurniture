package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.Random;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.client.particle.CandleFlameParticle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LightSourceChandelierCandle extends BlockFalling {
    private static final AxisAlignedBB AABB = new AxisAlignedBB(
        0.0D, 2.0D / 16.0D, 0.0D,
        1.0D, 1.0D, 1.0D
    );

    private static final double[][] CANDLE_POINTS = new double[][] {
        { 3.5D / 16.0D, 3.5D / 16.0D },
        { 12.5D / 16.0D, 3.5D / 16.0D },
        { 12.5D / 16.0D, 12.5D / 16.0D },
        { 3.5D / 16.0D, 12.5D / 16.0D },
        rotate45(3.5D / 16.0D, 3.5D / 16.0D),
        rotate45(12.5D / 16.0D, 3.5D / 16.0D),
        rotate45(12.5D / 16.0D, 12.5D / 16.0D),
        rotate45(3.5D / 16.0D, 12.5D / 16.0D)
    };

    public LightSourceChandelierCandle(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn);
        this.setSoundType(SoundType.METAL);
        this.blockResistance = resistance;
        this.blockHardness = hardness;
        this.setCreativeTab(Ironagefurniture.ironagefurnitureTab);
        this.setLightLevel(1.0F);
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
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            checkFallable(worldIn, pos);
        }
    }

    private void checkFallable(World worldIn, BlockPos pos) {
        if (!shouldFall(worldIn, pos)) {
            return;
        }

        int range = 32;

        if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-range, -range, -range), pos.add(range, range, range))) {
            EntityFallingBlock fallingBlock = new EntityFallingBlock(worldIn, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, worldIn.getBlockState(pos));
            this.onStartFalling(fallingBlock);
            worldIn.spawnEntity(fallingBlock);
            return;
        }

        IBlockState state = worldIn.getBlockState(pos);
        worldIn.setBlockToAir(pos);
        BlockPos landingPos;

        for (landingPos = pos.down();
                (worldIn.isAirBlock(landingPos) || BlockFalling.canFallThrough(worldIn.getBlockState(landingPos))) && landingPos.getY() > 0;
                landingPos = landingPos.down()) {
            ;
        }

        if (landingPos.getY() > 0) {
            worldIn.setBlockState(landingPos.up(), state);
        }
    }

    private boolean shouldFall(World worldIn, BlockPos pos) {
        return (worldIn.isAirBlock(pos.down()) || BlockFalling.canFallThrough(worldIn.getBlockState(pos.down())))
            && !canHangFrom(worldIn, pos.up())
            && pos.getY() >= 0;
    }

    protected boolean canHangFrom(World worldIn, BlockPos supportPos) {
        IBlockState supportState = worldIn.getBlockState(supportPos);
        return supportState.getBlock() == BlockObjectHolder.chain_top
            || supportState.isSideSolid(worldIn, supportPos, EnumFacing.DOWN);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
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
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        for (double[] candlePoint : CANDLE_POINTS) {
            double x = pos.getX() + candlePoint[0];
            double y = pos.getY() + 8.15D / 16.0D;
            double z = pos.getZ() + candlePoint[1];

            if (rand.nextInt(3) == 0) {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y + 0.04D, z, 0.0D, 0.0D, 0.0D);
            }

            CandleFlameParticle.spawn(world, x, y, z);
        }
    }

    private static double[] rotate45(double x, double z) {
        double center = 0.5D;
        double dx = x - center;
        double dz = z - center;
        double sin = Math.sin(Math.PI / 4.0D);
        double cos = Math.cos(Math.PI / 4.0D);

        return new double[] {
            center + dx * cos - dz * sin,
            center + dx * sin + dz * cos
        };
    }
}
