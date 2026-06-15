package com.mcmoddev.ironagefurniture.api.Blocks;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

import com.google.common.collect.Lists;
import com.mcmoddev.ironagefurniture.BlockObjectHolder;
import com.mcmoddev.ironagefurniture.api.MetalVariantHelper;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LightSourceSconceRedWall extends LightSourceSconceGlowWall {
    protected static final Map<World, List<Toggle>> RECENT_TOGGLES = new WeakHashMap<World, List<Toggle>>();
    protected static final int RECENT_TOGGLE_TIMER = 60;
    protected static final int MAX_RECENT_TOGGLES = 8;
    protected static final int RESTART_DELAY = 160;

    private final int lightLevel;

    public LightSourceSconceRedWall(Material materialIn, String name, float resistance, float hardness, int lightLevel) {
        super(materialIn, name, resistance, hardness);
        this.lightLevel = lightLevel;
        this.setLightLevel(lightLevel / 15.0F);
    }

    protected int GetLightLevel() {
        return this.lightLevel;
    }

    @Override
    protected Block LightDrop() {
        return BlockObjectHolder.light_metal_ironage_block_floor_red_clear;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        notifyNeighbors(worldIn, pos);
        scheduleUpdateIfNeeded(worldIn, pos, state);
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

        scheduleUpdateIfNeeded(worldIn, pos, state);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int signal = getNeighborSignal(worldIn, pos, state);
        pruneRecentToggles(worldIn);

        if (signal != GetLightLevel()) {
            Block newBlock = getBlockBySignalLevel(signal);
            MetalVariantHelper.replaceBlockPreservingMetal(worldIn, pos,
                newBlock.getDefaultState().withProperty(FACING, state.getValue(FACING)),
                3);

            worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), tickRate(worldIn));

            if (isToggledTooFrequently(worldIn, pos, true)) {
                worldIn.playEvent(1502, pos, 0);
                worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), RESTART_DELAY);
            }
        }
    }

    @Override
    public int tickRate(World worldIn) {
        return 2;
    }

    protected void scheduleUpdateIfNeeded(World worldIn, BlockPos pos, IBlockState state) {
        if ((getNeighborSignal(worldIn, pos, state) > 0 || GetLightLevel() > 0) && !worldIn.isUpdateScheduled(pos, this)) {
            worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
        }
    }

    protected int getNeighborSignal(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing direction = state.getValue(FACING).getOpposite();
        return worldIn.getRedstonePower(pos.offset(direction), direction);
    }

    protected Block getBlockBySignalLevel(int signal) {
        switch (signal) {
            case 1:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_one;
            case 2:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_two;
            case 3:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_three;
            case 4:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_four;
            case 5:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_five;
            case 6:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_six;
            case 7:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_seven;
            case 8:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_eight;
            case 9:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_nine;
            case 10:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_ten;
            case 11:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_eleven;
            case 12:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_twelve;
            case 13:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_thirteen;
            case 14:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_fourteen;
            case 15:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron_fifteen;
            default:
                return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron;
        }
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
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (GetLightLevel() <= 0) {
            return;
        }

        EnumFacing facing = state.getValue(FACING);
        double baseX;
        double baseZ;

        switch (facing) {
            case WEST:
                baseX = 0.4D;
                baseZ = 0.5D;
                break;
            case NORTH:
                baseX = 0.5D;
                baseZ = 0.4D;
                break;
            case SOUTH:
                baseX = 0.5D;
                baseZ = 0.6D;
                break;
            default:
                baseX = 0.6D;
                baseZ = 0.5D;
                break;
        }

        EnumFacing opposite = facing.getOpposite();
        double x = pos.getX() + baseX + (0.27D * opposite.getFrontOffsetX());
        double y = pos.getY() + 1.02D;
        double z = pos.getZ() + baseZ + (0.27D * opposite.getFrontOffsetZ());

        world.spawnParticle(EnumParticleTypes.REDSTONE, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected Block GetWallVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron;
    }

    @Override
    protected Block GetRedVariant() {
        return BlockObjectHolder.light_metal_ironage_sconce_wall_red_iron;
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
