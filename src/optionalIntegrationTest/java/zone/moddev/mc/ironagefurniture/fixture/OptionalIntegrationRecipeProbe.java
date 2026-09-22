package zone.moddev.mc.ironagefurniture.fixture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

/** Exact-loader probe for optional recipes and recipe advancements. */
@Mod(modid = OptionalIntegrationRecipeProbe.MOD_ID,
        name = "Iron Age Furniture Optional Integration Probe",
        version = "1",
        acceptableRemoteVersions = "*",
        dependencies = "required-after:ironagefurniture@[0.3.0.112021]")
public final class OptionalIntegrationRecipeProbe {
    public static final String MOD_ID = "ironagefurnitureintegrationprobe";

    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<String> REQUIRED_MODS = Arrays.asList(
            "biomesoplenty", "natura", "forestry", "immersiveengineering");

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if ("absent".equals(System.getProperty("iaf.probe.profile"))) {
            verifyOptionalModsAbsent();
            verifyRecipesAbsent("expected-conditional-recipes.txt");
            verifyAdvancementsAbsent(server, "expected-conditional-advancements.txt");
            writeAbsentMarker();
            LOGGER.info("IRON AGE FURNITURE CORE ABSENCE PROBE PASSED");
            server.initiateShutdown();
            return;
        }
        List<String> selectedMods = selectedMods();
        Map<String, String> versions = requireMods(selectedMods);
        Map<String, Integer> recipes = verifyRecipes(
                "expected-conditional-recipes.txt", selectedMods);
        Map<String, Integer> advancements = verifyAdvancements(server,
                "expected-conditional-advancements.txt", selectedMods);

        int recipeCount = total(recipes);
        int advancementCount = total(advancements);
        int expectedCount = expectedCount(selectedMods);
        require(recipeCount == expectedCount,
                "Expected " + expectedCount
                        + " conditional recipes, found " + recipeCount);
        require(advancementCount == expectedCount,
                "Expected " + expectedCount
                        + " conditional advancements, found " + advancementCount);

        writeMarker(versions, recipes, advancements, recipeCount, advancementCount);
        LOGGER.info("IRON AGE FURNITURE OPTIONAL INTEGRATION PROBE PASSED: "
                + "{} recipes and {} advancements", recipeCount, advancementCount);
        server.initiateShutdown();
    }

    private static void verifyOptionalModsAbsent() {
        for (String modId : REQUIRED_MODS) {
            require(!Loader.isModLoaded(modId),
                    "Optional mod unexpectedly loaded during absence probe: " + modId);
        }
    }

    private static void verifyRecipesAbsent(String resourceName) {
        List<String> unexpected = new ArrayList<>();
        for (ExpectedEntry expected : readExpectedEntries(resourceName)) {
            if (CraftingManager.REGISTRY.getObject(expected.id) != null) {
                unexpected.add(expected.id.toString());
            }
        }
        require(unexpected.isEmpty(),
                "Conditional recipes loaded without their mods: " + summarize(unexpected));
    }

    private static void verifyAdvancementsAbsent(MinecraftServer server, String resourceName) {
        List<String> unexpected = new ArrayList<>();
        for (ExpectedEntry expected : readExpectedEntries(resourceName)) {
            if (server.getAdvancementManager().getAdvancement(expected.id) != null) {
                unexpected.add(expected.id.toString());
            }
        }
        require(unexpected.isEmpty(),
                "Conditional advancements loaded without their mods: " + summarize(unexpected));
    }

    private static List<String> selectedMods() {
        String profile = System.getProperty("iaf.probe.mods", "all");
        if ("all".equals(profile)) return REQUIRED_MODS;
        require(REQUIRED_MODS.contains(profile), "Unknown optional-integration profile " + profile);
        return Arrays.asList(profile);
    }

    private static Map<String, String> requireMods(List<String> selectedMods) {
        Map<String, String> versions = new LinkedHashMap<>();
        Map<String, ModContainer> loadedMods = Loader.instance().getIndexedModList();
        for (String modId : REQUIRED_MODS) {
            ModContainer container = loadedMods.get(modId);
            if (selectedMods.contains(modId)) {
                require(container != null,
                        "Required optional-integration test mod is not loaded: " + modId);
                versions.put(modId, container.getVersion());
            } else {
                require(container == null,
                        "Unselected optional-integration mod is loaded: " + modId);
            }
        }
        return versions;
    }

    private static Map<String, Integer> verifyRecipes(String resourceName,
            List<String> selectedMods) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        List<String> missing = new ArrayList<>();
        List<String> unexpected = new ArrayList<>();
        for (ExpectedEntry expected : readExpectedEntries(resourceName)) {
            boolean present = CraftingManager.REGISTRY.getObject(expected.id) != null;
            if (selectedMods.contains(expected.modId)) {
                increment(counts, expected.modId);
                if (!present) missing.add(expected.id.toString());
            } else if (present) {
                unexpected.add(expected.id.toString());
            }
        }
        require(missing.isEmpty(), "Conditional recipes did not load: " + summarize(missing));
        require(unexpected.isEmpty(),
                "Unselected conditional recipes loaded: " + summarize(unexpected));
        return counts;
    }

    private static Map<String, Integer> verifyAdvancements(MinecraftServer server,
            String resourceName, List<String> selectedMods) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        List<String> missing = new ArrayList<>();
        List<String> unexpected = new ArrayList<>();
        for (ExpectedEntry expected : readExpectedEntries(resourceName)) {
            boolean present = server.getAdvancementManager().getAdvancement(expected.id) != null;
            if (selectedMods.contains(expected.modId)) {
                increment(counts, expected.modId);
                if (!present) missing.add(expected.id.toString());
            } else if (present) {
                unexpected.add(expected.id.toString());
            }
        }
        require(missing.isEmpty(),
                "Conditional recipe advancements did not load: " + summarize(missing));
        require(unexpected.isEmpty(),
                "Unselected conditional advancements loaded: " + summarize(unexpected));
        return counts;
    }

    private static int expectedCount(List<String> selectedMods) {
        int count = 0;
        for (String modId : selectedMods) {
            if ("biomesoplenty".equals(modId)) count += 624;
            else if ("natura".equals(modId)) count += 468;
            else if ("forestry".equals(modId)) count += 1131;
            else if ("immersiveengineering".equals(modId)) count += 48;
        }
        return count;
    }

    private static List<ExpectedEntry> readExpectedEntries(String resourceName) {
        InputStream stream = OptionalIntegrationRecipeProbe.class.getClassLoader()
                .getResourceAsStream(resourceName);
        require(stream != null, "Missing probe resource " + resourceName);
        List<ExpectedEntry> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("=", 2);
                require(parts.length == 2, "Invalid probe entry: " + line);
                result.add(new ExpectedEntry(parts[0], new ResourceLocation(parts[1])));
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read probe resource " + resourceName,
                    exception);
        }
        return result;
    }

    private static void writeMarker(Map<String, String> versions,
            Map<String, Integer> recipes, Map<String, Integer> advancements,
            int recipeCount, int advancementCount) {
        StringBuilder result = new StringBuilder()
                .append("status=PASS\n")
                .append("conditional_recipes_loaded=").append(recipeCount).append('\n')
                .append("conditional_advancements_loaded=").append(advancementCount).append('\n');
        for (String modId : REQUIRED_MODS) {
            result.append("mod.").append(modId).append('=')
                    .append(versions.get(modId)).append('\n')
                    .append("recipes.").append(modId).append('=')
                    .append(valueOrZero(recipes, modId)).append('\n')
                    .append("advancements.").append(modId).append('=')
                    .append(valueOrZero(advancements, modId)).append('\n');
        }
        try {
            Files.write(Paths.get("optional-integration-pass.properties"),
                    result.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException exception) {
            throw new IllegalStateException("Could not write optional-integration marker",
                    exception);
        }
    }

    private static void writeAbsentMarker() {
        String result = "status=PASS\n"
                + "profile=absent\n"
                + "conditional_recipes_loaded=0\n"
                + "conditional_advancements_loaded=0\n";
        try {
            Files.write(Paths.get("optional-integration-pass.properties"),
                    result.getBytes(StandardCharsets.UTF_8));
        } catch (IOException exception) {
            throw new IllegalStateException("Could not write core absence marker", exception);
        }
    }

    private static void increment(Map<String, Integer> counts, String key) {
        counts.put(key, valueOrZero(counts, key) + 1);
    }

    private static int valueOrZero(Map<String, Integer> counts, String key) {
        Integer value = counts.get(key);
        return value == null ? 0 : value;
    }

    private static int total(Map<String, Integer> counts) {
        int result = 0;
        for (Integer value : counts.values()) result += value;
        return result;
    }

    private static String summarize(List<String> missing) {
        int limit = Math.min(missing.size(), 10);
        return missing.subList(0, limit) + (missing.size() > limit
                ? " (and " + (missing.size() - limit) + " more)" : "");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }

    private static final class ExpectedEntry {
        private final String modId;
        private final ResourceLocation id;

        private ExpectedEntry(String modId, ResourceLocation id) {
            this.modId = modId;
            this.id = id;
        }
    }
}
