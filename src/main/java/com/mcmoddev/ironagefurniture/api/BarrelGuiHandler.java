package com.mcmoddev.ironagefurniture.api;

import com.mcmoddev.ironagefurniture.api.container.ContainerBarrel;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class BarrelGuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		return tileEntity instanceof TileEntityBarrel ? new ContainerBarrel((TileEntityBarrel)tileEntity) : null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		return tileEntity instanceof TileEntityBarrel ? createClientGui((TileEntityBarrel)tileEntity) : null;
	}

	private static Object createClientGui(TileEntityBarrel barrel) {
		try {
			Class<?> guiClass = Class.forName("com.mcmoddev.ironagefurniture.client.gui.GuiBarrel");
			return guiClass.getConstructor(TileEntityBarrel.class).newInstance(barrel);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Unable to open barrel GUI", e);
		}
	}
}
