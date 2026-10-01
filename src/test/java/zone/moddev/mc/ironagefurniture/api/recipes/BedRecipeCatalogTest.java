package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.Enumerations.UpholsteryColour;

public class BedRecipeCatalogTest {
    private JsonObject read(Path path) throws Exception {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }

    @Test public void everyWoodHasSixteenExactBedConversionsAndCanopyUpgrades() throws Exception {
        Path assets = Paths.get("src/main/resources/assets/ironagefurniture");
        JsonObject woods = read(Paths.get("gradle/furniture-catalog.json")).getAsJsonObject("woods");
        int checked = 0;
        for (java.util.Map.Entry<String, JsonElement> integration : woods.entrySet()) {
            for (JsonElement entry : integration.getValue().getAsJsonArray()) {
                String wood = entry.getAsString().replace("dark_oak", "big_oak");
                String single = "bed_wood_foot_" + wood;
                String canopy = "bed_canopy_foot_lower_" + wood;
                JsonObject plank = read(assets.resolve("recipes/chair_wood_ironage_classic_" + wood + ".json"))
                        .getAsJsonObject("key").getAsJsonObject("x");
                for (UpholsteryColour colour : UpholsteryColour.values()) {
                    String id = colour == UpholsteryColour.RED ? single : single + "_" + colour.getSerializedName();
                    JsonObject recipe = read(assets.resolve("recipes/" + id + ".json"));
                    assertEquals("forge:ore_shapeless", recipe.get("type").getAsString());
                    assertEquals(2, recipe.getAsJsonArray("ingredients").size());
                    JsonObject vanillaBed = recipe.getAsJsonArray("ingredients").get(0).getAsJsonObject();
                    assertEquals("minecraft:bed", vanillaBed.get("item").getAsString());
                    assertEquals(colour.getCarpetMetadata(), vanillaBed.get("data").getAsInt());
                    assertEquals(plank, recipe.getAsJsonArray("ingredients").get(1));
                    checkResult(recipe, single, colour);
                    JsonObject advancement = read(assets.resolve("advancements/recipes/" + id + ".json"));
                    assertEquals(vanillaBed, advancement.getAsJsonObject("criteria").getAsJsonObject("has_ingredient")
                            .getAsJsonObject("conditions").getAsJsonArray("items").get(0));

                    id = canopy + "_" + colour.getSerializedName();
                    recipe = read(assets.resolve("recipes/" + id + ".json"));
                    assertEquals("ironagefurniture:canopy_bed_upgrade", recipe.get("type").getAsString());
                    assertEquals(2, recipe.getAsJsonArray("ingredients").size());
                    assertEquals("ironagefurniture:" + single,
                            recipe.getAsJsonArray("ingredients").get(0).getAsJsonObject().get("item").getAsString());
                    assertEquals(plank, recipe.getAsJsonArray("ingredients").get(1));
                    checkResult(recipe, canopy, colour);
                    advancement = read(assets.resolve("advancements/recipes/" + id + ".json"));
                    JsonObject predicate = advancement.getAsJsonObject("criteria").getAsJsonObject("has_ingredient")
                            .getAsJsonObject("conditions").getAsJsonArray("items").get(0).getAsJsonObject();
                    assertEquals("ironagefurniture:" + single, predicate.get("item").getAsString());
                    assertEquals("{Color:\"" + colour.getSerializedName() + "\"}", predicate.get("nbt").getAsString());
                    assertFalse("Stored colour must win over stale item metadata", predicate.has("data"));
                    assertEquals("ironagefurniture:" + id, advancement.getAsJsonObject("criteria")
                            .getAsJsonObject("has_the_recipe").getAsJsonObject("conditions").get("recipe").getAsString());
                    if (!integration.getKey().equals("vanilla")) {
                        assertEquals(integration.getKey(), recipe.getAsJsonArray("conditions").get(0)
                                .getAsJsonObject().get("modid").getAsString());
                        assertEquals(recipe.get("conditions"), advancement.get("conditions"));
                    }
                    ++checked;
                }
            }
        }
        assertEquals(65 * 16, checked);
    }

    private void checkResult(JsonObject recipe, String block, UpholsteryColour colour) {
        JsonObject result = recipe.getAsJsonObject("result");
        assertEquals("ironagefurniture:" + block, result.get("item").getAsString());
        assertEquals(colour.getItemMetadata(), result.get("data").getAsInt());
        assertEquals("{Color:\"" + colour.getSerializedName() + "\"}", result.get("nbt").getAsString());
        assertEquals("ironagefurniture:" + block + "/meta_" + colour.getItemMetadata(), recipe.get("group").getAsString());
    }
}
