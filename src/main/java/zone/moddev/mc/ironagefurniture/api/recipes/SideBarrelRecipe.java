package zone.moddev.mc.ironagefurniture.api.recipes;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class SideBarrelRecipe implements IRecipe {
	private final Block sourceBarrel;
	private final Block sideBarrel;
	private final ItemStack output;

	public SideBarrelRecipe(Block sourceBarrel, Block sideBarrel) {
		this.sourceBarrel = sourceBarrel;
		this.sideBarrel = sideBarrel;
		this.output = new ItemStack(sideBarrel, 1);
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		return this.findBarrelStack(inv) != null;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack barrelStack = this.findBarrelStack(inv);

		if (barrelStack == null) {
			return null;
		}

		ItemStack result = new ItemStack(this.sideBarrel, 1);
		if (barrelStack.hasTagCompound()) {
			result.setTagCompound((NBTTagCompound)barrelStack.getTagCompound().copy());
		}
		return result;
	}

	@Override
	public int getRecipeSize() {
		return 3;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.output;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		ItemStack[] remaining = new ItemStack[inv.getSizeInventory()];

		for (int i = 0; i < remaining.length; i++) {
			ItemStack stack = inv.getStackInSlot(i);

			if (stack != null && stack.getItem().hasContainerItem(stack)) {
				remaining[i] = stack.getItem().getContainerItem(stack);
			}
		}

		return remaining;
	}

	private ItemStack findBarrelStack(InventoryCrafting inv) {
		ItemStack barrelStack = null;
		int stickCount = 0;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);

			if (stack == null || stack.stackSize <= 0) {
				continue;
			}

			if (stack.getItem() == Item.getItemFromBlock(this.sourceBarrel)) {
				if (barrelStack != null) {
					return null;
				}
				barrelStack = stack;
			} else if (this.isWoodStick(stack)) {
				stickCount++;
			} else {
				return null;
			}
		}

		return barrelStack != null && stickCount == 2 ? barrelStack : null;
	}

	private boolean isWoodStick(ItemStack stack) {
		if (stack.getItem() == Items.STICK) {
			return true;
		}

		for (int id : OreDictionary.getOreIDs(stack)) {
			if ("stickWood".equals(OreDictionary.getOreName(id))) {
				return true;
			}
		}

		return false;
	}
}
