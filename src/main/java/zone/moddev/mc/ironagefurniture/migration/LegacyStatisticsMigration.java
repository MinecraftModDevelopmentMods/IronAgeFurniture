package zone.moddev.mc.ironagefurniture.migration;

import java.util.ArrayList;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import zone.moddev.mc.ironagefurniture.Ironagefurniture;

/** Recovers retired furniture counters before the native statistics reader resolves IDs. */
public final class LegacyStatisticsMigration {
    private LegacyStatisticsMigration() { }

    /**
     * Uses actual registered aliases, never guesses an unavailable provider's ID.
     * Merge counters before native lookup, which otherwise overwrites collisions.
     */
    public static void finish(CompoundTag root) {
        if (!root.contains("stats", Tag.TAG_COMPOUND)) return;
        CompoundTag original = root.getCompound("stats"), stats = original.copy();
        boolean changed = false;
        for (String type : stats.getAllKeys()) {
            Registry<?> registry;
            if (type.equals("minecraft:mined")) registry = BuiltInRegistries.BLOCK;
            else if (type.equals("minecraft:crafted") || type.equals("minecraft:used")
                    || type.equals("minecraft:broken") || type.equals("minecraft:picked_up")
                    || type.equals("minecraft:dropped")) registry = BuiltInRegistries.ITEM;
            else continue;
            if (!stats.contains(type, Tag.TAG_COMPOUND)) continue;
            CompoundTag values = stats.getCompound(type);
            for (String old : new ArrayList<>(values.getAllKeys())) {
                ResourceLocation id = ResourceLocation.tryParse(old);
                if (id == null || !id.getNamespace().equals(Ironagefurniture.MODID)
                        || !values.contains(old, Tag.TAG_INT)) continue;
                ResourceLocation target = canonicalId(registry, id);
                if (target == null || target.equals(id)) continue;
                String key = target.toString();
                // A malformed existing destination is not permission to overwrite opaque data.
                if (values.contains(key) && !values.contains(key, Tag.TAG_INT)) continue;
                long total = (long)values.getInt(old) + values.getInt(key);
                values.putInt(key, (int)Math.clamp(total, Integer.MIN_VALUE, Integer.MAX_VALUE));
                values.remove(old);
                changed = true;
            }
        }
        if (changed) root.put("stats", stats);
    }

    private static <T> ResourceLocation canonicalId(Registry<T> registry, ResourceLocation id) {
        return registry.getOptional(id).map(registry::getKey).orElse(null);
    }
}
