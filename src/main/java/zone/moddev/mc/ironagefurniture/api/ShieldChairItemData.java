package zone.moddev.mc.ironagefurniture.api;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/** Item representation of the shield fitted to a shield chair. Untagged old chairs have a plain shield. */
public final class ShieldChairItemData {
	private static final String CHAIR_TAG = "IAFShieldChair";
	private static final String EMPTY_TAG = "Empty";
	private static final String SHIELD_TAG = "Shield";

	private ShieldChairItemData() { }

	public static boolean isEmptyFrame(ItemStack chair) {
		return chair != null && chair.hasTagCompound()
				&& chair.getTagCompound().hasKey(CHAIR_TAG, 10)
				&& chair.getTagCompound().getCompoundTag(CHAIR_TAG).getBoolean(EMPTY_TAG);
	}

	public static ItemStack getShield(ItemStack chair) {
		if (chair != null && chair.hasTagCompound() && chair.getTagCompound().hasKey(CHAIR_TAG, 10)) {
			NBTTagCompound data = chair.getTagCompound().getCompoundTag(CHAIR_TAG);
			if (isEmptyFrame(chair)) return null;
			if (data.hasKey(SHIELD_TAG, 10)) {
				ItemStack shield = ItemStack.loadItemStackFromNBT(data.getCompoundTag(SHIELD_TAG));
				if (shield != null && shield.getItem() == Items.SHIELD) {
					shield.stackSize = 1;
					return shield;
				}
			}
		}
		return new ItemStack(Items.SHIELD);
	}

	public static ItemStack createChair(Block chair, ItemStack shield) {
		ItemStack result = new ItemStack(chair);
		NBTTagCompound root = new NBTTagCompound();
		NBTTagCompound data = new NBTTagCompound();
		if (shield == null || shield.getItem() != Items.SHIELD) {
			data.setBoolean(EMPTY_TAG, true);
		} else {
			ItemStack single = shield.copy();
			single.stackSize = 1;
			data.setTag(SHIELD_TAG, single.writeToNBT(new NBTTagCompound()));
		}
		root.setTag(CHAIR_TAG, data);
		result.setTagCompound(root);
		return result;
	}
}
