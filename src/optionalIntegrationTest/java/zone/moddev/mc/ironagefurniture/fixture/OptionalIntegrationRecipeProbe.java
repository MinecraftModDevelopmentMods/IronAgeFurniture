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
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.Items.ItemBlockMetalVariant;
import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper;
import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper.MetalVariant;
import zone.moddev.mc.ironagefurniture.api.tile.TileEntityMetalVariant;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.oredict.OreDictionary;

/** Exact-loader probe for legacy Java-side optional registration and recipes. */
@Mod(modid = OptionalIntegrationRecipeProbe.MOD_ID,
        name = "Iron Age Furniture Optional Integration Probe",
        version = "1",
        acceptableRemoteVersions = "*",
        dependencies = "required-after:ironagefurniture@[0.4.0.110021]")
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
        int hiddenLightingBlocks = verifyHiddenLightingItems();
        if (Boolean.getBoolean("iaf.probe.metalSconce")) {
            verifyMetalSconce(server);
        }
        if (Boolean.getBoolean("iaf.probe.metalRecipes")) {
            verifyMetalRecipes();
        }
        writeMarker(requested, versions, blockCounts, recipeCounts, hiddenLightingBlocks);
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

    private static int verifyHiddenLightingItems() {
        List<String> unexpectedItems = new ArrayList<>();
        List<String> missingBlocks = new ArrayList<>();
        List<String> expected = readResourceLines("expected-hidden-lighting-blocks.txt");
        for (String value : expected) {
            ResourceLocation id = new ResourceLocation(value);
            if (!Block.REGISTRY.containsKey(id)) missingBlocks.add(value);
            if (Item.REGISTRY.containsKey(id)) unexpectedItems.add(value);
        }
        require(missingBlocks.isEmpty(),
                "Hidden lighting state blocks did not register: " + summarize(missingBlocks));
        require(unexpectedItems.isEmpty(),
                "Hidden lighting state blocks registered ItemBlocks: " + summarize(unexpectedItems));
        return expected.size();
    }

    private static void verifyMetalSconce(MinecraftServer server) {
        require(MetalVariant.ADAMANTINE.isAvailable(),
                "Adamantine is unavailable in the installed Base Metals jar");
        WorldServer world = (WorldServer) server.getEntityWorld();
        BlockPos pos = world.getSpawnPoint().add(8, 48, 8);
        Block block = BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron;
        ItemBlock item = (ItemBlock) Item.getItemFromBlock(block);
        require(item instanceof ItemBlockMetalVariant, "Sconce item has no metal subtypes");
        FakePlayer player = FakePlayerFactory.getMinecraft(world);
        player.setHeldItem(EnumHand.MAIN_HAND,
                new ItemStack(net.minecraft.init.Items.IRON_PICKAXE));
        ItemStack sconce = new ItemStack(item, 1, MetalVariant.ADAMANTINE.getMeta());
        IBlockState placedState = block.getDefaultState();
        world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 3);
        try {
            require(item.placeBlockAt(sconce, player, world, pos, EnumFacing.UP,
                    0.5F, 0.5F, 0.5F, placedState), "Adamantine sconce placement failed");
            require(MetalVariantHelper.getMetal(world, pos) == MetalVariant.ADAMANTINE,
                    "Placed Adamantine sconce lost its metal");
            TileEntity tile = world.getTileEntity(pos);
            require(tile instanceof TileEntityMetalVariant, "Placed sconce has no metal tile entity");
            require("adamantine".equals(tile.writeToNBT(new net.minecraft.nbt.NBTTagCompound())
                    .getString("Metal")), "Placed sconce saved the wrong metal");
            float hardness = block.getBlockHardness(world.getBlockState(pos), world, pos);
            ItemStack picked = block.getItem(world, pos, world.getBlockState(pos));
            IBlockState state = world.getBlockState(pos);
            require(block.canHarvestBlock(world, pos, player),
                    "Iron pickaxe cannot harvest the Adamantine sconce");
            float miningProgress = state.getPlayerRelativeBlockHardness(player, world, pos);
            require(block.removedByPlayer(state, world, pos, player, true),
                    "Adamantine sconce could not be harvested");
            List<ItemStack> drops = block.getDrops(world, pos, state, 0);
            require(drops.size() == 1 && drops.get(0).getMetadata() == MetalVariant.ADAMANTINE.getMeta(),
                    "Harvested Adamantine sconce dropped the wrong metal: " + drops);
            require(picked != null && picked.getMetadata() == MetalVariant.ADAMANTINE.getMeta(),
                    "Pick-block lost the Adamantine metal");
            require(hardness >= 4.0F,
                    "Adamantine sconce mines too quickly with an iron pickaxe: " + hardness);
            require(miningProgress > 0.0F && miningProgress <= 0.05F,
                    "Iron pickaxe mines the Adamantine sconce in under one second: " + miningProgress);
            AxisAlignedBB dropArea = new AxisAlignedBB(pos).expand(1.0D, 1.0D, 1.0D);
            Set<Integer> existingItems = new HashSet<>();
            for (EntityItem entity : world.getEntitiesWithinAABB(EntityItem.class, dropArea)) {
                existingItems.add(entity.getEntityId());
            }
            block.harvestBlock(world, player, pos, state, tile,
                    new ItemStack(net.minecraft.init.Items.IRON_PICKAXE));
            int actualDrops = 0;
            for (EntityItem entity : world.getEntitiesWithinAABB(EntityItem.class, dropArea)) {
                if (existingItems.contains(entity.getEntityId())) continue;
                ItemStack dropped = entity.getEntityItem();
                require(dropped.getItem() == item
                        && dropped.getMetadata() == MetalVariant.ADAMANTINE.getMeta(),
                        "Harvest spawned a non-Adamantine sconce item: " + dropped);
                actualDrops++;
            }
            require(actualDrops == 1, "Harvest spawned " + actualDrops + " Adamantine sconces");
            LOGGER.info("Metal sconce probe passed: Adamantine hardness {}, iron-pick progress {} per tick",
                    hardness, miningProgress);
        } finally {
            world.setBlockToAir(pos);
            world.setBlockToAir(pos.down());
        }
    }

    private static void verifyMetalRecipes() {
        Item sconce = Item.getItemFromBlock(
                BlockObjectHolder.light_metal_ironage_sconce_floor_empty_iron);
        int checked = 0;
        for (MetalVariant metal : MetalVariantHelper.getAvailableVariants()) {
            if (metal == MetalVariant.IRON) continue;
            List<String> matchingTags = new ArrayList<>();
            if (metal != MetalVariant.GOLD) {
                for (String tag : OreDictionary.getOreNames()) {
                    if (tag.equalsIgnoreCase("nugget" + metal.name().toLowerCase(Locale.ENGLISH))
                            && !OreDictionary.getOres(tag).isEmpty()) {
                        matchingTags.add(tag);
                    }
                }
                require(matchingTags.contains(metal.getNuggetOreName()),
                        "No matching nugget tag for " + metal.name() + ": requested "
                                + metal.getNuggetOreName() + ", found " + matchingTags);
            }
            int recipes = 0;
            for (IRecipe recipe : CraftingManager.getInstance().getRecipeList()) {
                ItemStack output = recipe.getRecipeOutput();
                if (output != null && output.getItem() == sconce
                        && output.getMetadata() == metal.getMeta()) recipes++;
            }
            require(recipes == 1, "Expected one sconce recipe for " + metal.name()
                    + ", found " + recipes);
            checked++;
        }
        LOGGER.info("Metal sconce recipe probe passed: {} available metals each have one recipe", checked);
    }

    private static List<ExpectedEntry> readExpectedEntries(String resourceName) {
        List<String> lines = readResourceLines(resourceName);
        List<ExpectedEntry> result = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("=", 2);
            require(parts.length == 2, "Invalid probe entry: " + line);
            result.add(new ExpectedEntry(parts[0], new ResourceLocation(parts[1])));
        }
        return result;
    }

    private static List<String> readResourceLines(String resourceName) {
        InputStream stream = OptionalIntegrationRecipeProbe.class.getClassLoader()
                .getResourceAsStream(resourceName);
        require(stream != null, "Missing probe resource " + resourceName);
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.trim().isEmpty()) continue;
                result.add(line);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read probe resource " + resourceName,
                    exception);
        }
        return result;
    }

    private static void writeMarker(String profile, Map<String, String> versions,
            Map<String, Integer> blocks, Map<String, Integer> recipes,
            int hiddenLightingBlocks) {
        StringBuilder result = new StringBuilder()
                .append("status=PASS\n")
                .append("profile=").append(profile).append('\n')
                .append("optional_blocks_loaded=").append(total(blocks)).append('\n')
                .append("optional_recipe_outputs_loaded=").append(total(recipes)).append('\n')
                .append("hidden_lighting_blocks_without_items=")
                .append(hiddenLightingBlocks).append('\n');
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
