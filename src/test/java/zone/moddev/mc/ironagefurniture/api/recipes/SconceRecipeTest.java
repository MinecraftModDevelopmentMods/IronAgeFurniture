package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal;

/** All sconce frames use the same amount of metal, regardless of its type. */
public class SconceRecipeTest {
    @Test public void fiveMatchingNuggetsMakeFourSconcesForEveryMetal() throws IOException {
        int recipes = 0;
        for (SconceMetal metal : SconceMetal.values()) {
            String id = "light_metal_ironage_sconce_floor_empty_"
                    + (metal.isBaseMetal() ? "basemetals_" : "") + metal.getName();
            JsonObject recipe;
            try (Reader reader = Files.newBufferedReader(Paths.get(
                    "src/main/resources/data/ironagefurniture/recipes/" + id + ".json"), StandardCharsets.UTF_8)) {
                recipe = new JsonParser().parse(reader).getAsJsonObject();
            }
            assertEquals(id, "minecraft:crafting_shaped", recipe.get("type").getAsString());
            JsonArray pattern = recipe.getAsJsonArray("pattern");
            assertEquals(id, 3, pattern.size());
            assertEquals(id, "xxx", pattern.get(0).getAsString());
            assertEquals(id, "x  ", pattern.get(1).getAsString());
            assertEquals(id, "x  ", pattern.get(2).getAsString());
            JsonObject key = recipe.getAsJsonObject("key");
            assertEquals(id, 1, key.entrySet().size());
            JsonObject ingredient = key.getAsJsonObject("x");
            assertEquals(id, 1, ingredient.entrySet().size());
            String input = metal.isBaseMetal() ? "forge:nuggets/" + metal.getName()
                    : "minecraft:" + metal.getName() + "_nugget";
            assertEquals(id, input, ingredient.get(metal.isBaseMetal() ? "tag" : "item").getAsString());
            JsonObject result = recipe.getAsJsonObject("result");
            assertEquals(id, "ironagefurniture:" + id, result.get("item").getAsString());
            assertEquals(id, 4, result.get("count").getAsInt());
            recipes++;
        }
        assertEquals(23, recipes);
    }
}
