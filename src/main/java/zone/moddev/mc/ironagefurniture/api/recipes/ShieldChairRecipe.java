package zone.moddev.mc.ironagefurniture.api.recipes;

import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/** Unlike a fixed-output recipe, the fitted shield keeps its full item data. */
public final class ShieldChairRecipe extends ShapelessOreRecipe {
	private final Block result;

	public ShieldChairRecipe(Block chairIn, Block chairOut) {
		super(new ItemStack(chairOut), new ItemStack(chairIn), new ItemStack(Items.SHIELD, 1, 32767));
		this.result = chairOut;
	}

	@Override public ItemStack getCraftingResult(InventoryCrafting inventory) {
		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			ItemStack stack = inventory.getStackInSlot(slot);
			if (stack != null && stack.getItem() == Items.SHIELD)
				return ShieldChairItemData.createChair(result, stack);
		}
		return null;
	}
}
