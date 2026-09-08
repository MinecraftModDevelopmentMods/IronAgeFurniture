package zone.moddev.mc.ironagefurniture.api.Blocks;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.WeakHashMap;

import com.google.common.collect.Lists;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.CreativeModeBreakTracker;
import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityFallingBlock;
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

public class LightSourceChandelierLava extends LightSourceChandelierGlowstone {
    private static final Map<World, Set<BlockPos>> WATER_LANDINGS = new WeakHashMap<World, Set<BlockPos>>();
    private static final double[][] LAVA_POINTS = new double[][] {
        { 11.0D / 16.0D, 3.5D / 16.0D, 14.0D / 16.0D },
        { 5.0D / 16.0D, 3.5D / 16.0D, 14.0D / 16.0D },
        { 2.0D / 16.0D, 3.5D / 16.0D, 11.0D / 16.0D },
        { 2.0D / 16.0D, 3.5D / 16.0D, 5.0D / 16.0D },
        { 5.0D / 16.0D, 3.5D / 16.0D, 2.0D / 16.0D },
        { 11.0D / 16.0D, 3.5D / 16.0D, 2.0D / 16.0D },
        { 14.0D / 16.0D, 3.5D / 16.0D, 5.0D / 16.0D },
        { 14.0D / 16.0D, 3.5D / 16.0D, 11.0D / 16.0D }
    };

    public LightSourceChandelierLava(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) || isFallingLandingPos(worldIn, pos);
    }

    @Override
    public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack) {
        return super.canReplace(worldIn, pos, side, stack)
            || (stack == null && isFallingLandingPos(worldIn, pos));
    }

    private boolean isFallingLandingPos(World worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos).getMaterial() == Material.WATER) {
            return true;
        }

        return !worldIn.isAirBlock(pos.down())
            && !BlockFalling.canFallThrough(worldIn.getBlockState(pos.down()));
    }

    @Override
    protected void onStartFalling(EntityFallingBlock fallingEntity) {
        super.onStartFalling(fallingEntity);
        fallingEntity.shouldDropItem = false;

        if (!fallingEntity.world.isRemote) {
            BlockPos waterLanding = findWaterLanding(fallingEntity.world, new BlockPos(fallingEntity));

            if (waterLanding != null) {
                rememberWaterLanding(fallingEntity.world, waterLanding);
            }
        }
    }

    @Override
    public void onEndFalling(World worldIn, BlockPos pos) {
        if (worldIn.isRemote) {
            return;
        }

        super.onEndFalling(worldIn, pos);

        if (CreativeModeBreakTracker.shouldSuppressFallingLavaBreak(worldIn, pos)) {
            consumeWaterLanding(worldIn, pos);
            return;
        }

        if (consumeWaterLanding(worldIn, pos) || worldIn.getBlockState(pos).getMaterial() == Material.WATER) {
            breakIntoObsidianChunk(worldIn, pos, null, EnumFacing.NORTH);
            return;
        }

        breakIntoFire(worldIn, pos, null);
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (rand.nextInt(25) == 0) {
            double[] lavaPoint = LAVA_POINTS[rand.nextInt(LAVA_POINTS.length)];
            double x = (double)pos.getX() + lavaPoint[0] + (rand.nextDouble() - 0.5D) * 0.08D;
            double y = (double)pos.getY() + lavaPoint[1] + (rand.nextDouble() - 0.5D) * 0.08D;
            double z = (double)pos.getZ() + lavaPoint[2] + (rand.nextDouble() - 0.5D) * 0.08D;

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

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (player != null && player.capabilities.isCreativeMode) {
            world.setBlockToAir(pos);
            return true;
        }

        boolean silkTouch = player != null && hasSilkTouch(player.getHeldItemMainhand());

        if (!silkTouch) {
            if (!world.isRemote) {
                breakIntoFire(world, pos, player);
            } else {
                world.setBlockToAir(pos);
            }

            return true;
        }

        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
            TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
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
        dropSurvivingMetal(worldIn, pos);

        BlockPos firePos = findFirePosition(worldIn, pos);

        if (firePos.equals(pos)) {
            worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
        } else {
            worldIn.setBlockToAir(pos);
            worldIn.setBlockState(firePos, Blocks.FIRE.getDefaultState(), 3);
        }
    }

    protected void breakIntoObsidianChunk(World worldIn, BlockPos pos, EntityPlayer player, EnumFacing facing) {
        playWaterBreakSounds(worldIn, pos, player);
        dropSurvivingMetal(worldIn, pos);
        worldIn.setBlockState(pos, BlockObjectHolder.obsidian_chunk.getDefaultState()
            .withProperty(BlockHBase.FACING, facing), 3);
    }

    private void dropSurvivingMetal(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote && worldIn.getGameRules().getBoolean("doTileDrops")) {
            spawnAsEntity(worldIn, pos, MetalVariantHelper.getIngotStack(MetalVariantHelper.getMetal(worldIn, pos)));
        }
    }

    private void playWaterBreakSounds(World worldIn, BlockPos pos, EntityPlayer player) {
        worldIn.playSound(player, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
        worldIn.playSound(player, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS,
            0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
    }

    private BlockPos findFirePosition(World worldIn, BlockPos pos) {
        if (Blocks.FIRE.canPlaceBlockAt(worldIn, pos)) {
            return pos;
        }

        BlockPos cursor = pos.down();
        BlockPos lastFallThrough = pos;

        while (cursor.getY() > 0
                && (worldIn.isAirBlock(cursor) || BlockFalling.canFallThrough(worldIn.getBlockState(cursor)))) {
            lastFallThrough = cursor;
            cursor = cursor.down();
        }

        if (lastFallThrough != pos && Blocks.FIRE.canPlaceBlockAt(worldIn, lastFallThrough)) {
            return lastFallThrough;
        }

        return pos;
    }

    private BlockPos findWaterLanding(World world, BlockPos start) {
        BlockPos cursor = start.down();
        BlockPos lastFallThrough = start;
        boolean touchedWater = false;

        while (cursor.getY() > 0
                && (world.isAirBlock(cursor) || BlockFalling.canFallThrough(world.getBlockState(cursor)))) {
            if (world.getBlockState(cursor).getMaterial() == Material.WATER) {
                touchedWater = true;
            }

            lastFallThrough = cursor;
            cursor = cursor.down();
        }

        return touchedWater ? lastFallThrough : null;
    }

    private static void rememberWaterLanding(World world, BlockPos pos) {
        Set<BlockPos> landings = WATER_LANDINGS.get(world);

        if (landings == null) {
            landings = new HashSet<BlockPos>();
            WATER_LANDINGS.put(world, landings);
        }

        landings.add(pos.toImmutable());
    }

    private static boolean consumeWaterLanding(World world, BlockPos pos) {
        Set<BlockPos> landings = WATER_LANDINGS.get(world);
        return landings != null && landings.remove(pos);
    }

    @Override
    protected Block VisibleDropBlock() {
        return BlockObjectHolder.chandelier_lava;
    }
}
