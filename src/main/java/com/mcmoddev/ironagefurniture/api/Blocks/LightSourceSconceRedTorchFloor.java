package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

import com.google.common.collect.Lists;
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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LightSourceSconceRedTorchFloor extends LightSourceSconceTorchFloor {
    protected static final Map<World, List<Toggle>> RECENT_TOGGLES = new WeakHashMap<World, List<Toggle>>();
    protected static final int RECENT_TOGGLE_TIMER = 60;
    protected static final int MAX_RECENT_TOGGLES = 8;
    protected static final int RESTART_DELAY = 160;

    public LightSourceSconceRedTorchFloor(Material materialIn, String name, float resistance, float hardness) {
        super(materialIn, name, resistance, hardness);
        this.setLightLevel(8.0F / 15.0F);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = Lists.newArrayList();
        drops.add(new ItemStack(BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron, 1));
        drops.add(new ItemStack(Blocks.REDSTONE_TORCH, 1));
        return drops;
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (!HasFlame()) {
            return;
        }

        double x = pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
        double y = pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
        double z = pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
        world.spawnParticle(EnumParticleTypes.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
                                    EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if (heldItem == null || heldItem.stackSize <= 0) {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
        }

        if (heldItem.getItem() == Items.WATER_BUCKET) {
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos,
                    GetUnlitTorchVariant().getDefaultState().withProperty(FACING, state.getValue(FACING)),
                    3);
            }

            return true;
        }

        Block newBlock = null;

        if (heldItem.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
            newBlock = GetTorchVariant();
        }
        else if (heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_glow_clear)) {
            newBlock = GetGlowVariant();
        }
        else if (heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_lava_clear)) {
            newBlock = GetLavaVariant();
        }
        else if (heldItem.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)) {
            newBlock = DropVariant();
        }
        else if (heldItem.getItem() == Item.getItemFromBlock(BlockObjectHolder.light_metal_ironage_block_floor_red_clear)) {
            newBlock = GetRedVariant();
        }

        if (newBlock == null) {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
        }

        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, newBlock.getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
        }

        if (!playerIn.capabilities.isCreativeMode
            && heldItem.getItem() != Item.getItemFromBlock(Blocks.REDSTONE_TORCH)) {
            heldItem.stackSize--;
            playerIn.inventory.addItemStackToInventory(new ItemStack(LightDrop(), 1));
        }

        return true;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        notifyNeighbors(worldIn, pos);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        notifyNeighbors(worldIn, pos);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        super.neighborChanged(state, worldIn, pos, blockIn);

        if (worldIn.getBlockState(pos).getBlock() != this) {
            return;
        }

        if (hasNeighborSignal(worldIn, pos, state) && !worldIn.isUpdateScheduled(pos, this)) {
            worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean hasSignal = hasNeighborSignal(worldIn, pos, state);
        pruneRecentToggles(worldIn);

        if (!hasSignal) {
            return;
        }

        worldIn.setBlockState(pos,
            GetUnlitTorchVariant().getDefaultState().withProperty(FACING, state.getValue(FACING)),
            3);

        if (isToggledTooFrequently(worldIn, pos, true)) {
            worldIn.playEvent(1502, pos, 0);
            worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), RESTART_DELAY);
        }
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side != EnumFacing.UP ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? this.getWeakPower(state, blockAccess, pos, side) : 0;
    }

    @Override
    public int tickRate(World worldIn) {
        return 2;
    }

    protected boolean hasNeighborSignal(World worldIn, BlockPos pos, IBlockState state) {
        return worldIn.isSidePowered(pos.down(), EnumFacing.DOWN);
    }

    protected void pruneRecentToggles(World worldIn) {
        List<Toggle> list = RECENT_TOGGLES.get(worldIn);

        while (list != null && !list.isEmpty() && worldIn.getTotalWorldTime() - list.get(0).when > RECENT_TOGGLE_TIMER) {
            list.remove(0);
        }
    }

    protected boolean isToggledTooFrequently(World worldIn, BlockPos pos, boolean addNewToggle) {
        List<Toggle> list = RECENT_TOGGLES.get(worldIn);

        if (list == null) {
            list = Lists.newArrayList();
            RECENT_TOGGLES.put(worldIn, list);
        }

        if (addNewToggle) {
            list.add(new Toggle(pos, worldIn.getTotalWorldTime()));
        }

        int toggleCount = 0;

        for (Toggle toggle : list) {
            if (!toggle.pos.equals(pos)) {
                continue;
            }

            toggleCount++;

            if (toggleCount >= MAX_RECENT_TOGGLES) {
                return true;
            }
        }

        return false;
    }

    protected void notifyNeighbors(World worldIn, BlockPos pos) {
        for (EnumFacing direction : EnumFacing.values()) {
            worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
        }
    }

    @Override
    protected Block LightDrop() {
        return Blocks.REDSTONE_TORCH;
    }

    @Override
    protected boolean CanEx() {
        return false;
    }

    @Override
    protected Block GetWallVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_redtorch_iron;
    }

    @Override
    protected Block GetRedTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron;
    }

    @Override
    protected Block GetUnlitTorchVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_floor_redtorch_iron_unlit;
    }

    protected static class Toggle {
        final BlockPos pos;
        final long when;

        Toggle(BlockPos pos, long when) {
            this.pos = pos;
            this.when = when;
        }
    }
}
