package zone.moddev.mc.ironagefurniture.integration.jei;

import java.util.Collections;
import java.util.List;

import zone.moddev.mc.ironagefurniture.api.PotStillDistillingRegistry.DistillationJeiRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public final class PotStillRecipeWrapper extends BlankRecipeWrapper {
	private static final int RECIPE_WIDTH = 150;
	private static final int NAME_Y = 61;
	private static final int RATIO_Y = 72;
	private static final int MINIMUM_Y = 83;
	private static final int TIME_Y = 94;
	private static final int TICKS_PER_SECOND = 20;
	private static final int SECONDS_PER_MINUTE = 60;

	private final DistillationJeiRecipe recipe;
	private final FluidStack input;
	private final FluidStack output;

	public PotStillRecipeWrapper(DistillationJeiRecipe recipe) {
		this.recipe = recipe;
		this.input = recipe.getInput();
		this.output = recipe.getOutput();
	}

	@Override
	public void getIngredients(IIngredients recipeIngredients) {
		recipeIngredients.setInput(FluidStack.class, this.input);
		recipeIngredients.setOutput(FluidStack.class, this.output);
	}

	@Override
	public List<ItemStack> getInputs() {
		return Collections.emptyList();
	}

	@Override
	public List<ItemStack> getOutputs() {
		return Collections.emptyList();
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		return Collections.singletonList(this.input);
	}

	@Override
	public List<FluidStack> getFluidOutputs() {
		return Collections.singletonList(this.output);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		this.drawCenteredTrimmed(minecraft, this.recipe.getOutputName(), NAME_Y);
		this.drawCenteredTrimmed(minecraft,
			I18n.format("gui.ironagefurniture.jei.distillation_ratio", Integer.valueOf(this.recipe.getRatio())),
			RATIO_Y);
		String minimum = I18n.format("gui.ironagefurniture.jei.minimum_charge",
			Integer.valueOf(this.recipe.getMinimumInput()));
		if (this.recipe.acceptsAnyAge()) {
			minimum = minimum + " - " + I18n.format("gui.ironagefurniture.jei.any_age");
		}
		this.drawCenteredTrimmed(minecraft, minimum, MINIMUM_Y);
		this.drawCenteredTrimmed(minecraft,
			I18n.format("gui.ironagefurniture.jei.distill_time", this.getFormattedDistillationTime()), TIME_Y);
	}

	private void drawCenteredTrimmed(Minecraft minecraft, String text, int y) {
		String fitted = minecraft.fontRendererObj.trimStringToWidth(text, RECIPE_WIDTH);
		int x = (RECIPE_WIDTH - minecraft.fontRendererObj.getStringWidth(fitted)) / 2;
		minecraft.fontRendererObj.drawString(fitted, x, y, 0x404040);
	}

	private String getFormattedDistillationTime() {
		int ticks = this.recipe.getDistillationTime();
		int seconds = Math.max(1, (ticks + TICKS_PER_SECOND - 1) / TICKS_PER_SECOND);
		int minutes = seconds / SECONDS_PER_MINUTE;
		int remainingSeconds = seconds % SECONDS_PER_MINUTE;

		if (minutes <= 0) {
			return Integer.toString(seconds) + " sec";
		}

		if (remainingSeconds <= 0) {
			return Integer.toString(minutes) + " min";
		}

		return Integer.toString(minutes) + " min " + Integer.toString(remainingSeconds) + " sec";
	}
}
