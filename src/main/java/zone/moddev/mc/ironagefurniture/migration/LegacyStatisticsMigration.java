package zone.moddev.mc.ironagefurniture.migration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Set;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

/** Forge's live aliases are not used by vanilla's registry-backed statistics codec. */
public final class LegacyStatisticsMigration {
    private static final Set<String> ITEM_TYPES = Set.of("minecraft:crafted", "minecraft:used", "minecraft:broken",
            "minecraft:picked_up", "minecraft:dropped");

    /** Returns canonical IAF keys without editing the source JSON or unrelated statistics. */
    public static JsonElement convert(JsonElement source) {
        if (!source.isJsonObject() || !(source.getAsJsonObject().get("stats") instanceof JsonObject)) return source;
        JsonObject result = source.deepCopy().getAsJsonObject();
        boolean changed = false;
        for (var group : result.getAsJsonObject("stats").entrySet()) {
            if (!(group.getValue() instanceof JsonObject values)) continue;
            if (!group.getKey().equals("minecraft:mined") && !ITEM_TYPES.contains(group.getKey())) continue;
            for (String old : Set.copyOf(values.keySet())) {
                String target = canonical(group.getKey(), old);
                if (target.equals(old)) continue;
                try {
                    int count = values.getAsJsonPrimitive(old).getAsBigDecimal().intValueExact();
                    if (values.has(target)) {
                        int existing = values.getAsJsonPrimitive(target).getAsBigDecimal().intValueExact();
                        count = (int)Math.clamp((long)count + existing, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    }
                    values.addProperty(target, count);
                    values.remove(old);
                    changed = true;
                } catch (ArithmeticException | IllegalStateException | ClassCastException | NumberFormatException invalidCount) {
                    // Leave malformed input to the normal codec; never overwrite it.
                }
            }
        }
        return changed ? result : source;
    }

    private static String canonical(String type, String source) {
        Identifier id = Identifier.tryParse(source);
        if (id == null || !id.getNamespace().equals("ironagefurniture")) return source;
        if (type.equals("minecraft:mined")) {
            var block = ForgeRegistries.BLOCKS.getValue(id);
            return block == null || block == Blocks.AIR ? source : ForgeRegistries.BLOCKS.getKey(block).toString();
        }
        var item = ForgeRegistries.ITEMS.getValue(id);
        return item == null || item == Items.AIR ? source : ForgeRegistries.ITEMS.getKey(item).toString();
    }
    private LegacyStatisticsMigration() { }
}
