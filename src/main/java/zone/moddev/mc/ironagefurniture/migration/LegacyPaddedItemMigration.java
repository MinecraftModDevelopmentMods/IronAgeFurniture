package zone.moddev.mc.ironagefurniture.migration;

import java.util.ArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.SconceMetalData;

/** Rewrite saved identities before vanilla discards obsolete item subtype numbers. */
public final class LegacyPaddedItemMigration {
    static boolean containsFurnitureItems(Tag value) {
        if (value instanceof CompoundTag tag) {
            String id = tag.getStringOr("id", "");
            if ((tag.contains("Count") || tag.contains("count")) && id.startsWith("ironagefurniture:")) return true;
            for (String key : tag.keySet()) if (containsFurnitureItems(tag.get(key))) return true;
        } else if (value instanceof ListTag list) for (Tag child : list) if (containsFurnitureItems(child)) return true;
        return false;
    }
    public static boolean migrateChunkContents(CompoundTag root) {
        boolean changed = false;
        if ((root.contains("Count") || root.contains("count")) && root.contains("id")) changed |= migrateItemCompound(root);
        for (String key : new ArrayList<>(root.keySet())) {
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
        String originalId = stack.getStringOr("id", "");
        if (originalId.contains("_biomesoplenty_") || originalId.contains("_biomeswevegone_") || originalId.contains("_byg_")) {
            CompoundTag migrated = stack.copy();
            if (!rewriteItemCompound(migrated)) return false;
            Identifier target = Identifier.parse(migrated.getStringOr("id", ""));
            if ((target.getPath().contains("_biomesoplenty_") || target.getPath().contains("_biomeswevegone_") || target.getPath().contains("_byg_"))
                    && !net.minecraft.core.registries.BuiltInRegistries.ITEM.containsKey(target)) return false;
            for (String key : new ArrayList<>(stack.keySet())) stack.remove(key);
            stack.merge(migrated);
            return true;
        }
        return rewriteItemCompound(stack);
    }
    private static boolean rewriteItemCompound(CompoundTag stack) {
        String id = stack.getStringOr("id", "");
        if (!id.startsWith("ironagefurniture:")) return false;
        String old = stack.toString();
        CompoundTag legacy = stack.getCompoundOrEmpty("tag");
        CompoundTag components = stack.getCompoundOrEmpty("components");
        CompoundTag custom = components.getCompoundOrEmpty("minecraft:custom_data");
        CompoundTag colourData = legacy.contains("Color") ? legacy : custom;
        String path = id.substring("ironagefurniture:".length());
        if (path.startsWith("light_metal_ironage_candle_")) {
            stack.putString("id", "minecraft:candle");
            stack.remove("Damage");
        } else if (path.startsWith("light_metal_ironage_sconce_floor_empty_")) {
            String saved = legacy.contains("Metal") ? legacy.getStringOr("Metal", "iron") : custom.getStringOr("Metal", "iron");
            SconceMetal metal = path.equals(SconceMetalData.FRAME)
                    ? legacy.contains("Metal") || custom.contains("Metal") ? SconceMetal.byName(saved)
                            : SconceMetal.byLegacyMetadata(stack.getIntOr("Damage", 0)) : SconceMetal.byName(path.substring(path.lastIndexOf('_') + 1));
            stack.putString("id", SconceMetalData.itemId(metal).toString());
            stack.remove("Damage"); legacy.remove("Metal"); custom.remove("Metal");
        } else if (LegacyPaddedBenchIds.isModernRedPaddedPath(path) && colourData.contains("Color")) {
            stack.putString("id", LegacyPaddedBenchIds.currentId(Identifier.fromNamespaceAndPath("ironagefurniture",
                    LegacyPaddedBenchIds.colouredPathFromModernRed(path, colourData.getStringOr("Color", "red")))).toString());
            legacy.remove("Color"); custom.remove("Color");
        } else if (LegacyPaddedBenchIds.isLegacyPaddedPath(path)) {
            String colour = colourData.getStringOr("Color", "red");
            stack.putString("id", "ironagefurniture:" + LegacyPaddedBenchIds.modernPaddedPath(path, colour));
            legacy.remove("Color"); custom.remove("Color"); stack.remove("Damage");
        } else if (LegacyUpholsteryMigration.isUpholsteredPath(path)) {
            UpholsteryColour fixed = null;
            for (UpholsteryColour value : UpholsteryColour.values()) if (value != UpholsteryColour.RED && path.endsWith("_" + value.getName())
                    && (fixed == null || value.getName().length() > fixed.getName().length())) fixed = value;
            UpholsteryColour colour = fixed != null ? fixed : colourData.contains("Color") ? UpholsteryColour.byName(colourData.getStringOr("Color", "red"))
                    : UpholsteryColour.byItemMetadata(stack.getIntOr("Damage", 0));
            if (fixed != null) path = path.substring(0, path.length() - fixed.getName().length() - 1);
            path = LegacyPaddedBenchIds.currentId(Identifier.fromNamespaceAndPath("ironagefurniture", path)).getPath()
                    .replace("bed_canopy_foot_right_lower_", "bed_canopy_foot_left_lower_");
            stack.putString("id", "ironagefurniture:" + path + (colour == UpholsteryColour.RED ? "" : "_" + colour.getName()));
            stack.remove("Damage"); legacy.remove("Color"); legacy.remove("Damage"); custom.remove("Color"); custom.remove("Damage");
        } else {
            stack.putString("id", LegacyPaddedBenchIds.currentId(Identifier.parse(id)).toString());
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
