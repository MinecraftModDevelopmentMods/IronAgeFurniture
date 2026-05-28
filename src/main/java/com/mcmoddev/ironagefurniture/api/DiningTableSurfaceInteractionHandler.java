package com.mcmoddev.ironagefurniture.api;

import com.mcmoddev.ironagefurniture.api.Blocks.DiningTable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DiningTableSurfaceInteractionHandler {
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();

        if (world == null || world.isRemote || player == null || player.isSneaking()) {
            return;
        }

        BlockPos tablePos = event.getPos().down();
        IBlockState tableState = world.getBlockState(tablePos);
        Block tableBlock = tableState.getBlock();

        if (!(tableBlock instanceof DiningTable)) {
            return;
        }

        if (((DiningTable)tableBlock).tryRetrievePlacedBlockAbove(world, tablePos, player, event.getItemStack())) {
            event.setCanceled(true);
        }
    }
}
