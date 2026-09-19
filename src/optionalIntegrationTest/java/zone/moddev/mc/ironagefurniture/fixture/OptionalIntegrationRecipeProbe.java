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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

/** Exact-loader probe for legacy Java-side optional registration and recipes. */
@Mod(modid = OptionalIntegrationRecipeProbe.MOD_ID,
        name = "Iron Age Furniture Optional Integration Probe",
        version = "1",
        acceptableRemoteVersions = "*",
        dependencies = "required-after:ironagefurniture@[0.3.0.110021]")
public final class OptionalIntegrationRecipeProbe {
    public static final String MOD_ID = "ironagefurnitureintegrationprobe";

    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<String> PROFILES = Arrays.asList(
            "biomesoplenty", "natura", "forestry", "immersiveengineering");
    private static final Map<String, String> MOD_IDS = new LinkedHashMap<>();

    static {
        MOD_IDS.put("biomesoplenty", "BiomesOPlenty");
        MOD_IDS.put("natura", "natura");
        MOD_IDS.put("forestry", "forestry");
        MOD_IDS.put("immersiveengineering", "immersiveengineering");
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        String requested = System.getProperty("iaf.probe.mods", "all");
        boolean absenceProbe = "absent".equals(requested);
        List<String> selected = absenceProbe ? new ArrayList<String>()
                : "all".equals(requested) ? PROFILES : Arrays.asList(requested);
        require(absenceProbe || "all".equals(requested) || PROFILES.contains(requested),
                "Unknown optional-integration profile " + requested);

        Map<String, String> versions = verifyLoadedMods(selected);
        Map<String, Integer> blockCounts = verifyBlocks(selected);
        Map<String, Integer> recipeCounts = verifyRecipes(selected);
        writeMarker(requested, versions, blockCounts, recipeCounts);
        LOGGER.info("IRON AGE FURNITURE OPTIONAL INTEGRATION PROBE PASSED: {} blocks, {} recipes",
                total(blockCounts), total(recipeCounts));
        server.initiateShutdown();
    }

    private static Map<String, String> verifyLoadedMods(List<String> selected) {
        Map<String, String> versions = new LinkedHashMap<>();
        Map<String, ModContainer> loaded = Loader.instance().getIndexedModList();
        for (String profile : PROFILES) {
            String modId = MOD_IDS.get(profile);
            ModContainer container = loaded.get(modId);
            if (selected.contains(profile)) {
                require(container != null, "Required integration mod is not loaded: " + modId);
                versions.put(profile, container.getVersion());
            } else {
                require(container == null, "Unselected integration mod is loaded: " + modId);
            }
        }
        return versions;
    }

    private static Map<String, Integer> verifyBlocks(List<String> selected) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        List<String> missing = new ArrayList<>();
        List<String> unexpected = new ArrayList<>();
        for (ExpectedEntry expected : readExpectedEntries("expected-optional-blocks.txt")) {
            boolean present = Block.REGISTRY.containsKey(expected.id);
            if (selected.contains(expected.profile)) {
                increment(counts, expected.profile);
                if (!present) missing.add(expected.id.toString());
            } else if (present) {
                unexpected.add(expected.id.toString());
            }
        }
        require(missing.isEmpty(), "Optional furniture did not register: " + summarize(missing));
        require(unexpected.isEmpty(),
                "Optional furniture registered without its mod: " + summarize(unexpected));
        return counts;
    }

    private static Map<String, Integer> verifyRecipes(List<String> selected) {
        Set<ResourceLocation> outputs = new HashSet<>();
        for (IRecipe recipe : CraftingManager.getInstance().getRecipeList()) {
            ItemStack output = recipe.getRecipeOutput();
            if (output != null && output.getItem() != null
                    && output.getItem().getRegistryName() != null) {
                outputs.add(output.getItem().getRegistryName());
            }
        }
        Map<String, Integer> counts = new LinkedHashMap<>();
        List<String> missing = new ArrayList<>();
        List<String> unexpected = new ArrayList<>();
        for (ExpectedEntry expected : readExpectedEntries("expected-optional-recipes.txt")) {
            boolean present = outputs.contains(expected.id);
            if (selected.contains(expected.profile)) {
                increment(counts, expected.profile);
                if (!present) missing.add(expected.id.toString());
            } else if (present) {
                unexpected.add(expected.id.toString());
            }
        }
        require(missing.isEmpty(), "Optional recipes did not register: " + summarize(missing));
        require(unexpected.isEmpty(),
                "Optional recipes registered without their mod: " + summarize(unexpected));
        return counts;
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

    private static void writeMarker(String profile, Map<String, String> versions,
            Map<String, Integer> blocks, Map<String, Integer> recipes) {
        StringBuilder result = new StringBuilder()
                .append("status=PASS\n")
                .append("profile=").append(profile).append('\n')
                .append("optional_blocks_loaded=").append(total(blocks)).append('\n')
                .append("optional_recipe_outputs_loaded=").append(total(recipes)).append('\n');
        for (String name : PROFILES) {
            result.append("mod.").append(name).append('=')
                    .append(valueOrEmpty(versions, name)).append('\n')
                    .append("blocks.").append(name).append('=')
                    .append(valueOrZero(blocks, name)).append('\n')
                    .append("recipes.").append(name).append('=')
                    .append(valueOrZero(recipes, name)).append('\n');
        }
        try {
            Files.write(Paths.get("optional-integration-pass.properties"),
                    result.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException exception) {
            throw new IllegalStateException("Could not write optional-integration marker",
                    exception);
        }
    }

    private static void increment(Map<String, Integer> counts, String key) {
        counts.put(key, valueOrZero(counts, key) + 1);
    }

    private static int valueOrZero(Map<String, Integer> counts, String key) {
        Integer value = counts.get(key);
        return value == null ? 0 : value;
    }

    private static String valueOrEmpty(Map<String, String> values, String key) {
        String value = values.get(key);
        return value == null ? "" : value;
    }

    private static int total(Map<String, Integer> counts) {
        int result = 0;
        for (Integer value : counts.values()) result += value;
        return result;
    }

    private static String summarize(List<String> values) {
        int limit = Math.min(values.size(), 10);
        return values.subList(0, limit) + (values.size() > limit
                ? " (and " + (values.size() - limit) + " more)" : "");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }

    private static final class ExpectedEntry {
        private final String profile;
        private final ResourceLocation id;

        private ExpectedEntry(String profile, ResourceLocation id) {
            this.profile = profile;
            this.id = id;
        }
    }
}
