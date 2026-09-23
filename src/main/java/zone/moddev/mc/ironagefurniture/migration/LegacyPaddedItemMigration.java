package zone.moddev.mc.ironagefurniture.migration;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Converts the shared 1.10/1.12 padded item format to flattened colour-specific items. */
public final class LegacyPaddedItemMigration {
	private LegacyPaddedItemMigration() {
	}

	public static ItemStack migrate(ItemStack original) {
		if (original.isEmpty()) {
			return original;
		}
		CompoundNBT serialized = original.save(new CompoundNBT());
		return migrateItemCompound(serialized) ? ItemStack.of(serialized) : original;
	}

	/** Migrates serialized item stacks before vanilla flattens a legacy chunk. */
	static boolean migrateChunkContents(CompoundNBT level) {
		return migrateNested(level);
	}

	static boolean migrateItemCompound(CompoundNBT stack) {
		boolean changed = false;
		if (stack.contains("id", 8)) {
			ResourceLocation id = new ResourceLocation(stack.getString("id"));
			if (LegacyPaddedBenchIds.isLegacyNamespace(id.getNamespace())) {
				CompoundNBT tag = stack.contains("tag", 10) ? stack.getCompound("tag") : null;
				String colour = tag != null && tag.contains(LegacyPaddedBenchIds.COLOR_TAG, 8)
						? tag.getString(LegacyPaddedBenchIds.COLOR_TAG) : "red";
				String targetPath = null;
				if (LegacyPaddedBenchIds.isLegacyPaddedPath(id.getPath())) {
					targetPath = LegacyPaddedBenchIds.modernPaddedPath(id, colour);
				} else if (LegacyPaddedBenchIds.isModernRedPaddedPath(id.getPath())
						&& tag != null && tag.contains(LegacyPaddedBenchIds.COLOR_TAG, 8)) {
					targetPath = LegacyPaddedBenchIds.colouredPathFromModernRed(id.getPath(), colour);
				}
				ResourceLocation targetId = targetPath != null
						? new ResourceLocation(Ironagefurniture.MODID, targetPath)
						: LegacyPaddedBenchIds.currentId(id);
				if (!targetId.equals(id)) {
					Item target = ForgeRegistries.ITEMS.getValue(targetId);
					if (target != null) {
						stack.putString("id", targetId.toString());
						if (tag != null) {
							tag.remove(LegacyPaddedBenchIds.COLOR_TAG);
						}
						changed = true;
					}
				}
			}
		}

		for (String key : new ArrayList<>(stack.getAllKeys())) {
			INBT child = stack.get(key);
			if (child instanceof CompoundNBT) {
				CompoundNBT compound = (CompoundNBT)child;
				if (compound.contains("id", 8) && compound.contains("Count", 1)) {
					changed |= migrateItemCompound(compound);
				} else {
					changed |= migrateNested(compound);
				}
			} else if (child instanceof ListNBT) {
				changed |= migrateList((ListNBT)child);
			}
		}
		return changed;
	}

	private static boolean migrateNested(CompoundNBT compound) {
		boolean changed = false;
		for (String key : new ArrayList<>(compound.getAllKeys())) {
			INBT child = compound.get(key);
			if (child instanceof CompoundNBT) {
				CompoundNBT nested = (CompoundNBT)child;
				changed |= nested.contains("id", 8) && nested.contains("Count", 1)
						? migrateItemCompound(nested) : migrateNested(nested);
			} else if (child instanceof ListNBT) {
				changed |= migrateList((ListNBT)child);
			}
		}
		return changed;
	}

	private static boolean migrateList(ListNBT list) {
		boolean changed = false;
		for (int index = 0; index < list.size(); ++index) {
			INBT child = list.get(index);
			if (child instanceof CompoundNBT) {
				CompoundNBT compound = (CompoundNBT)child;
				changed |= compound.contains("id", 8) && compound.contains("Count", 1)
						? migrateItemCompound(compound) : migrateNested(compound);
			} else if (child instanceof ListNBT) {
				changed |= migrateList((ListNBT)child);
			}
		}
		return changed;
	}
}
