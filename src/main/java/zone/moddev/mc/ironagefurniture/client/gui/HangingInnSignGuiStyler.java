package zone.moddev.mc.ironagefurniture.client.gui;

import zone.moddev.mc.ironagefurniture.api.tile.TileEntityHangingInnSign;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HangingInnSignGuiStyler {
	private static final int DONE_BUTTON_ID = 0;
	private static final int ACTIVE_TEXT_COLOR = 0xFFFFFF;

	@SubscribeEvent
	public void onGuiInitialised(GuiScreenEvent.InitGuiEvent.Post event) {
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

		if (!(sign instanceof TileEntityHangingInnSign)) {
			return;
		}

		for (GuiButton button : event.getButtonList()) {
			if (button.id == DONE_BUTTON_ID) {
				button.packedFGColour = ACTIVE_TEXT_COLOR;
				return;
			}
		}
	}
}
