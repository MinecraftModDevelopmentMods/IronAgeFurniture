package com.mcmoddev.ironagefurniture.api;

import com.mcmoddev.ironagefurniture.Ironagefurniture;
import com.mcmoddev.ironagefurniture.api.container.ContainerBarrel;
import com.mcmoddev.ironagefurniture.api.container.ContainerFoudre;
import com.mcmoddev.ironagefurniture.api.container.ContainerFoudreLabel;
import com.mcmoddev.ironagefurniture.api.container.ContainerPotStill;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityBarrel;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityFoudre;
import com.mcmoddev.ironagefurniture.api.tile.TileEntityPotStill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class BarrelGuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (id == Ironagefurniture.GUI_FOUDRE_LABEL) {
			return tileEntity instanceof TileEntityFoudre ? new ContainerFoudreLabel((TileEntityFoudre)tileEntity)
				: null;
		}

		if (id == Ironagefurniture.GUI_POT_STILL) {
			return tileEntity instanceof TileEntityPotStill
				? new ContainerPotStill(player.inventory, (TileEntityPotStill)tileEntity) : null;
		}

		if (tileEntity instanceof TileEntityFoudre) {
			return new ContainerFoudre(player.inventory, (TileEntityFoudre)tileEntity);
		}

		return tileEntity instanceof TileEntityBarrel ? new ContainerBarrel((TileEntityBarrel)tileEntity) : null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (id == Ironagefurniture.GUI_FOUDRE_LABEL) {
			return tileEntity instanceof TileEntityFoudre ? createClientLabelGui((TileEntityFoudre)tileEntity) : null;
		}

		if (id == Ironagefurniture.GUI_POT_STILL) {
			return tileEntity instanceof TileEntityPotStill
				? createClientPotStillGui((TileEntityPotStill)tileEntity, player.inventory) : null;
		}

		if (tileEntity instanceof TileEntityFoudre) {
			return createClientFoudreGui((TileEntityFoudre)tileEntity, player.inventory);
		}

		return tileEntity instanceof TileEntityBarrel ? createClientBarrelGui((TileEntityBarrel)tileEntity) : null;
	}

	private static Object createClientBarrelGui(TileEntityBarrel barrel) {
		try {
			Class<?> guiClass = Class.forName("com.mcmoddev.ironagefurniture.client.gui.GuiBarrel");
			return guiClass.getConstructor(TileEntityBarrel.class).newInstance(barrel);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Unable to open barrel GUI", e);
		}
	}

	private static Object createClientFoudreGui(TileEntityFoudre foudre, InventoryPlayer playerInventory) {
		try {
			Class<?> guiClass = Class.forName("com.mcmoddev.ironagefurniture.client.gui.GuiFoudre");
			return guiClass.getConstructor(TileEntityFoudre.class, InventoryPlayer.class)
				.newInstance(foudre, playerInventory);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Unable to open foudre GUI", e);
		}
	}

	private static Object createClientPotStillGui(TileEntityPotStill potStill, InventoryPlayer playerInventory) {
		try {
			Class<?> guiClass = Class.forName("com.mcmoddev.ironagefurniture.client.gui.GuiPotStill");
			return guiClass.getConstructor(TileEntityPotStill.class, InventoryPlayer.class)
				.newInstance(potStill, playerInventory);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Unable to open pot still GUI", e);
		}
	}

	private static Object createClientLabelGui(TileEntityFoudre foudre) {
		try {
			Class<?> guiClass = Class.forName("com.mcmoddev.ironagefurniture.client.gui.GuiFoudreLabel");
			return guiClass.getConstructor(TileEntityFoudre.class).newInstance(foudre);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Unable to open foudre label GUI", e);
		}
	}
}
