package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper.MetalVariant;
import com.mcmoddev.ironagefurniture.api.entity.EntityFallingMetalBlock;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityMetalVariant;
import com.mcmoddev.ironagefurniture.client.particle.CandleFlameParticle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class LightSourceChandelierCandle extends BlockFalling implements ITileEntityProvider {
    private static final AxisAlignedBB DISC_AABB = new AxisAlignedBB(
        0.0D, 2.0D / 16.0D, 0.0D,
        1.0D, 5.0D / 16.0D, 1.0D
    );
    private static final AxisAlignedBB INTERACTION_AABB = new AxisAlignedBB(
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
        this.setDefaultState(this.blockState.getBaseState().withProperty(MetalVariantHelper.METAL, MetalVariant.IRON));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { MetalVariantHelper.METAL });
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
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
    public TileEntity createNewTileEntity(World worldIn, int meta) {
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

    protected void checkFallable(World worldIn, BlockPos pos) {
        if (!shouldFall(worldIn, pos)) {
            return;
        }

        int range = 32;

        IBlockState sourceState = worldIn.getBlockState(pos)
            .withProperty(MetalVariantHelper.METAL, MetalVariantHelper.getMetal(worldIn, pos));
        IBlockState state = getFallingState(worldIn, pos, sourceState);

        if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-range, -range, -range), pos.add(range, range, range))) {
            EntityFallingBlock fallingBlock = new EntityFallingMetalBlock(worldIn,
                pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, state);
            this.onStartFalling(fallingBlock);
            worldIn.spawnEntity(fallingBlock);
            return;
        }

        worldIn.setBlockToAir(pos);
        BlockPos landingPos;

        for (landingPos = pos.down();
                (worldIn.isAirBlock(landingPos) || BlockFalling.canFallThrough(worldIn.getBlockState(landingPos))) && landingPos.getY() > 0;
                landingPos = landingPos.down()) {
            ;
        }

        if (landingPos.getY() > 0) {
            worldIn.setBlockState(landingPos.up(), state);
            MetalVariantHelper.setMetal(worldIn, landingPos.up(), state.getValue(MetalVariantHelper.METAL));
        }
    }

    protected IBlockState getFallingState(World worldIn, BlockPos pos, IBlockState state) {
        return state;
    }

    @Override
    protected void onStartFalling(EntityFallingBlock fallingEntity) {
        IBlockState state = fallingEntity.getBlock();

        if (state != null && state.getProperties().containsKey(MetalVariantHelper.METAL)) {
            if (fallingEntity.tileEntityData == null) {
                fallingEntity.tileEntityData = new NBTTagCompound();
            }

            fallingEntity.tileEntityData.setString("Metal",
                state.getValue(MetalVariantHelper.METAL).getName());
        }
    }

    @Override
    public void onEndFalling(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote) {
            IBlockState state = worldIn.getBlockState(pos);
            worldIn.notifyBlockUpdate(pos, state, state, 3);
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
            EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
            EnumFacing side, float hitX, float hitY, float hitZ) {
        if (heldItem != null && heldItem.stackSize > 0 && heldItem.getItem() == Items.WATER_BUCKET) {
            if (!worldIn.isRemote) {
                MetalVariantHelper.replaceBlockPreservingMetal(worldIn, pos, GetUnlitVariant().getDefaultState(), 3);
            }

            return true;
        }

        return false;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList(MetalVariantHelper.getDrop(VisibleDropBlock(), world, pos));
    }

    @Override
    public int damageDropped(IBlockState state) {
        if (state != null && state.getProperties().containsKey(MetalVariantHelper.METAL)) {
            return state.getValue(MetalVariantHelper.METAL).getMeta();
        }

        return 0;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return INTERACTION_AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return DISC_AABB;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos,
            AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
        super.addCollisionBoxToList(pos, entityBox, collidingBoxes, DISC_AABB);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return DISC_AABB.offset(pos);
    }

    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(IBlockState state, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return this.rayTrace(pos, start, end, INTERACTION_AABB);
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
        if (!IsLit()) {
            return;
        }

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

    protected boolean IsLit() {
        return true;
    }

    protected Block GetUnlitVariant() {
        return BlockObjectHolder.chandelier_candle_unlit;
    }

    protected Block GetLitVariant() {
        return BlockObjectHolder.chandelier_candle;
    }

    protected Block VisibleDropBlock() {
        return BlockObjectHolder.chandelier_candle;
    }
}
