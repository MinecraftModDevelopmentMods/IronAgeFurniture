package zone.moddev.mc.ironagefurniture.migration;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Keeps learned furniture recipes usable when their old names change. */
final class LegacyRecipeBookMigration {
    private static final Pattern PADDED = Pattern.compile("^(chair_wood_ironage_bench(?:_back)?_padded_single_.+)_(\\d{1,2})$");
    private LegacyRecipeBookMigration() { }

    static void prepare(CompoundTag root) {
        if (root.contains("recipeBook")) {
            CompoundTag book = LegacyNbt.compound(root, "recipeBook");
            for (String key : new String[]{"recipes", "toBeDisplayed"}) {
                if (!book.contains(key)) continue;
                ListTag saved = LegacyNbt.list(book, key), current = new ListTag();
                for (int index = 0; index < saved.size(); index++) {
                    String target = currentRecipe(LegacyNbt.string(saved, index, ""));
                    if (target != null && !current.contains(StringTag.valueOf(target))) current.add(StringTag.valueOf(target));
                }
                book.put(key, current);
            }
        }
        for (String key : new ArrayList<>(root.getAllKeys())) {
            Tag child = root.get(key);
            if (child instanceof CompoundTag && !"recipeBook".equals(key)) prepare((CompoundTag) child);
        }
    }

    static String currentRecipe(String saved) {
        // Before coloured signs, this recipe made the vanilla oak sign.
        if ("minecraft:sign".equals(saved)) return "minecraft:oak_sign";
        // An unavailable add-on owns its recipe names; leave them recoverable.
        if (!saved.startsWith("ironagefurniture:")) return saved;
        // Standalone IAF wax candles now craft the native vanilla candle item.
        if ("ironagefurniture:light_metal_ironage_candle_floor".equals(saved))
            return "ironagefurniture:candles_from_tallow";
        ResourceLocation old = ResourceLocation.parse(saved);
        String path = old.getPath();
        if (path.startsWith("bed_") && path.contains("_recolour")) return null;
        Matcher padded = PADDED.matcher(path);
        if (padded.matches()) {
            String colour = UpholsteryColour.byItemMetadata(Integer.parseInt(padded.group(2))).getSerializedName();
            return "ironagefurniture:" + LegacyPaddedBenchIds.modernPaddedPath(
                    ResourceLocation.fromNamespaceAndPath(old.getNamespace(), padded.group(1)), colour);
        }
        String suffix = "";
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            String token = "_" + colour.getSerializedName();
            if (path.endsWith(token) && token.length() > suffix.length()) suffix = token;
        }
        path = path.substring(0, path.length() - suffix.length());
        path = path.replaceAll("_big_oak(?=_|$)", "_dark_oak");
        ResourceLocation current = LegacyPaddedBenchIds.currentId(ResourceLocation.fromNamespaceAndPath(old.getNamespace(), path));
        if (LegacyPaddedBenchIds.isLegacyPaddedPath(current.getPath()))
            return "ironagefurniture:" + LegacyPaddedBenchIds.modernPaddedPath(current, "red");
        if ("_red".equals(suffix) && (path.startsWith("bed_wood_") || path.startsWith("bed_canopy_foot_left_"))) suffix = "";
        return current.toString() + suffix;
    }
}
