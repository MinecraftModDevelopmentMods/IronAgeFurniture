package zone.moddev.mc.ironagefurniture.api;

import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockGlassVase;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class VasePlantHelper {
	private static final String PLANT_TAG = "VasePlant";

	private VasePlantHelper() {
	}

	public static boolean isGlassVaseStack(ItemStack itemStack) {
		return itemStack != null && itemStack.stackSize > 0
			&& itemStack.getItem() instanceof ItemBlockGlassVase;
	}

	public static boolean isPlantItem(ItemStack itemStack) {
		Block block = getBlock(itemStack);

		if (block == Blocks.TALLGRASS) {
			return itemStack.getMetadata() == 2;
		}

		return block == Blocks.RED_FLOWER
			|| block == Blocks.YELLOW_FLOWER
			|| block == Blocks.SAPLING
			|| block == Blocks.BROWN_MUSHROOM
			|| block == Blocks.RED_MUSHROOM
			|| block == Blocks.DEADBUSH
			|| block == Blocks.CACTUS;
	}

	public static boolean canHandleVaseClick(ItemStack vaseStack, ItemStack heldItem) {
		if (!isGlassVaseStack(vaseStack)) {
			return false;
		}

		ItemStack plant = getPlant(vaseStack);

		if (plant != null) {
			return isEmpty(heldItem) || isSamePlant(plant, heldItem);
		}

		return isPlantItem(heldItem);
	}

	public static boolean hasPlant(ItemStack vaseStack) {
		return getPlant(vaseStack) != null;
	}

	public static ItemStack getPlant(ItemStack vaseStack) {
		if (!isGlassVaseStack(vaseStack) || !vaseStack.hasTagCompound()
				|| !vaseStack.getTagCompound().hasKey(PLANT_TAG)) {
			return null;
		}

		ItemStack plant = ItemStack.loadItemStackFromNBT(vaseStack.getTagCompound().getCompoundTag(PLANT_TAG));
		return plant != null && plant.stackSize > 0 ? plant : null;
	}

	public static boolean addPlant(ItemStack vaseStack, ItemStack plantStack) {
		if (!isGlassVaseStack(vaseStack) || hasPlant(vaseStack) || !isPlantItem(plantStack)) {
			return false;
		}

		NBTTagCompound compound = vaseStack.hasTagCompound() ? vaseStack.getTagCompound() : new NBTTagCompound();
		NBTTagCompound plantTag = new NBTTagCompound();
		ItemStack storedPlant = copyOne(plantStack);
		storedPlant.writeToNBT(plantTag);
		compound.setTag(PLANT_TAG, plantTag);
		vaseStack.setTagCompound(compound);
		return true;
	}

	public static ItemStack removePlant(ItemStack vaseStack) {
		ItemStack plant = getPlant(vaseStack);

		if (plant == null || !vaseStack.hasTagCompound()) {
			return null;
		}

		NBTTagCompound compound = vaseStack.getTagCompound();
		compound.removeTag(PLANT_TAG);

		if (compound.hasNoTags()) {
			vaseStack.setTagCompound(null);
		}

		return plant;
	}

	public static boolean isSamePlant(ItemStack storedPlant, ItemStack heldItem) {
		return storedPlant != null && heldItem != null && heldItem.stackSize > 0
			&& storedPlant.isItemEqual(heldItem)
			&& ItemStack.areItemStackTagsEqual(storedPlant, heldItem);
	}

	public static ItemStack copyOne(ItemStack itemStack) {
		if (itemStack == null) {
			return null;
		}

		ItemStack copy = itemStack.copy();
		copy.stackSize = 1;
		return copy;
	}

	private static boolean isEmpty(ItemStack itemStack) {
		return itemStack == null || itemStack.stackSize <= 0;
	}

	private static Block getBlock(ItemStack itemStack) {
		return itemStack != null && itemStack.stackSize > 0 && itemStack.getItem() instanceof ItemBlock
			? ((ItemBlock)itemStack.getItem()).getBlock() : null;
	}
}
