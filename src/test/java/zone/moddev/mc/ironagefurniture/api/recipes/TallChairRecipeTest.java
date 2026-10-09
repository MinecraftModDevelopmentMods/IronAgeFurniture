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

/** Checks the generated recipes for every supported wood and upholstery colour. */
public class TallChairRecipeTest {
    private static JsonObject read(String path) throws IOException {
        try (Reader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }

    @Test public void bothChairUpgradesAreShapelessWithExactlyThreeMatchingIngredients() throws IOException {
        JsonObject catalog = read("gradle/furniture-catalog.json");
        int recipes = 0;
        for (Map.Entry<String, JsonElement> integration : catalog.getAsJsonObject("woods").entrySet()) {
            for (JsonElement woodEntry : integration.getValue().getAsJsonArray()) {
                String wood = woodEntry.getAsString();
                JsonObject plank = read("src/main/resources/data/ironagefurniture/recipes/chair_wood_ironage_classic_"
                        + wood + ".json").getAsJsonObject("key").getAsJsonObject("x");
                for (JsonElement colourEntry : catalog.getAsJsonArray("colours")) {
                    String colour = colourEntry.getAsString();
                    for (String form : new String[] { "wingback", "throne" }) {
                        String id = "chair_wood_ironage_" + form + "_" + wood + "_" + colour;
                        JsonObject recipe = read("src/main/resources/data/ironagefurniture/recipes/" + id + ".json");
                        assertEquals(id, "minecraft:crafting_shapeless", recipe.get("type").getAsString());
                        assertFalse(id, recipe.has("pattern"));
                        assertFalse(id, recipe.has("key"));
                        JsonArray ingredients = recipe.getAsJsonArray("ingredients");
                        assertEquals(id, 3, ingredients.size());
                        assertEquals(id, "minecraft:" + colour + "_carpet",
                                ingredients.get(0).getAsJsonObject().get("item").getAsString());
                        assertEquals(id, plank, ingredients.get(1));
                        String input = "chair_wood_ironage_" + (form.equals("throne") ? "wingback" : "classic") + "_" + wood;
                        if (form.equals("throne") && !colour.equals("red")) input += "_" + colour;
                        assertEquals(id, "ironagefurniture:" + input,
                                ingredients.get(2).getAsJsonObject().get("item").getAsString());
                        String output = "chair_wood_ironage_" + form + "_" + wood
                                + (colour.equals("red") ? "" : "_" + colour);
                        assertEquals(id, "ironagefurniture:" + output,
                                recipe.getAsJsonObject("result").get("item").getAsString());
                        assertEquals(id, 1, recipe.getAsJsonObject("result").get("count").getAsInt());
                        assertFalse(id, recipe.getAsJsonObject("result").has("nbt"));
                        assertEquals(id, "ironagefurniture:" + id, recipe.get("group").getAsString());
                        JsonObject advancement = read("src/main/resources/data/ironagefurniture/advancements/recipes/upholstery/"
                                + id + ".json").getAsJsonArray("advancements").get(0).getAsJsonObject();
                        assertEquals(id, recipe.get("conditions"), advancement.get("conditions"));
                        JsonObject document = advancement.getAsJsonObject("advancement");
                        JsonObject criteria = document.getAsJsonObject("criteria");
                        assertEquals(id, "minecraft:recipe_unlocked", criteria.getAsJsonObject("has_the_recipe")
                                .get("trigger").getAsString());
                        assertEquals(id, "ironagefurniture:" + id, criteria.getAsJsonObject("has_the_recipe")
                                .getAsJsonObject("conditions").get("recipe").getAsString());
                        assertEquals(id, ingredients.get(form.equals("throne") ? 2 : 0), criteria
                                .getAsJsonObject("has_ingredient").getAsJsonObject("conditions")
                                .getAsJsonArray("items").get(0));
                        recipes++;
                    }
                }
            }
        }
        assertEquals(576, recipes);
    }

    @Test public void tallChairPartsUseTheClassicChairRotations() throws IOException {
        JsonObject catalog = read("gradle/furniture-catalog.json");
        int variants = 0;
        for (Map.Entry<String, JsonElement> integration : catalog.getAsJsonObject("woods").entrySet())
            for (JsonElement woodEntry : integration.getValue().getAsJsonArray()) {
                String wood = woodEntry.getAsString();
                String root = "src/main/resources/assets/ironagefurniture/blockstates/chair_wood_ironage_";
                JsonObject classic = read(root + "classic_" + wood + ".json").getAsJsonObject("variants");
                for (String form : new String[] {"wingback", "throne"}) {
                    JsonObject tall = read(root + form + "_" + wood + ".json").getAsJsonObject("variants");
                    for (JsonElement colour : catalog.getAsJsonArray("colours"))
                        for (String facing : new String[] {"north", "east", "south", "west"})
                            for (boolean wet : new boolean[] {false, true}) {
                                JsonObject reference = classic.getAsJsonObject("facing=" + facing + ",waterlogged=" + wet);
                                int expected = reference.has("y") ? reference.get("y").getAsInt() : 0;
                                for (String part : new String[] {"lower", "middle", "upper"}) {
                                    String key = "colour=" + colour.getAsString() + ",facing=" + facing + ",part=" + part + ",waterlogged=" + wet;
                                    JsonObject actual = tall.getAsJsonObject(key);
                                    assertNotNull(form + "_" + wood + "/" + key, actual);
                                    assertEquals(key, expected, actual.has("y") ? actual.get("y").getAsInt() : 0);
                                    variants++;
                                }
                            }
                }
            }
        assertEquals(13824, variants);
    }
}
