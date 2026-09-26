package zone.moddev.mc.ironagefurniture.init;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.FurnitureFactory;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/** Phase 4 registrations are additive to the Phase 3 furniture catalog. */
public final class PhaseFourFurnitureInitialiser {
	private PhaseFourFurnitureInitialiser() {
	}

	public static void registerBlocks() {
		for (String suffix : WoodVariantHelper.getEnabledWoodSuffixes()) {
			if (IronAgeFurnitureConfiguration.GENERATE_CANOPY_BEDS) {
				BlockObjectHolder.bed_canopy_single.put(suffix, FurnitureFactory.CreateSingleCanopyBed(suffix));
				Block[] doubleBed = FurnitureFactory.CreateDoubleCanopyBed(suffix);
				BlockObjectHolder.bed_canopy_double_left.put(suffix, doubleBed[0]);
				BlockObjectHolder.bed_canopy_double_right.put(suffix, doubleBed[1]);
			}
			if (IronAgeFurnitureConfiguration.GENERATE_WOOD_BEDS) {
				BlockObjectHolder.bed_wood_single.put(suffix, FurnitureFactory.CreateSingleWoodBed(suffix));
				BlockObjectHolder.bed_wood_double.put(suffix, FurnitureFactory.CreateDoubleWoodBed(suffix));
			}
			if (!IronAgeFurnitureConfiguration.GENERATE_CLASSIC_CHAIRS
					|| !IronAgeFurnitureConfiguration.GENERATE_WINGBACK_CHAIRS
					|| !Ironagefurniture.BlockRegistry.containsKey("chair_wood_ironage_classic_" + suffix)) {
				continue;
			}
			BlockObjectHolder.chair_wood_ironage_wingback.put(suffix,
				FurnitureFactory.CreateWoodWingbackChair("chair_wood_ironage_wingback_" + suffix));
			if (IronAgeFurnitureConfiguration.GENERATE_THRONES) {
				BlockObjectHolder.chair_wood_ironage_throne.put(suffix,
					FurnitureFactory.CreateWoodThroneChair("chair_wood_ironage_throne_" + suffix));
			}
		}
	}

	public static void registerRecipes() {
		WoodVariantHelper.forEachEnabledPlankVariant(new WoodVariantHelper.ItemStackVariantConsumer() {
			@Override
			public void accept(String suffix, ItemStack planks) {
				Block wood = BlockObjectHolder.bed_wood_single.get(suffix);
				if (wood != null) {
					FurnitureFactory.AddSingleWoodBedRecipe(planks, wood);
					FurnitureFactory.AddBedRecolourRecipe(wood);
				}
				Block canopy = BlockObjectHolder.bed_canopy_single.get(suffix);
				if (canopy != null) {
					FurnitureFactory.AddSingleCanopyBedRecipe(planks, canopy);
					FurnitureFactory.AddBedRecolourRecipe(canopy);
				}
			}
		});
		for (String suffix : WoodVariantHelper.getEnabledWoodSuffixes()) {
			Block woodSingle = BlockObjectHolder.bed_wood_single.get(suffix);
			Block woodDouble = BlockObjectHolder.bed_wood_double.get(suffix);
			if (woodSingle != null && woodDouble != null) {
				FurnitureFactory.AddDoubleWoodBedRecipe(woodSingle, woodDouble);
				FurnitureFactory.AddBedRecolourRecipe(woodDouble);
			}
			Block canopySingle = BlockObjectHolder.bed_canopy_single.get(suffix);
			Block canopyDouble = BlockObjectHolder.bed_canopy_double_left.get(suffix);
			if (canopySingle != null && canopyDouble != null) {
				FurnitureFactory.AddDoubleCanopyBedRecipe(canopySingle, canopyDouble);
				FurnitureFactory.AddBedRecolourRecipe(canopyDouble);
			}
		}
	}
}
