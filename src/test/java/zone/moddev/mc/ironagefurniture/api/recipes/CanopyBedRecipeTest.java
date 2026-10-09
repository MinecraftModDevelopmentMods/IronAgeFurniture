package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.junit.Test;

/** Every canopy needs a matching wooden frame and matching fabric. */
public class CanopyBedRecipeTest {
    private static JsonObject read(String path) throws IOException {
        try (Reader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }

    @Test public void singleCanopiesRequireTheMatchingBedPlankAndCarpet() throws IOException {
        JsonObject catalog = read("gradle/furniture-catalog.json");
        int recipes = 0;
        for (Map.Entry<String, JsonElement> integration : catalog.getAsJsonObject("woods").entrySet())
            for (JsonElement woodEntry : integration.getValue().getAsJsonArray()) {
                String wood = woodEntry.getAsString();
                JsonObject plank = read("src/main/resources/data/ironagefurniture/recipes/chair_wood_ironage_classic_"
                        + wood + ".json").getAsJsonObject("key").getAsJsonObject("x");
                for (JsonElement colourEntry : catalog.getAsJsonArray("colours")) {
                    String colour = colourEntry.getAsString();
                    String id = "bed_canopy_foot_lower_" + wood + "_" + colour;
                    JsonObject recipe = read("src/main/resources/data/ironagefurniture/recipes/" + id + ".json");
                    assertEquals(id, "ironagefurniture:upholstery_upgrade", recipe.get("type").getAsString());
                    assertFalse(id, recipe.has("pattern"));
                    JsonArray ingredients = recipe.getAsJsonArray("ingredients");
                    assertEquals(id, 3, ingredients.size());
                    String suffix = colour.equals("red") ? "" : "_" + colour;
                    String bed = "ironagefurniture:bed_wood_foot_" + wood + suffix;
                    assertEquals(id, bed, ingredients.get(0).getAsJsonObject().get("item").getAsString());
                    assertEquals(id, plank, ingredients.get(1));
                    assertEquals(id, "minecraft:" + colour + "_carpet",
                            ingredients.get(2).getAsJsonObject().get("item").getAsString());
                    assertEquals(id, "ironagefurniture:bed_canopy_foot_lower_" + wood + suffix,
                            recipe.getAsJsonObject("result").get("item").getAsString());
                    assertEquals(id, 1, recipe.getAsJsonObject("result").get("count").getAsInt());
                    assertEquals(id, "ironagefurniture:" + id, recipe.get("group").getAsString());
                    JsonObject wrapper = read("src/main/resources/data/ironagefurniture/advancements/recipes/upholstery/"
                            + id + ".json").getAsJsonArray("advancements").get(0).getAsJsonObject();
                    assertEquals(id, recipe.get("conditions"), wrapper.get("conditions"));
                    JsonObject advancement = wrapper.getAsJsonObject("advancement");
                    JsonObject criteria = advancement.getAsJsonObject("criteria");
                    assertEquals(id, ingredients.get(0), criteria.getAsJsonObject("has_ingredient")
                            .getAsJsonObject("conditions").getAsJsonArray("items").get(0));
                    assertEquals(id, "ironagefurniture:" + id, criteria.getAsJsonObject("has_the_recipe")
                            .getAsJsonObject("conditions").get("recipe").getAsString());
                    assertEquals(id, "ironagefurniture:" + id, advancement.getAsJsonObject("rewards")
                            .getAsJsonArray("recipes").get(0).getAsString());
                    recipes++;
                }
            }
        assertEquals(288, recipes);
    }

    @Test public void doubleBedsStillCombineOnlyTwoMatchingSingles() throws IOException {
        JsonObject catalog = read("gradle/furniture-catalog.json");
        int recipes = 0;
        for (Map.Entry<String, JsonElement> integration : catalog.getAsJsonObject("woods").entrySet())
            for (JsonElement woodEntry : integration.getValue().getAsJsonArray())
                for (JsonElement colourEntry : catalog.getAsJsonArray("colours"))
                    for (boolean canopy : new boolean[] {false, true}) {
                        String wood = woodEntry.getAsString(), colour = colourEntry.getAsString();
                        String suffix = colour.equals("red") ? "" : "_" + colour;
                        String id = (canopy ? "bed_canopy_foot_left_lower_" : "bed_wood_foot_left_") + wood + suffix;
                        JsonObject recipe = read("src/main/resources/data/ironagefurniture/recipes/" + id + ".json");
                        JsonArray ingredients = recipe.getAsJsonArray("ingredients");
                        assertEquals(id, 2, ingredients.size());
                        String single = "ironagefurniture:" + (canopy ? "bed_canopy_foot_lower_" : "bed_wood_foot_") + wood + suffix;
                        assertEquals(id, single, ingredients.get(0).getAsJsonObject().get("item").getAsString());
                        assertEquals(id, ingredients.get(0), ingredients.get(1));
                        JsonObject wrapper = read("src/main/resources/data/ironagefurniture/advancements/recipes/upholstery/"
                                + id + ".json").getAsJsonArray("advancements").get(0).getAsJsonObject();
                        assertEquals(id, recipe.get("conditions"), wrapper.get("conditions"));
                        JsonObject advancement = wrapper.getAsJsonObject("advancement");
                        JsonObject criteria = advancement.getAsJsonObject("criteria");
                        assertEquals(id, ingredients.get(0), criteria.getAsJsonObject("has_ingredient")
                                .getAsJsonObject("conditions").getAsJsonArray("items").get(0));
                        assertEquals(id, "ironagefurniture:" + id, criteria.getAsJsonObject("has_the_recipe")
                                .getAsJsonObject("conditions").get("recipe").getAsString());
                        assertEquals(id, "ironagefurniture:" + id, advancement.getAsJsonObject("rewards")
                                .getAsJsonArray("recipes").get(0).getAsString());
                        recipes++;
                    }
        assertEquals(576, recipes);
    }
}
