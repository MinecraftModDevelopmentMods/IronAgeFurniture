package zone.moddev.mc.ironagefurniture.api.surface;

import zone.moddev.mc.ironagefurniture.api.Items.DrinkContainerHelper;
import zone.moddev.mc.ironagefurniture.api.VasePlantHelper;
import zone.moddev.mc.ironagefurniture.api.surface.SurfaceSetting.Slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

public final class SurfaceSettingInteraction {
	public static enum Result {
		NOT_HANDLED,
		HANDLED,
		HANDLED_AND_EMPTIED
	}

	private SurfaceSettingInteraction() {
	}

	public static Result handle(SurfaceSettingHost host, EntityPlayer player, EnumHand hand, ItemStack heldItem,
			float hitX, float hitZ, EnumFacing.Axis forcedAxis, boolean allowSingleton) {
		if (host == null || player == null) {
			return Result.NOT_HANDLED;
		}

		SurfaceSetting setting = host.getSurfaceSetting();
		Slot target = setting.findTarget(hitX, hitZ);
		ItemStack targetedItem = setting.getItem(target);

		if (target != null && DrinkContainerHelper.canFillDisplayedDrinkware(targetedItem, heldItem)) {
			if (!player.getEntityWorld().isRemote) {
				ItemStack filledItem = DrinkContainerHelper.fillDisplayedDrinkware(targetedItem, heldItem,
					player, hand);

				if (filledItem != null) {
					setting.replace(target, filledItem);
					host.markSurfaceSettingChanged();
				}
			}

			return Result.HANDLED;
		}

		if (target != null && VasePlantHelper.canHandleVaseClick(targetedItem, heldItem)) {
			if (!player.getEntityWorld().isRemote) {
				handleVase(setting, target, host, player, hand, heldItem);
			}

			return Result.HANDLED;
		}

		if (target != null && canRetrieve(targetedItem, heldItem)) {
			boolean removesLastItem = setting.getItemCount() == 1;

			if (!player.getEntityWorld().isRemote) {
				ItemStack removed = setting.remove(target);
				host.markSurfaceSettingChanged();
				DrinkContainerHelper.giveOrDrop(player, removed);
			}

			return removesLastItem ? Result.HANDLED_AND_EMPTIED : Result.HANDLED;
		}

		// An occupied position must never spill a follow-up click into placement elsewhere.
		if (target != null) {
			return Result.HANDLED;
		}

		if (heldItem == null || heldItem.stackSize <= 0) {
			return Result.NOT_HANDLED;
		}

		boolean isSettingItem = SurfaceSetting.isSettingItem(heldItem);
		boolean canInsert = (allowSingleton || isSettingItem)
			&& setting.canInsert(heldItem, player.getHorizontalFacing(), hitX, hitZ, forcedAxis);

		if (!canInsert) {
			return isSettingItem ? Result.HANDLED : Result.NOT_HANDLED;
		}

		if (!player.getEntityWorld().isRemote) {
			if (setting.insert(heldItem, player.getHorizontalFacing(), hitX, hitZ, forcedAxis)) {
				host.markSurfaceSettingChanged();
				consumeOne(player, hand, heldItem);
			}
		}

		return Result.HANDLED;
	}

	private static void handleVase(SurfaceSetting setting, Slot target, SurfaceSettingHost host,
			EntityPlayer player, EnumHand hand, ItemStack heldItem) {
		ItemStack displayedItem = setting.getItem(target);
		ItemStack plant = VasePlantHelper.removePlant(displayedItem);

		if (plant != null) {
			setting.replace(target, displayedItem);
			host.markSurfaceSettingChanged();
			DrinkContainerHelper.giveOrDrop(player, plant);
			return;
		}

		if (VasePlantHelper.addPlant(displayedItem, heldItem)) {
			setting.replace(target, displayedItem);
			host.markSurfaceSettingChanged();
			consumeOne(player, hand, heldItem);
		}
	}

	private static boolean canRetrieve(ItemStack displayedItem, ItemStack heldItem) {
		if (displayedItem == null || displayedItem.stackSize <= 0) {
			return false;
		}

		if (heldItem == null || heldItem.stackSize <= 0) {
			return true;
		}

		return ItemStack.areItemsEqual(displayedItem, heldItem)
			&& ItemStack.areItemStackTagsEqual(displayedItem, heldItem);
	}

	private static void consumeOne(EntityPlayer player, EnumHand hand, ItemStack heldItem) {
		if (player.capabilities.isCreativeMode) {
			return;
		}

		heldItem.stackSize--;

		if (heldItem.stackSize <= 0) {
			player.setHeldItem(hand, null);
		}
	}
}
