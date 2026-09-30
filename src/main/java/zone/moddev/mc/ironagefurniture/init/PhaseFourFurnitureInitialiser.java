package zone.moddev.mc.ironagefurniture.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.FurnitureFactory;
import net.minecraftforge.fml.common.Loader;

/** Adds Phase 4 chairs only for woods the existing integration has registered. */
public final class PhaseFourFurnitureInitialiser {
	private static final String CLASSIC_PREFIX = "chair_wood_ironage_classic_";
	private static final String[] VANILLA_WOODS = {
		"oak", "acacia", "big_oak", "birch", "jungle", "spruce"
	};
	private static final String[] BOP_WOODS = {
		"cherry", "ebony", "ethereal", "eucalyptus", "fir", "hellbark", "jacaranda", "magic",
		"mahogany", "mangrove", "palm", "pine", "redwood", "sacred_oak", "umbran", "willow"
	};
	private static final String[] NATURA_WOODS = {
		"amaranth", "bloodwood", "darkwood", "eucalyptus", "fusewood", "ghostwood",
		"hopseed", "maple", "sakura", "silverbell", "tiger", "willow"
	};
	private static final String[] FORESTRY_WOODS = {
		"acacia", "balsa", "baobab", "cherry", "chestnut", "citrus", "cocobolo", "ebony",
		"giganteum", "greenheart", "ipe", "kapok", "larch", "lime", "mahoe", "mahogany",
		"maple", "padauk", "palm", "papaya", "pine", "plum", "poplar", "sequoia", "teak",
		"walnut", "wenge", "willow", "zebrawood"
	};

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

	public static void registerBeds() {
		if (!IronAgeFurnitureConfiguration.GENERATE_CANOPY_BEDS
				&& !IronAgeFurnitureConfiguration.GENERATE_WOOD_BEDS) return;
		List<String> woods = new ArrayList<String>();
		Collections.addAll(woods, VANILLA_WOODS);
		addOptionalWoods(woods, "biomesoplenty", "biomesoplenty", BOP_WOODS,
				IronAgeFurnitureConfiguration.INTEGRATION_BIOMESOPLENTY);
		addOptionalWoods(woods, "natura", "natura", NATURA_WOODS,
				IronAgeFurnitureConfiguration.INTEGRATION_NATURA);
		addOptionalWoods(woods, "forestry", "forestry", FORESTRY_WOODS,
				IronAgeFurnitureConfiguration.INTEGRATION_FORESTRY);
		if (IronAgeFurnitureConfiguration.INTEGRATION_IMMERSIVEENGINEERING
				&& Loader.isModLoaded("immersiveengineering"))
			woods.add("immersiveengineering_treated_wood");
		for (String wood : woods) {
			if (IronAgeFurnitureConfiguration.GENERATE_CANOPY_BEDS) {
				FurnitureFactory.CreateSingleCanopyBed(wood);
				FurnitureFactory.CreateDoubleCanopyBed(wood);
			}
			if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BEDS) {
				FurnitureFactory.CreateSingleWoodBed(wood);
				FurnitureFactory.CreateDoubleWoodBed(wood);
			}
		}
	}

	private static void addOptionalWoods(List<String> woods, String modId, String prefix,
			String[] names, boolean enabled) {
		if (!enabled || !Loader.isModLoaded(modId)) return;
		for (String name : names) woods.add(prefix + "_" + name);
	}
}
