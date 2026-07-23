package com.mcmoddev.ironagefurniture.api;

import com.mcmoddev.ironagefurniture.api.Blocks.DiningTable;
import com.mcmoddev.ironagefurniture.api.Blocks.Cabinet;
import com.mcmoddev.ironagefurniture.api.Blocks.SurfaceDisplayBlocker;
import com.mcmoddev.ironagefurniture.api.Blocks.WallShelf;
import com.mcmoddev.ironagefurniture.api.surface.SurfaceSetting;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DiningTableSurfaceInteractionHandler {
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();

        if (world == null || player == null || player.isSneaking()) {
            return;
        }

        if (!world.isRemote) {
            BlockPos tablePos = event.getPos().down();
            IBlockState tableState = world.getBlockState(tablePos);
            Block tableBlock = tableState.getBlock();

            if (tableBlock instanceof DiningTable
                    && ((DiningTable)tableBlock).tryRetrievePlacedBlockAbove(world, tablePos, player,
                        event.getItemStack())) {
                event.setCanceled(true);
                return;
            }

            if (tableBlock instanceof Cabinet
                    && ((Cabinet)tableBlock).tryRetrievePlacedBlockAbove(world, tablePos, player,
                        event.getItemStack())) {
                event.setCanceled(true);
                return;
            }
        }

        if (event.getFace() != EnumFacing.UP) {
            return;
        }

        BlockPos surfacePos = event.getPos();
        IBlockState surfaceState = world.getBlockState(surfacePos);
        Block surfaceBlock = surfaceState.getBlock();
        ItemStack heldItem = event.getItemStack();

        if (!SurfaceSetting.isSettingItem(heldItem)
                || surfaceBlock instanceof DiningTable
                || surfaceBlock instanceof Cabinet
                || surfaceBlock instanceof WallShelf
                || surfaceBlock instanceof SurfaceDisplayBlocker
                || !surfaceState.isSideSolid(world, surfacePos, EnumFacing.UP)
                || !player.canPlayerEdit(surfacePos.up(), EnumFacing.UP, heldItem)) {
            return;
        }

        Vec3d hit = event.getHitVec();
        float hitX = hit == null ? 0.5F : (float)(hit.xCoord - surfacePos.getX());
        float hitZ = hit == null ? 0.5F : (float)(hit.zCoord - surfacePos.getZ());

        if (SurfaceDisplayBlocker.placeSurfaceItem(world, surfacePos, heldItem,
                player.getHorizontalFacing(), hitX, hitZ)) {
            if (!world.isRemote && !player.capabilities.isCreativeMode) {
                heldItem.stackSize--;

                if (heldItem.stackSize <= 0) {
                    player.setHeldItem(event.getHand(), null);
                }
            }

            event.setCanceled(true);
        }
    }
}
