package zone.moddev.mc.ironagefurniture.migration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;

/** Preserves historical furniture recipe progress at the native JSON load/save boundary.
 * Retired or unavailable advancement identities remain opaque: no guessed replacement is earned.
 * Known old inventory criteria keep their original earned instant, never the current clock. */
public final class LegacyAdvancementProgressMigration {
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z", Locale.ROOT);
    private final Map<String, JsonObject> unavailable = new LinkedHashMap<>();
    private final Map<String, JsonObject> historicalCriteria = new LinkedHashMap<>();

    /** Returns an independent JSON tree; the caller's original input is never mutated. */
    public JsonElement prepare(JsonElement input, ServerAdvancementManager manager) {
        unavailable.clear();
        historicalCriteria.clear();
        if (!input.isJsonObject()) return input;
        JsonObject source = input.getAsJsonObject();
        int version;
        try { version = source.has("DataVersion") ? source.get("DataVersion").getAsInt() : 1343; }
        catch (RuntimeException malformed) { return input; }
        if (version > SharedConstants.getCurrentVersion().getDataVersion().getVersion()) return input;
        JsonObject copy = source.deepCopy();
        for (Map.Entry<String, JsonElement> entry : source.entrySet()) {
            ResourceLocation id = ResourceLocation.tryParse(entry.getKey());
            if (id == null || !id.getNamespace().equals("ironagefurniture") || !entry.getValue().isJsonObject()) continue;
            JsonObject saved = entry.getValue().getAsJsonObject();
            if (!saved.has("criteria") || !saved.get("criteria").isJsonObject()) continue;
            var holder = manager.getAdvancement(id);
            if (holder == null) {
                unavailable.put(entry.getKey(), saved.deepCopy());
                continue;
            }
            JsonObject criteria = saved.getAsJsonObject("criteria"), historical = new JsonObject();
            for (Map.Entry<String, JsonElement> criterion : criteria.entrySet()) {
                if (!holder.getCriteria().containsKey(criterion.getKey())) historical.add(criterion.getKey(), criterion.getValue().deepCopy());
            }
            if (historical.size() != 0) historicalCriteria.put(entry.getKey(), historical);
            // These two old inventory criterion names are evidenced by the original 1.15/1.16
            // recipe advancements. Only an explicitly completed, currently available IAF recipe
            // with its native recipe-unlocked criterion may inherit that earned progress.
            boolean recipe = id.getPath().startsWith("recipes/")
                    && holder.getCriteria().containsKey("has_the_recipe")
                    && holder.getRewards().getRecipes().length == 1
                    && holder.getRewards().getRecipes()[0].getNamespace().equals("ironagefurniture");
            if (!recipe || !saved.has("done") || !saved.get("done").isJsonPrimitive()
                    || !saved.get("done").getAsBoolean() || criteria.has("has_the_recipe")) continue;
            String originalTime = null;
            Instant earliest = null;
            boolean understood = true;
            for (Map.Entry<String, JsonElement> criterion : historical.entrySet()) {
                if (!criterion.getKey().equals("has_rock") && !criterion.getKey().equals("has_wood")) { understood = false; break; }
                try {
                    String text = criterion.getValue().getAsString();
                    Instant earned = OffsetDateTime.parse(text, TIME).toInstant();
                    if (earliest == null || earned.isBefore(earliest)) { earliest = earned; originalTime = text; }
                } catch (DateTimeParseException | IllegalStateException invalid) { understood = false; break; }
            }
            if (understood && originalTime != null)
                copy.getAsJsonObject(entry.getKey()).getAsJsonObject("criteria").addProperty("has_the_recipe", originalTime);
        }
        return copy;
    }

    /** Carries opaque history through native saves without reviving revoked modern progress. */
    public JsonElement finish(JsonElement encoded) {
        if (!encoded.isJsonObject() || unavailable.isEmpty() && historicalCriteria.isEmpty()) return encoded;
        JsonObject copy = encoded.getAsJsonObject().deepCopy();
        unavailable.forEach((id, saved) -> { if (!copy.has(id)) copy.add(id, saved.deepCopy()); });
        historicalCriteria.forEach((id, saved) -> {
            JsonObject progress;
            if (copy.has(id) && copy.get(id).isJsonObject()) progress = copy.getAsJsonObject(id);
            else {
                progress = new JsonObject();
                progress.add("criteria", new JsonObject());
                // If native progress was revoked or was only partial, history is not an award.
                progress.addProperty("done", false);
                copy.add(id, progress);
            }
            JsonObject criteria = progress.getAsJsonObject("criteria");
            saved.entrySet().forEach(entry -> { if (!criteria.has(entry.getKey())) criteria.add(entry.getKey(), entry.getValue().deepCopy()); });
        });
        return copy;
    }
}
