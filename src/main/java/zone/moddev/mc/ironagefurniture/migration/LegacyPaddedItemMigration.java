package zone.moddev.mc.ironagefurniture.migration;

import java.util.ArrayList;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
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
		CompoundTag serialized = original.save(new CompoundTag());
		return migrateItemCompound(serialized) ? ItemStack.of(serialized) : original;
	}

	/** Migrates serialized item stacks before vanilla flattens a legacy chunk. */
	static boolean migrateChunkContents(CompoundTag level) {
		return migrateNested(level);
	}

    /** Unchanged item IDs still need their old container chunk preserved. */
    static boolean containsFurnitureItems(Tag value) {
        if (value instanceof CompoundTag) {
            CompoundTag compound = (CompoundTag)value;
            String id = compound.getString("id");
            if (compound.contains("Count", 99) && (id.startsWith("ironagefurniture:") || id.startsWith("iafbygaddon:")))
                return true;
            for (String key : compound.getAllKeys()) if (containsFurnitureItems(compound.get(key))) return true;
        } else if (value instanceof ListTag) {
            for (Tag child : (ListTag)value) if (containsFurnitureItems(child)) return true;
        }
        return false;
    }

	static boolean migrateItemCompound(CompoundTag stack) {
		boolean changed = LegacyUpholsteryMigration.migrateItem(stack);
        changed |= zone.moddev.mc.ironagefurniture.api.SconceMetalData.migrateItem(stack);
        String savedName = stack.getString("id");
        if (savedName.startsWith(Ironagefurniture.MODID + ":")
                || savedName.startsWith("iafbygaddon:")) {
			ResourceLocation id = new ResourceLocation(stack.getString("id"));
			if (LegacyPaddedBenchIds.isLegacyNamespace(id.getNamespace())) {
				ResourceLocation renamed = LegacyPaddedBenchIds.currentId(id);
                if (!renamed.equals(id) && !LegacyPaddedBenchIds.isLegacyPaddedPath(id.getPath())) {
                    stack.putString("id", renamed.toString());
                    id = renamed;
                    changed = true;
                }
				CompoundTag tag = stack.contains("tag", 10) ? stack.getCompound("tag") : null;
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
			Tag child = stack.get(key);
			if (child instanceof CompoundTag) {
				CompoundTag compound = (CompoundTag)child;
				if (compound.contains("id", 8) && compound.contains("Count", 1)) {
					changed |= migrateItemCompound(compound);
				} else {
					changed |= migrateNested(compound);
				}
			} else if (child instanceof ListTag) {
				changed |= migrateList((ListTag)child);
			}
		}
		return changed;
	}

	private static boolean migrateNested(CompoundTag compound) {
		boolean changed = false;
		for (String key : new ArrayList<>(compound.getAllKeys())) {
			Tag child = compound.get(key);
			if (child instanceof CompoundTag) {
				CompoundTag nested = (CompoundTag)child;
				changed |= nested.contains("id", 8) && nested.contains("Count", 1)
						? migrateItemCompound(nested) : migrateNested(nested);
			} else if (child instanceof ListTag) {
				changed |= migrateList((ListTag)child);
			}
		}
		return changed;
	}

	private static boolean migrateList(ListTag list) {
		boolean changed = false;
		for (int index = 0; index < list.size(); ++index) {
			Tag child = list.get(index);
			if (child instanceof CompoundTag) {
				CompoundTag compound = (CompoundTag)child;
				changed |= compound.contains("id", 8) && compound.contains("Count", 1)
						? migrateItemCompound(compound) : migrateNested(compound);
			} else if (child instanceof ListTag) {
				changed |= migrateList((ListTag)child);
			}
		}
		return changed;
	}
}
