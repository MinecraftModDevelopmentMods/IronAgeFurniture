package zone.moddev.mc.ironagefurniture.integration.jei;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;
import zone.moddev.mc.ironagefurniture.api.FoudreBrewingRegistry;
import zone.moddev.mc.ironagefurniture.api.FoudreBrewingRegistry.FoudreBrewingRecipe;
import zone.moddev.mc.ironagefurniture.api.PotStillDistillingRegistry;
import zone.moddev.mc.ironagefurniture.api.PotStillDistillingRegistry.DistillationJeiRecipe;
import zone.moddev.mc.ironagefurniture.api.container.ContainerFoudre;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityFoudre;
import zone.moddev.mc.ironagefurniture.client.gui.GuiFoudre;
import zone.moddev.mc.ironagefurniture.client.gui.GuiPotStill;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;

@JEIPlugin
public final class IronAgeFurnitureJeiPlugin extends BlankModPlugin {
	public static final String FOUDRE_BREWING_UID = Ironagefurniture.MODID + ".foudre_brewing";
	public static final String POT_STILL_DISTILLING_UID = Ironagefurniture.MODID + ".pot_still_distilling";

	@Override
	public void register(IModRegistry registry) {
		List<FoudreBrewingRecipe> foudreRecipes = new ArrayList<FoudreBrewingRecipe>(
			FoudreBrewingRegistry.getRecipes());
		List<DistillationJeiRecipe> distillationRecipes = new ArrayList<DistillationJeiRecipe>(
			PotStillDistillingRegistry.getJeiRecipes());
		Set<Fluid> reachableFluids = findReachableFluids(foudreRecipes, distillationRecipes);

		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new FoudreRecipeCategory(guiHelper));
		registry.addRecipeCategories(new PotStillRecipeCategory(guiHelper));
		registry.addRecipeHandlers(new FoudreRecipeHandler());
		registry.addRecipeHandlers(new PotStillRecipeHandler());
		registry.addRecipes(filterFoudreRecipes(foudreRecipes, reachableFluids));
		registry.addRecipes(filterDistillationRecipes(distillationRecipes, reachableFluids));
		hideUnreachableFluidBuckets(registry, reachableFluids);
		registry.addRecipeClickArea(GuiFoudre.class, GuiFoudre.RECIPE_CLICK_X, GuiFoudre.RECIPE_CLICK_Y,
			GuiFoudre.RECIPE_CLICK_WIDTH, GuiFoudre.RECIPE_CLICK_HEIGHT, FOUDRE_BREWING_UID);
		registry.addRecipeClickArea(GuiPotStill.class, GuiPotStill.RECIPE_CLICK_X, GuiPotStill.RECIPE_CLICK_Y,
			GuiPotStill.RECIPE_CLICK_WIDTH, GuiPotStill.RECIPE_CLICK_HEIGHT, POT_STILL_DISTILLING_UID);
		registry.addAdvancedGuiHandlers(new FoudreGuiHandler());

		IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();
		transferRegistry.addRecipeTransferHandler(ContainerFoudre.class, FOUDRE_BREWING_UID, 0,
			TileEntityFoudre.INGREDIENT_SLOTS, TileEntityFoudre.INGREDIENT_SLOTS, 36);

		for (Block foudre : BlockObjectHolder.foudre_wood_ironage.values()) {
			if (foudre != null) {
				registry.addRecipeCategoryCraftingItem(new ItemStack(foudre), FOUDRE_BREWING_UID);
			}
		}

		for (Block potStill : BlockObjectHolder.pot_still_wood_ironage.values()) {
			if (potStill != null) {
				registry.addRecipeCategoryCraftingItem(new ItemStack(potStill), POT_STILL_DISTILLING_UID);
			}
		}
	}

	private static Set<Fluid> findReachableFluids(List<FoudreBrewingRecipe> foudreRecipes,
			List<DistillationJeiRecipe> distillationRecipes) {
		Set<Fluid> reachable = new HashSet<Fluid>();
		reachable.add(FluidRegistry.WATER);
		boolean changed;

		do {
			changed = false;
			for (FoudreBrewingRecipe recipe : foudreRecipes) {
				if (reachable.contains(recipe.getInput())) {
					changed |= reachable.add(recipe.getOutput());
				}
			}
			for (DistillationJeiRecipe recipe : distillationRecipes) {
				FluidStack input = recipe.getInput();
				FluidStack output = recipe.getOutput();

				if (input != null && output != null && reachable.contains(input.getFluid())) {
					changed |= reachable.add(output.getFluid());
				}
			}
		} while (changed);

		return reachable;
	}

	private static List<FoudreBrewingRecipe> filterFoudreRecipes(List<FoudreBrewingRecipe> recipes,
			Set<Fluid> reachableFluids) {
		List<FoudreBrewingRecipe> available = new ArrayList<FoudreBrewingRecipe>();

		for (FoudreBrewingRecipe recipe : recipes) {
			if (reachableFluids.contains(recipe.getInput()) && reachableFluids.contains(recipe.getOutput())) {
				available.add(recipe);
			}
		}

		return available;
	}

	private static List<DistillationJeiRecipe> filterDistillationRecipes(List<DistillationJeiRecipe> recipes,
			Set<Fluid> reachableFluids) {
		List<DistillationJeiRecipe> available = new ArrayList<DistillationJeiRecipe>();

		for (DistillationJeiRecipe recipe : recipes) {
			FluidStack input = recipe.getInput();
			FluidStack output = recipe.getOutput();

			if (input != null && output != null && reachableFluids.contains(input.getFluid())
					&& reachableFluids.contains(output.getFluid())) {
				available.add(recipe);
			}
		}

		return available;
	}

	private static void hideUnreachableFluidBuckets(IModRegistry registry, Set<Fluid> reachableFluids) {
		if (!FluidRegistry.isUniversalBucketEnabled() || ForgeModContainer.getInstance().universalBucket == null) {
			return;
		}

		Set<Fluid> registeredFluids = new HashSet<Fluid>(FoudreBrewingRegistry.getRegisteredFluids());
		registeredFluids.addAll(PotStillDistillingRegistry.getRegisteredSpirits());

		for (Fluid fluid : registeredFluids) {
			if (!reachableFluids.contains(fluid) && FluidRegistry.getBucketFluids().contains(fluid)) {
				registry.getJeiHelpers().getIngredientBlacklist().addIngredientToBlacklist(
					new FluidStack(fluid, Fluid.BUCKET_VOLUME));
				ItemStack bucket = UniversalBucket.getFilledBucket(
					ForgeModContainer.getInstance().universalBucket, fluid);
				registry.getJeiHelpers().getItemBlacklist().addItemToBlacklist(bucket);
			}
		}
	}
}
