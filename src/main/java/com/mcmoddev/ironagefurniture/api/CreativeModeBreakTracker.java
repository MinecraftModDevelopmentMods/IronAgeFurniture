package com.mcmoddev.ironagefurniture.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.mcmoddev.ironagefurniture.BlockObjectHolder;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CreativeModeBreakTracker {
    private static final long RECENT_BREAK_TICKS = 200L;
    private static final Map<World, List<BreakRecord>> BREAKS = new WeakHashMap<World, List<BreakRecord>>();

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        World world = event.getWorld();

        if (world == null || world.isRemote || player == null || !player.capabilities.isCreativeMode) {
            return;
        }

        if (!isNearLavaFallingLight(world, event.getPos())) {
            return;
        }

        remember(world, event.getPos());
    }

    public static boolean shouldSuppressFallingLavaBreak(World world, BlockPos landingPos) {
        return consumeRecentCreativeBreakAbove(world, landingPos);
    }

    private static void remember(World world, BlockPos pos) {
        List<BreakRecord> breaks = BREAKS.get(world);

        if (breaks == null) {
            breaks = new ArrayList<BreakRecord>();
            BREAKS.put(world, breaks);
        }

        breaks.add(new BreakRecord(pos.toImmutable(), world.getTotalWorldTime()));
    }

    private static boolean consumeRecentCreativeBreakAbove(World world, BlockPos landingPos) {
        List<BreakRecord> breaks = BREAKS.get(world);

        if (breaks == null) {
            return false;
        }

        long now = world.getTotalWorldTime();
        Iterator<BreakRecord> iterator = breaks.iterator();

        while (iterator.hasNext()) {
            BreakRecord record = iterator.next();

            if (now - record.time > RECENT_BREAK_TICKS) {
                iterator.remove();
                continue;
            }

            if (Math.abs(record.pos.getX() - landingPos.getX()) <= 1
                    && Math.abs(record.pos.getZ() - landingPos.getZ()) <= 1
                    && record.pos.getY() >= landingPos.getY()) {
                iterator.remove();
                return true;
            }
        }

        return false;
    }

    private static boolean isNearLavaFallingLight(World world, BlockPos pos) {
        if (isLavaFallingLight(world.getBlockState(pos.up()).getBlock())
                || isLavaFallingLight(world.getBlockState(pos.down()).getBlock())) {
            return true;
        }

        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos neighbourPos = pos.offset(facing);
            IBlockState neighbourState = world.getBlockState(neighbourPos);
            Block neighbourBlock = neighbourState.getBlock();

            if (isLavaFallingLight(neighbourBlock)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isLavaFallingLight(Block block) {
        return block == BlockObjectHolder.light_metal_ironage_block_floor_lava_clear
            || block == BlockObjectHolder.light_metal_ironage_sconce_floor_lava_iron
            || block == BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron
            || block == BlockObjectHolder.chandelier_lava;
    }

    private static class BreakRecord {
        private final BlockPos pos;
        private final long time;

        private BreakRecord(BlockPos pos, long time) {
            this.pos = pos;
            this.time = time;
        }
    }
}
