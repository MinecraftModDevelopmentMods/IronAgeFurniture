package zone.moddev.mc.ironagefurniture.migration;

import java.util.ArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;

/** Rewrite saved identities before vanilla discards obsolete item subtype numbers. */
public final class LegacyPaddedItemMigration {
    static boolean containsFurnitureItems(Tag value) {
        if (value instanceof CompoundTag tag) {
            String id = LegacyNbt.string(tag, "id", "");
            if ((tag.contains("Count") || tag.contains("count")) && id.startsWith("ironagefurniture:")) return true;
            for (String key : tag.getAllKeys()) if (containsFurnitureItems(tag.get(key))) return true;
        } else if (value instanceof ListTag list) for (Tag child : list) if (containsFurnitureItems(child)) return true;
        return false;
    }
    public static boolean migrateChunkContents(CompoundTag root) {
        if (unavailableProviderItem(root)) return false;
        boolean changed = false;
        if ((root.contains("Count") || root.contains("count")) && root.contains("id")) changed |= migrateItemCompound(root);
        for (String key : new ArrayList<>(root.getAllKeys())) {
            Tag child = root.get(key);
            if (child instanceof CompoundTag compound) changed |= migrateChunkContents(compound);
            else if (child instanceof ListTag list) changed |= migrateList(list);
        }
        return changed;
    }
    private static boolean migrateList(ListTag list) {
        boolean changed = false;
        for (Tag child : list) {
            if (child instanceof CompoundTag tag) changed |= migrateChunkContents(tag);
            else if (child instanceof ListTag nested) changed |= migrateList(nested);
        }
        return changed;
    }
    public static boolean migrateItemCompound(CompoundTag stack) {
        // Providers are optional. Do not consume subtype data or rename
        // an unavailable item before its provider can be restored on a later load.
        CompoundTag migrated = stack.copy();
        if (!rewriteItemCompound(migrated)) return false;
        ResourceLocation target = ResourceLocation.tryParse(migrated.getString("id"));
        // Check the entire candidate BEFORE consuming subtype/component payload.
        // This includes built-in IE as well as BOP/BWG and retired BYG names.
        if (target == null || !net.minecraft.core.registries.BuiltInRegistries.ITEM.containsKey(target)) return false;
        for (String key : new ArrayList<>(stack.getAllKeys())) stack.remove(key);
        stack.merge(migrated);
        return true;
    }
    private static boolean optionalWood(String id) {
        return id.contains("_biomesoplenty_") || id.contains("_biomeswevegone_")
                || id.contains("_byg_") || id.contains("_immersiveengineering_");
    }
    static boolean unavailableProviderItem(CompoundTag stack) {
        if (!stack.contains("Count") && !stack.contains("count")) return false;
        String id = stack.getString("id");
        if (!id.startsWith("ironagefurniture:") || !optionalWood(id)) return false;
        // A retired raw ID can be absent even though its provider and current
        // coloured replacement are present. Inspect a copy, never the saved item.
        CompoundTag candidate = stack.copy();
        rewriteItemCompound(candidate);
        ResourceLocation key = ResourceLocation.tryParse(candidate.getString("id"));
        return key == null || !net.minecraft.core.registries.BuiltInRegistries.ITEM.containsKey(key);
    }

    private static boolean rewriteItemCompound(CompoundTag stack) {
        String id = LegacyNbt.string(stack, "id", "");
        if (!id.startsWith("ironagefurniture:")) return false;
        String old = stack.toString();
        CompoundTag legacy = LegacyNbt.compound(stack, "tag");
        CompoundTag components = LegacyNbt.compound(stack, "components");
        CompoundTag custom = LegacyNbt.compound(components, "minecraft:custom_data");
        CompoundTag colourData = legacy.contains("Color") ? legacy : custom;
        String path = id.substring("ironagefurniture:".length());
        if (path.startsWith("light_metal_ironage_candle_")) {
            stack.putString("id", "minecraft:candle");
            stack.remove("Damage");
        } else if (path.startsWith("light_metal_ironage_sconce_floor_empty_")) {
            String saved = legacy.contains("Metal") ? LegacyNbt.string(legacy, "Metal", "iron") : LegacyNbt.string(custom, "Metal", "iron");
            SconceMetal metal = path.equals(SconceMetalData.FRAME)
                    ? legacy.contains("Metal") || custom.contains("Metal") ? SconceMetal.byName(saved)
                            : SconceMetal.byLegacyMetadata(LegacyNbt.integer(stack, "Damage", 0)) : SconceMetal.byName(path.substring(path.lastIndexOf('_') + 1));
            stack.putString("id", SconceMetalData.itemId(metal).toString());
            stack.remove("Damage"); legacy.remove("Metal"); custom.remove("Metal");
        } else if (LegacyPaddedBenchIds.isModernRedPaddedPath(path) && colourData.contains("Color")) {
            stack.putString("id", LegacyPaddedBenchIds.currentId(new ResourceLocation("ironagefurniture",
                    LegacyPaddedBenchIds.colouredPathFromModernRed(path, LegacyNbt.string(colourData, "Color", "red")))).toString());
            legacy.remove("Color"); custom.remove("Color");
        } else if (LegacyPaddedBenchIds.isLegacyPaddedPath(path)) {
            String colour = LegacyNbt.string(colourData, "Color", "red");
            stack.putString("id", "ironagefurniture:" + LegacyPaddedBenchIds.modernPaddedPath(path, colour));
            legacy.remove("Color"); custom.remove("Color"); stack.remove("Damage");
        } else if (LegacyUpholsteryMigration.isUpholsteredPath(path)) {
            UpholsteryColour fixed = null;
            for (UpholsteryColour value : UpholsteryColour.values()) if (value != UpholsteryColour.RED && path.endsWith("_" + value.getName())
                    && (fixed == null || value.getName().length() > fixed.getName().length())) fixed = value;
            UpholsteryColour colour = fixed != null ? fixed : colourData.contains("Color") ? UpholsteryColour.byName(LegacyNbt.string(colourData, "Color", "red"))
                    : UpholsteryColour.byItemMetadata(LegacyNbt.integer(stack, "Damage", 0));
            if (fixed != null) path = path.substring(0, path.length() - fixed.getName().length() - 1);
            path = LegacyPaddedBenchIds.currentId(new ResourceLocation("ironagefurniture", path)).getPath()
                    .replace("bed_canopy_foot_right_lower_", "bed_canopy_foot_left_lower_");
            stack.putString("id", "ironagefurniture:" + path + (colour == UpholsteryColour.RED ? "" : "_" + colour.getName()));
            stack.remove("Damage"); legacy.remove("Color"); legacy.remove("Damage"); custom.remove("Color"); custom.remove("Damage");
        } else {
            stack.putString("id", LegacyPaddedBenchIds.currentId(new ResourceLocation(id)).toString());
        }
        if (stack.contains("tag")) { if (legacy.isEmpty()) stack.remove("tag"); else stack.put("tag", legacy); }
        if (components.contains("minecraft:custom_data")) {
            if (custom.isEmpty()) components.remove("minecraft:custom_data"); else components.put("minecraft:custom_data", custom);
            stack.put("components", components);
        }
        return !old.equals(stack.toString());
    }
    private LegacyPaddedItemMigration() { }
}
