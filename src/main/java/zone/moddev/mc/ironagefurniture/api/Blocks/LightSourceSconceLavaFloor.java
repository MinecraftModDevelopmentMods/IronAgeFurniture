package zone.moddev.mc.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
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

public class LightSourceSconceLavaFloor extends LightSourceSconceGlowFloor {
    public LightSourceSconceLavaFloor(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        if (!canPlaceOnTop(worldIn, pos.down())) {
            if (!worldIn.isRemote) {
                spawnAsEntity(worldIn, pos,
                    MetalVariantHelper.getDrop(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron,
                        worldIn, pos));
                IBlockState lavaState = LightDrop().getDefaultState().withProperty(FACING, state.getValue(FACING));
                worldIn.setBlockState(pos, lavaState, 3);
                worldIn.scheduleUpdate(pos, LightDrop(), LightDrop().tickRate(worldIn));
            }

            return;
        }

        super.neighborChanged(state, worldIn, pos, blockIn);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList(MetalVariantHelper.getDrop(
            BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, world, pos));
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (player != null && player.capabilities.isCreativeMode) {
            return super.removedByPlayer(state, world, pos, player, willHarvest);
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
        boolean silkTouch = hasSilkTouch(stack);

        if (silkTouch && !player.capabilities.isCreativeMode && !worldIn.isRemote) {
            spawnAsEntity(worldIn, pos, new ItemStack(LightDrop(), 1));
        }

        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        BlockPos blockpos = pos.up();

        if (world.isAirBlock(blockpos) && !world.getBlockState(blockpos).isFullBlock()) {
            if (rand.nextInt(25) == 0) {
                double x = (double)pos.getX() + 0.5D;
                double y = (double)pos.getY() + 0.72D;
                double z = (double)pos.getZ() + 0.5D;

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

    private boolean canPlaceOnTop(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        return state.isSideSolid(worldIn, pos, EnumFacing.UP)
            || state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
    }

    private boolean hasSilkTouch(ItemStack stack) {
        return stack != null && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
    }

    protected void breakIntoFire(World worldIn, BlockPos pos, EntityPlayer player) {
        worldIn.playSound(player, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
        BlockPos firePos = findFirePosition(worldIn, pos);

        if (firePos.equals(pos)) {
            worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
        } else {
            worldIn.setBlockToAir(pos);
            worldIn.setBlockState(firePos, Blocks.FIRE.getDefaultState(), 3);
        }
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

        if (!lastFallThrough.equals(pos) && Blocks.FIRE.canPlaceBlockAt(worldIn, lastFallThrough)) {
            return lastFallThrough;
        }

        return pos;
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
        return BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron;
    }
}
