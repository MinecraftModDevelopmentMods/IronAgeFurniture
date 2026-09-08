package zone.moddev.mc.ironagefurniture.api;

import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.container.ContainerBarrel;
import zone.moddev.mc.ironagefurniture.api.container.ContainerFoudre;
import zone.moddev.mc.ironagefurniture.api.container.ContainerFoudreLabel;
import zone.moddev.mc.ironagefurniture.api.container.ContainerPotStill;
import zone.moddev.mc.ironagefurniture.api.container.ContainerInnkeeper;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityBarrel;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityFoudre;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityPotStill;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityHangingInnSign;

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
		if (id == Ironagefurniture.GUI_INNKEEPER) {
			return tileEntity instanceof TileEntityHangingInnSign
				? new ContainerInnkeeper(player.inventory, (TileEntityHangingInnSign)tileEntity) : null;
		}

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
		if (id == Ironagefurniture.GUI_INNKEEPER) {
			return tileEntity instanceof TileEntityHangingInnSign
				? createClientInnkeeperGui((TileEntityHangingInnSign)tileEntity, player.inventory) : null;
		}

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
			Class<?> guiClass = Class.forName("zone.moddev.mc.ironagefurniture.client.gui.GuiBarrel");
			return guiClass.getConstructor(TileEntityBarrel.class).newInstance(barrel);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Unable to open barrel GUI", e);
		}
	}

	private static Object createClientFoudreGui(TileEntityFoudre foudre, InventoryPlayer playerInventory) {
		try {
			Class<?> guiClass = Class.forName("zone.moddev.mc.ironagefurniture.client.gui.GuiFoudre");
			return guiClass.getConstructor(TileEntityFoudre.class, InventoryPlayer.class)
				.newInstance(foudre, playerInventory);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Unable to open foudre GUI", e);
		}
	}

	private static Object createClientPotStillGui(TileEntityPotStill potStill, InventoryPlayer playerInventory) {
		try {
			Class<?> guiClass = Class.forName("zone.moddev.mc.ironagefurniture.client.gui.GuiPotStill");
			return guiClass.getConstructor(TileEntityPotStill.class, InventoryPlayer.class)
				.newInstance(potStill, playerInventory);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Unable to open pot still GUI", e);
		}
	}

	private static Object createClientLabelGui(TileEntityFoudre foudre) {
		try {
			Class<?> guiClass = Class.forName("zone.moddev.mc.ironagefurniture.client.gui.GuiFoudreLabel");
			return guiClass.getConstructor(TileEntityFoudre.class).newInstance(foudre);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Unable to open foudre label GUI", e);
		}
	}

	private static Object createClientInnkeeperGui(TileEntityHangingInnSign sign, InventoryPlayer playerInventory) {
		try {
			Class<?> guiClass = Class.forName("zone.moddev.mc.ironagefurniture.client.gui.GuiInnkeeper");
			return guiClass.getConstructor(TileEntityHangingInnSign.class, InventoryPlayer.class)
				.newInstance(sign, playerInventory);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Unable to open Innkeeper GUI", e);
		}
	}
}
