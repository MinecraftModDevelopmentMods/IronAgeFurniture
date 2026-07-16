package com.mcmoddev.ironagefurniture.integration.jei;

import java.awt.Rectangle;
import java.util.List;

import com.mcmoddev.ironagefurniture.client.gui.GuiFoudre;

import mezz.jei.api.gui.IAdvancedGuiHandler;

public final class FoudreGuiHandler implements IAdvancedGuiHandler<GuiFoudre> {
	@Override
	public Class<GuiFoudre> getGuiContainerClass() {
		return GuiFoudre.class;
	}

	@Override
	public List<Rectangle> getGuiExtraAreas(GuiFoudre gui) {
		return gui.getGuiExtraAreas();
	}

	@Override
	public Object getIngredientUnderMouse(GuiFoudre gui, int mouseX, int mouseY) {
		return null;
	}
}
