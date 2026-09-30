package zone.moddev.mc.ironagefurniture.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.FurnitureFactory;

/** Adds Phase 4 chairs only for woods the existing integration has registered. */
public final class PhaseFourFurnitureInitialiser {
	private static final String CLASSIC_PREFIX = "chair_wood_ironage_classic_";

	private PhaseFourFurnitureInitialiser() { }

	public static void registerChairs() {
		if (!IronAgeFurnitureConfiguration.GENERATE_WINGBACK_CHAIRS) return;
		List<String> registered = new ArrayList<String>(Ironagefurniture.BlockRegistry.keySet());
		Collections.sort(registered);
		for (String name : registered) {
			if (!name.startsWith(CLASSIC_PREFIX)) continue;
			String wood = name.substring(CLASSIC_PREFIX.length());
			FurnitureFactory.CreateWoodWingbackChair("chair_wood_ironage_wingback_" + wood);
			if (IronAgeFurnitureConfiguration.GENERATE_THRONES)
				FurnitureFactory.CreateWoodThroneChair("chair_wood_ironage_throne_" + wood);
		}
	}
}
