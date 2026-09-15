package zone.moddev.mc.ironagefurniture.client.gui;

import zone.moddev.mc.ironagefurniture.api.tile.TileEntityHangingInnSign;

import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HangingInnSignGuiStyler {
	@SubscribeEvent
	public void onGuiOpened(GuiOpenEvent event) {
		if (!(event.getGui() instanceof GuiEditSign)) {
			return;
		}

		TileEntitySign sign;

		try {
			sign = ReflectionHelper.getPrivateValue(GuiEditSign.class, (GuiEditSign)event.getGui(),
				"tileSign", "field_146848_f");
		} catch (RuntimeException ignored) {
			return;
		}

		if (sign instanceof TileEntityHangingInnSign) {
			event.setGui(new GuiHangingInnSign((TileEntityHangingInnSign)sign));
		}
	}
}
