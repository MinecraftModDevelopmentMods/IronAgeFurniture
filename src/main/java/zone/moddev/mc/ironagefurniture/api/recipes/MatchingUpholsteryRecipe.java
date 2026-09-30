package zone.moddev.mc.ironagefurniture.api.recipes;

import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
	}

	@Override public boolean matches(InventoryCrafting inventory, World world) {
		if (!super.matches(inventory, world)) return false;
		UpholsteryColour first = null;
		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			ItemStack stack = inventory.getStackInSlot(slot);
			if (stack == null || stack.getItem() != source) continue;
			UpholsteryColour colour = UpholsteryColourHelper.getColour(stack);
			if (first != null && first != colour) return false;
			first = colour;
		}
		return first != null;
	}

	@Override public ItemStack getCraftingResult(InventoryCrafting inventory) {
		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			ItemStack stack = inventory.getStackInSlot(slot);
			if (stack != null && stack.getItem() == source)
				return UpholsteryColourHelper.createStack(result, 1, UpholsteryColourHelper.getColour(stack));
		}
		return null;
	}
}
