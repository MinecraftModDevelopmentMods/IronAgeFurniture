package zone.moddev.mc.ironagefurniture.migration;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;

/** Keeps learned furniture recipes usable when their old names change. */
final class LegacyRecipeBookMigration {
    private static final Pattern PADDED = Pattern.compile("^(chair_wood_ironage_bench(?:_back)?_padded_single_.+)_(\\d{1,2})$");
    private LegacyRecipeBookMigration() { }

    static void prepare(CompoundNBT root) {
        if (root.contains("recipeBook", 10)) {
            CompoundNBT book = root.getCompound("recipeBook");
            for (String key : new String[]{"recipes", "toBeDisplayed"}) {
                if (!book.contains(key, 9)) continue;
                ListNBT saved = book.getList(key, 8), current = new ListNBT();
                for (int index = 0; index < saved.size(); index++) {
                    String target = currentRecipe(saved.getString(index));
                    if (target != null && !current.contains(StringNBT.valueOf(target))) current.add(StringNBT.valueOf(target));
                }
                book.put(key, current);
            }
        }
        for (String key : new ArrayList<>(root.getAllKeys())) {
            INBT child = root.get(key);
            if (child instanceof CompoundNBT && !"recipeBook".equals(key)) prepare((CompoundNBT) child);
        }
    }

    static String currentRecipe(String saved) {
        // Before coloured signs, this recipe made the vanilla oak sign.
        if ("minecraft:sign".equals(saved)) return "minecraft:oak_sign";
        if (!saved.startsWith("ironagefurniture:") && !saved.startsWith("iafbygaddon:")) return saved;
        ResourceLocation old = new ResourceLocation(saved);
        String path = old.getPath();
        if (path.startsWith("bed_") && path.contains("_recolour")) return null;
        Matcher padded = PADDED.matcher(path);
        if (padded.matches()) {
            String colour = UpholsteryColour.byItemMetadata(Integer.parseInt(padded.group(2))).getSerializedName();
            return "ironagefurniture:" + LegacyPaddedBenchIds.modernPaddedPath(
                    new ResourceLocation(old.getNamespace(), padded.group(1)), colour);
        }
        String suffix = "";
        for (UpholsteryColour colour : UpholsteryColour.values()) {
            String token = "_" + colour.getSerializedName();
            if (path.endsWith(token) && token.length() > suffix.length()) suffix = token;
        }
        path = path.substring(0, path.length() - suffix.length());
        path = path.replaceAll("_big_oak(?=_|$)", "_dark_oak");
        ResourceLocation current = LegacyPaddedBenchIds.currentId(new ResourceLocation(old.getNamespace(), path));
        if (LegacyPaddedBenchIds.isLegacyPaddedPath(current.getPath()))
            return "ironagefurniture:" + LegacyPaddedBenchIds.modernPaddedPath(current, "red");
        if ("_red".equals(suffix) && (path.startsWith("bed_wood_") || path.startsWith("bed_canopy_foot_left_"))) suffix = "";
        return current.toString() + suffix;
    }
}
