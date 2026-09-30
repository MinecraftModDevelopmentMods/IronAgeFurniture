package zone.moddev.mc.ironagefurniture.api.recipes;

import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.UpholsteryColourHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/** One bed and one carpet recolour that same bed without changing its wood or size. */
public final class BedRecolourRecipe extends ShapelessOreRecipe {
	private final Block bed;
	private final Item carpet = Item.getItemFromBlock(Blocks.CARPET);

	public BedRecolourRecipe(Block bed) {
		super(new ResourceLocation("ironagefurniture", "bed_recolour"), new ItemStack(bed),
				new ItemStack(bed, 1, OreDictionary.WILDCARD_VALUE),
				new ItemStack(Blocks.CARPET, 1, OreDictionary.WILDCARD_VALUE));
		this.bed = bed;
	}

	@Override public boolean matches(InventoryCrafting inventory, World world) {
		return super.matches(inventory, world) && carpetColour(inventory) != null;
	}

	@Override public ItemStack getCraftingResult(InventoryCrafting inventory) {
		UpholsteryColour colour = carpetColour(inventory);
		return colour == null ? null : UpholsteryColourHelper.createStack(bed, 1, colour);
	}

	private UpholsteryColour carpetColour(InventoryCrafting inventory) {
		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			ItemStack stack = inventory.getStackInSlot(slot);
			if (stack != null && stack.getItem() == carpet) {
				int metadata = stack.getMetadata();
				return metadata >= 0 && metadata < 16
						? UpholsteryColour.byCarpetMetadata(metadata) : null;
			}
		}
		return null;
	}
}
