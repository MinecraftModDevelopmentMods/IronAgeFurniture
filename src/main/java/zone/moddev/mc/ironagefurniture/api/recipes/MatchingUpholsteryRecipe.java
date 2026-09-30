package zone.moddev.mc.ironagefurniture.api.recipes;

import com.google.gson.JsonObject;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/** Combines only two same-colour canopy beds; mixed colours cannot silently lose data. */
public final class MatchingUpholsteryRecipe extends ShapelessOreRecipe {
	private final Item source;
	private final Block result;

	public MatchingUpholsteryRecipe(Block source, Block result) {
		super(new ResourceLocation("ironagefurniture", "matching_upholstery"), new ItemStack(result),
				new ItemStack(source, 1, OreDictionary.WILDCARD_VALUE),
				new ItemStack(source, 1, OreDictionary.WILDCARD_VALUE));
		this.source = Item.getItemFromBlock(source);
		this.result = result;
		// The source beds may carry Color NBT, which the fast ingredient matcher skips.
		this.isSimple = false;
	}

	@Override public boolean matches(InventoryCrafting inventory, World world) {
		if (!super.matches(inventory, world)) return false;
		UpholsteryColour first = null;
		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			ItemStack stack = inventory.getStackInSlot(slot);
			if (stack.isEmpty() || stack.getItem() != source) continue;
			UpholsteryColour colour = UpholsteryColourHelper.getColour(stack);
			if (first != null && first != colour) return false;
			first = colour;
		}
		return first != null;
	}

	@Override public ItemStack getCraftingResult(InventoryCrafting inventory) {
		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			ItemStack stack = inventory.getStackInSlot(slot);
			if (!stack.isEmpty() && stack.getItem() == source)
				return UpholsteryColourHelper.createStack(result, 1, UpholsteryColourHelper.getColour(stack));
		}
		return ItemStack.EMPTY;
	}

	public static final class Factory implements IRecipeFactory {
		@Override public IRecipe parse(JsonContext context, JsonObject json) {
			String sourceName = json.getAsJsonArray("ingredients").get(0)
					.getAsJsonObject().get("item").getAsString();
			String resultName = json.getAsJsonObject("result").get("item").getAsString();
			Block source = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(sourceName));
			Block result = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(resultName));
			if (source == null || result == null)
				throw new IllegalArgumentException("Unknown bed recipe block: " + sourceName + " or " + resultName);
			return new MatchingUpholsteryRecipe(source, result);
		}
	}
}
