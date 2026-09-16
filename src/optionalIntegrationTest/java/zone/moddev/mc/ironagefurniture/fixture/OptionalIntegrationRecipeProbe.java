package zone.moddev.mc.ironagefurniture.fixture;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Exact-loader probe for optional recipe and recipe-advancement conditions. */
@Mod(OptionalIntegrationRecipeProbe.MOD_ID)
public final class OptionalIntegrationRecipeProbe
{
    public static final String MOD_ID = "ironagefurnitureintegrationprobe";

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<String> REQUIRED_DEPENDENCIES = List.of(
            "terrablender", "glitchcore");

    public OptionalIntegrationRecipeProbe()
    {
        NeoForge.EVENT_BUS.addListener(this::serverStarted);
    }

    private void serverStarted(ServerStartedEvent event)
    {
        MinecraftServer server = event.getServer();
        Map<String, Integer> recipes = verifyRecipes(server,
                "expected-conditional-recipes.txt");
        Map<String, Integer> advancements = verifyAdvancements(server,
                "expected-conditional-advancements.txt");
        require(recipes.keySet().equals(advancements.keySet()),
                "Recipe and advancement integrations do not match");
        Map<String, String> versions = requireMods(recipes.keySet());

        int recipeCount = recipes.values().stream().mapToInt(Integer::intValue).sum();
        int advancementCount = advancements.values().stream().mapToInt(Integer::intValue).sum();
        require(recipeCount > 0 && recipeCount >= advancementCount,
                "Conditional recipe/advancement counts are empty or invalid: "
                        + recipeCount + "/" + advancementCount);

        writeMarker(versions, recipes, advancements, recipeCount, advancementCount);
        LOGGER.info("IRON AGE FURNITURE OPTIONAL INTEGRATION PROBE PASSED: "
                + "{} recipes and {} advancements", recipeCount, advancementCount);
        server.halt(false);
    }

    private static Map<String, String> requireMods(Set<String> integrationMods)
    {
        require(!integrationMods.isEmpty(), "No optional integration was selected for the probe");
        Map<String, String> versions = new LinkedHashMap<>();
        List<String> requiredMods = new ArrayList<>(integrationMods);
        requiredMods.addAll(REQUIRED_DEPENDENCIES);
        for (String modId : requiredMods)
        {
            String version = ModList.get().getModContainerById(modId)
                    .map(container -> container.getModInfo().getVersion().toString())
                    .orElseThrow(() -> new IllegalStateException(
                            "Required optional-integration test mod is not loaded: " + modId));
            versions.put(modId, version);
        }
        return versions;
    }

    private static Map<String, Integer> verifyRecipes(MinecraftServer server,
                                                       String resourceName)
    {
        Map<String, Integer> counts = new LinkedHashMap<>();
        List<String> missing = new ArrayList<>();
        for (ExpectedEntry expected : readExpectedEntries(resourceName))
        {
            counts.merge(expected.modId(), 1, Integer::sum);
            ResourceKey<Recipe<?>> recipeKey = ResourceKey.create(Registries.RECIPE, expected.id());
            if (server.getRecipeManager().byKey(recipeKey).isEmpty())
            {
                missing.add(expected.id().toString());
            }
        }
        require(missing.isEmpty(), "Conditional recipes did not load: " + summarize(missing));
        return counts;
    }

    private static Map<String, Integer> verifyAdvancements(MinecraftServer server,
                                                           String resourceName)
    {
        Map<String, Integer> counts = new LinkedHashMap<>();
        List<String> missing = new ArrayList<>();
        for (ExpectedEntry expected : readExpectedEntries(resourceName))
        {
            counts.merge(expected.modId(), 1, Integer::sum);
            if (server.getAdvancements().get(expected.id()) == null)
            {
                missing.add(expected.id().toString());
            }
        }
        require(missing.isEmpty(),
                "Conditional recipe advancements did not load: " + summarize(missing));
        return counts;
    }

    private static List<ExpectedEntry> readExpectedEntries(String resourceName)
    {
        InputStream stream = OptionalIntegrationRecipeProbe.class.getClassLoader()
                .getResourceAsStream(resourceName);
        if (stream == null)
        {
            throw new IllegalStateException("Missing probe resource " + resourceName);
        }
        List<ExpectedEntry> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8)))
        {
            for (String line = reader.readLine(); line != null; line = reader.readLine())
            {
                if (line.isBlank()) continue;
                String[] parts = line.split("=", 2);
                require(parts.length == 2, "Invalid probe entry: " + line);
                result.add(new ExpectedEntry(parts[0], Identifier.parse(parts[1])));
            }
        }
        catch (IOException exception)
        {
            throw new IllegalStateException("Could not read probe resource " + resourceName,
                    exception);
        }
        return result;
    }

    private static void writeMarker(Map<String, String> versions,
                                    Map<String, Integer> recipes,
                                    Map<String, Integer> advancements,
                                    int recipeCount, int advancementCount)
    {
        StringBuilder result = new StringBuilder()
                .append("status=PASS\n")
                .append("conditional_recipes_loaded=").append(recipeCount).append('\n')
                .append("conditional_advancements_loaded=").append(advancementCount).append('\n');
        for (Map.Entry<String, String> version : versions.entrySet())
        {
            result.append("mod.").append(version.getKey()).append("=")
                    .append(version.getValue()).append('\n');
        }
        for (String modId : recipes.keySet())
        {
            result.append("recipes.").append(modId).append("=")
                    .append(recipes.getOrDefault(modId, 0)).append('\n');
            result.append("advancements.").append(modId).append("=")
                    .append(advancements.getOrDefault(modId, 0)).append('\n');
        }
        try
        {
            Files.writeString(Path.of("optional-integration-pass.properties"), result,
                    StandardCharsets.UTF_8);
        }
        catch (IOException exception)
        {
            throw new IllegalStateException("Could not write optional-integration marker",
                    exception);
        }
    }

    private static String summarize(List<String> missing)
    {
        int limit = Math.min(missing.size(), 10);
        return missing.subList(0, limit) + (missing.size() > limit
                ? " (and " + (missing.size() - limit) + " more)" : "");
    }

    private static void require(boolean condition, String message)
    {
        if (!condition) throw new IllegalStateException(message);
    }

    private record ExpectedEntry(String modId, Identifier id)
    {
    }
}
