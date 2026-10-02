package zone.moddev.mc.ironagefurniture.api.recipes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.block.Block;
import java.lang.reflect.Constructor;
import org.junit.BeforeClass;
import org.junit.Test;

public class RecipeBookGroupingTest {
    private static final Path RECIPES = Paths.get("src/main/resources/assets/ironagefurniture/recipes");

    @BeforeClass public static void bootstrap() { Bootstrap.register(); }

    private JsonObject recipe(Path path) throws Exception {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }

    @Test public void everyFurnitureRecipeGroupsOnlyTheSameItemAndColour() throws Exception {
        List<Path> paths;
        try (Stream<Path> files = Files.list(RECIPES)) {
            paths = files.filter(path -> path.toString().endsWith(".json"))
                    .filter(path -> !path.getFileName().toString().equals("_factories.json"))
                    .sorted().collect(Collectors.toList());
        }
        int checked = 0;
        for (Path path : paths) {
            JsonObject recipe = recipe(path);
            JsonObject result = recipe.getAsJsonObject("result");
            String item = result.get("item").getAsString();
            if (!item.startsWith("ironagefurniture:chair_wood_")
                    && !item.startsWith("ironagefurniture:bed_")) continue;
            String type = recipe.get("type").getAsString();
            String suffix = type.equals("ironagefurniture:matching_upholstery")
                    ? "matching_upholstery" : type.equals("ironagefurniture:bed_recolour")
                    ? "bed_recolour" : "meta_" + (result.has("data") ? result.get("data").getAsInt() : 0);
            assertEquals(path.toString(), item + "/" + suffix,
                    recipe.has("group") ? recipe.get("group").getAsString() : "");
            ++checked;
        }
        assertTrue("The complete core and optional furniture catalog must be checked", checked > 2500);
    }

    @Test public void conversionAndCraftingForTheSameChairShareOneEntry() throws Exception {
        String group = recipe(RECIPES.resolve("chair_wood_ironage_classic_oak.json"))
                .get("group").getAsString();
        assertEquals(group, recipe(RECIPES.resolve("cfm_classic_oak.json")).get("group").getAsString());
    }

    @Test public void customBedRecipesDoNotGroupDifferentWoods() {
        assertEquals("minecraft:planks/bed_recolour", new BedRecolourRecipe(Blocks.PLANKS).getGroup());
        assertNotEquals(new BedRecolourRecipe(Blocks.PLANKS).getGroup(),
                new BedRecolourRecipe(Blocks.BRICK_BLOCK).getGroup());
        assertEquals("minecraft:brick_block/matching_upholstery",
                new MatchingUpholsteryRecipe(Blocks.PLANKS, Blocks.BRICK_BLOCK).getGroup());
        assertNotEquals(new MatchingUpholsteryRecipe(Blocks.PLANKS, Blocks.BRICK_BLOCK).getGroup(),
                new MatchingUpholsteryRecipe(Blocks.PLANKS, Blocks.STONE).getGroup());
    }

    @Test public void shieldChairRecipesUseTheirFinishedChairGroup() throws Exception {
        Constructor<ShieldChairRecipe> constructor = ShieldChairRecipe.class
                .getDeclaredConstructor(Block.class, Block.class);
        constructor.setAccessible(true);
        assertEquals("minecraft:brick_block/meta_0",
                constructor.newInstance(Blocks.PLANKS, Blocks.BRICK_BLOCK).getGroup());
        assertNotEquals(constructor.newInstance(Blocks.PLANKS, Blocks.BRICK_BLOCK).getGroup(),
                constructor.newInstance(Blocks.PLANKS, Blocks.STONE).getGroup());
    }
}
