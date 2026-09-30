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
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.BlockObjectHolder;
import zone.moddev.mc.ironagefurniture.api.MetalVariantHelper;
import zone.moddev.mc.ironagefurniture.api.entity.EntityReleasedLavaLamp;

/** Exact-loader probe for optional recipes and recipe advancements. */
@Mod(modid = OptionalIntegrationRecipeProbe.MOD_ID,
        name = "Iron Age Furniture Optional Integration Probe",
        version = "1",
        acceptableRemoteVersions = "*",
        dependencies = "required-after:ironagefurniture@[0.4.0.112021]")
public final class OptionalIntegrationRecipeProbe {
    public static final String MOD_ID = "ironagefurnitureintegrationprobe";

    private static final Logger LOGGER = LogManager.getLogger();
    private BlockPos lavaTrapPos;
    private boolean lavaTrapActive;
    private boolean sawReleasedLamp;
    private boolean sawLavaTrapFire;
    private double lastReleasedLampY;
    private double lastReleasedLampZ;
    private int lavaTrapTicks;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (Boolean.getBoolean("iaf.probe.verifyLavaTrap")) {
            FMLCommonHandler.instance().bus().register(this);
        }
    }
    private static final List<String> REQUIRED_MODS = Arrays.asList(
            "biomesoplenty", "natura", "forestry", "immersiveengineering", "mineralogy",
            "basemetals", "cfm");

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        verifyCandleSconces();
        verifyMetalVariants(!"absent".equals(System.getProperty("iaf.probe.profile"))
                && ("basemetals".equals(System.getProperty("iaf.probe.mods"))
                        || "all".equals(System.getProperty("iaf.probe.mods"))));
        if ("absent".equals(System.getProperty("iaf.probe.profile"))) {
            verifyOptionalModsAbsent();
            verifyRockSaltSconces(false);
            verifyRecipesAbsent("expected-conditional-recipes.txt");
            verifyAdvancementsAbsent(server, "expected-conditional-advancements.txt");
            if (Files.isRegularFile(Paths.get("cfm-migration-fixture.properties"))) {
                verifyCfmMigrationFixture(server);
            }
            if (Boolean.getBoolean("iaf.probe.verifyLavaTrap")) {
                beginLavaTrapProbe(server);
                return;
            }
            writeAbsentMarker();
            LOGGER.info("IRON AGE FURNITURE CORE ABSENCE PROBE PASSED");
            server.initiateShutdown();
            return;
        }
        List<String> selectedMods = selectedMods();
        Map<String, String> versions = requireMods(selectedMods);
        verifyRockSaltSconces(selectedMods.contains("mineralogy"));
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

        if (selectedMods.contains("cfm")
                && Boolean.getBoolean("iaf.probe.createCfmMigrationFixture")) {
            createCfmMigrationFixture(server);
        }
        if (selectedMods.contains("cfm")
                && Boolean.getBoolean("iaf.probe.verifyCfmSourceFixture")) {
            verifyCfmSourceFixture(server);
        }
        if (selectedMods.contains("cfm")
                && Boolean.getBoolean("iaf.probe.verifyCfmMigrationFixture")) {
            verifyCfmMigrationFixture(server);
        }

        writeMarker(versions, recipes, advancements, recipeCount, advancementCount);
        LOGGER.info("IRON AGE FURNITURE OPTIONAL INTEGRATION PROBE PASSED: "
                + "{} recipes and {} advancements", recipeCount, advancementCount);
        server.initiateShutdown();
    }

    private void beginLavaTrapProbe(MinecraftServer server) {
        WorldServer world = server.getWorld(0);
        BlockPos spawn = world.getSpawnPoint();
        lavaTrapPos = new BlockPos(spawn.getX(), 100, spawn.getZ());
        Block lavaSconce = BlockObjectHolder.light_metal_ironage_sconce_wall_lava_iron;
        require(lavaSconce != null, "Wall lava sconce is not registered");
        world.setBlockToAir(lavaTrapPos.up());
        for (int x = lavaTrapPos.getX() - 1; x <= lavaTrapPos.getX() + 1; ++x) {
            for (int z = lavaTrapPos.getZ() - 1; z <= lavaTrapPos.getZ() + 1; ++z) {
                for (int y = 97; y <= 100; ++y) {
                    world.setBlockToAir(new BlockPos(x, y, z));
                }
                world.setBlockState(new BlockPos(x, 96, z), Blocks.STONE.getDefaultState(), 3);
            }
        }
        // The registered default faces north, so its wall support is south.
        world.setBlockState(lavaTrapPos.south(), Blocks.STONE.getDefaultState(), 3);
        world.setBlockState(lavaTrapPos, lavaSconce.getDefaultState(), 3);
        require(world.getBlockState(lavaTrapPos).getBlock() == lavaSconce,
                "Could not place wall lava sconce for trap probe");
        world.setBlockState(lavaTrapPos.up(), Blocks.REDSTONE_BLOCK.getDefaultState(), 3);
        require(world.isBlockPowered(lavaTrapPos), "Trap sconce is not powered");
        require(world.isAirBlock(lavaTrapPos.down()), "Trap lamp has no falling clearance");
        lavaTrapActive = true;
    }

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        if (!lavaTrapActive || event.phase != TickEvent.Phase.END) return;
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        WorldServer world = server.getWorld(0);
        ++lavaTrapTicks;
        for (Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityReleasedLavaLamp) {
                sawReleasedLamp = true;
                lastReleasedLampY = entity.posY;
                lastReleasedLampZ = entity.posZ;
            }
        }
        for (int x = lavaTrapPos.getX() - 1; x <= lavaTrapPos.getX() + 1; ++x) {
            for (int z = lavaTrapPos.getZ() - 1; z <= lavaTrapPos.getZ() + 1; ++z) {
                for (int y = 97; y <= 100; ++y) {
                    sawLavaTrapFire |= world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.FIRE;
                }
            }
        }
        if (lavaTrapTicks < 60) return;

        Block holder = world.getBlockState(lavaTrapPos).getBlock();
        require(holder == BlockObjectHolder.light_metal_ironage_sconce_wall_empty_iron,
                "Powered lava sconce did not become an empty holder: " + holder.getRegistryName()
                        + ", powered=" + world.isBlockPowered(lavaTrapPos)
                        + ", airBelow=" + world.isAirBlock(lavaTrapPos.down())
                        + ", sawFalling=" + sawReleasedLamp);
        require(sawReleasedLamp, "Released lava lamp never entered the world as a falling entity");
        require(sawLavaTrapFire, "Released lava lamp did not shatter into fire below its holder"
                + "; lastY=" + lastReleasedLampY + ", lastZ=" + lastReleasedLampZ
                + ", landingBlock=" + world.getBlockState(lavaTrapPos.down(3)).getBlock().getRegistryName());
        writeAbsentMarker();
        LOGGER.info("IRON AGE FURNITURE LAVA SCONCE TRAP PROBE PASSED");
        lavaTrapActive = false;
        server.initiateShutdown();
    }

    private static void verifyOptionalModsAbsent() {
        for (String modId : REQUIRED_MODS) {
            require(!Loader.isModLoaded(modId),
                    "Optional mod unexpectedly loaded during absence probe: " + modId);
        }
    }

    private static void verifyRockSaltSconces(boolean expected) {
        for (String form : Arrays.asList("floor", "wall")) {
            ResourceLocation id = new ResourceLocation("ironagefurniture",
                    "light_metal_ironage_sconce_" + form + "_rocksalt_iron");
            boolean present = ForgeRegistries.BLOCKS.containsKey(id);
            require(present == expected,
                    "Rock-salt sconce registration does not match Mineralogy presence: " + id);
        }
    }

    private static final String[] CFM_WOODS = {
            "oak", "spruce", "birch", "jungle", "acacia", "dark_oak"};

    private static void createCfmMigrationFixture(MinecraftServer server) {
        WorldServer world = server.getWorld(0);
        for (int i = 0; i < CFM_WOODS.length; i++) {
            BlockPos pos = new BlockPos(32 + i, 100, 32);
            Block chair = ForgeRegistries.BLOCKS.getValue(
                    new ResourceLocation("cfm", "chair_" + CFM_WOODS[i]));
            require(chair != null, "Published CFM chair is missing: " + CFM_WOODS[i]);
            world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 3);
            world.setBlockState(pos, chair.getStateFromMeta(i % 4), 3);
            require(world.getBlockState(pos).getBlock() == chair,
                    "Could not place CFM fixture chair " + CFM_WOODS[i]);
        }

        BlockPos chestPos = new BlockPos(40, 100, 32);
        world.setBlockState(chestPos.down(), Blocks.STONE.getDefaultState(), 3);
        world.setBlockState(chestPos, Blocks.CHEST.getDefaultState(), 3);
        TileEntityChest chest = (TileEntityChest) world.getTileEntity(chestPos);
        Item chairItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation("cfm", "chair_oak"));
        require(chairItem != null, "Published CFM chair item is missing");
        chest.setInventorySlotContents(0, new ItemStack(chairItem, 3));

        NBTTagCompound nestedChair = new ItemStack(chairItem, 2).writeToNBT(new NBTTagCompound());
        nestedChair.setByte("Slot", (byte) 0);
        NBTTagList nestedItems = new NBTTagList();
        nestedItems.appendTag(nestedChair);
        NBTTagCompound nestedBlockEntity = new NBTTagCompound();
        nestedBlockEntity.setTag("Items", nestedItems);
        NBTTagCompound nestedTag = new NBTTagCompound();
        nestedTag.setTag("BlockEntityTag", nestedBlockEntity);
        ItemStack container = new ItemStack(Blocks.CHEST);
        container.setTagCompound(nestedTag);
        chest.setInventorySlotContents(1, container);
        chest.markDirty();
        try {
            Files.write(Paths.get("cfm-migration-fixture.properties"),
                    Arrays.asList("source=cfm-6.3.2", "chairs=6", "stored_items=2"),
                    StandardCharsets.UTF_8);
        } catch (IOException error) {
            throw new IllegalStateException("Could not mark disposable CFM fixture", error);
        }
    }

    private static void verifyCfmMigrationFixture(MinecraftServer server) {
        WorldServer world = server.getWorld(0);
        for (int i = 0; i < CFM_WOODS.length; i++) {
            BlockPos pos = new BlockPos(32 + i, 100, 32);
            String wood = "dark_oak".equals(CFM_WOODS[i]) ? "big_oak" : CFM_WOODS[i];
            ResourceLocation expected = new ResourceLocation("ironagefurniture",
                    "chair_wood_ironage_classic_" + wood);
            Block block = world.getBlockState(pos).getBlock();
            require(expected.equals(block.getRegistryName()),
                    "CFM chair was not migrated at " + pos + ": " + block.getRegistryName());
            require(block.getMetaFromState(world.getBlockState(pos)) == i % 4,
                    "CFM chair facing changed at " + pos);
        }
        TileEntityChest chest = (TileEntityChest) world.getTileEntity(new BlockPos(40, 100, 32));
        require(chest != null, "CFM migration fixture chest is missing");
        ItemStack direct = chest.getStackInSlot(0);
        ResourceLocation oak = new ResourceLocation("ironagefurniture", "chair_wood_ironage_classic_oak");
        require(!direct.isEmpty() && oak.equals(direct.getItem().getRegistryName())
                        && direct.getCount() == 3,
                "Stored CFM chair was not migrated");
        ItemStack container = chest.getStackInSlot(1);
        require(!container.isEmpty() && container.hasTagCompound(), "Nested CFM fixture is missing");
        NBTTagCompound nested = container.getTagCompound().getCompoundTag("BlockEntityTag")
                .getTagList("Items", 10).getCompoundTagAt(0);
        require(oak.toString().equals(nested.getString("id")) && nested.getByte("Count") == 2,
                "Nested CFM chair was not migrated");
    }

    private static void verifyCfmSourceFixture(MinecraftServer server) {
        WorldServer world = server.getWorld(0);
        for (int i = 0; i < CFM_WOODS.length; i++) {
            BlockPos pos = new BlockPos(32 + i, 100, 32);
            ResourceLocation expected = new ResourceLocation("cfm", "chair_" + CFM_WOODS[i]);
            Block block = world.getBlockState(pos).getBlock();
            require(expected.equals(block.getRegistryName()),
                    "CFM chair converted without opt-in at " + pos);
            require(block.getMetaFromState(world.getBlockState(pos)) == i % 4,
                    "CFM chair facing changed at " + pos);
        }
        TileEntityChest chest = (TileEntityChest) world.getTileEntity(new BlockPos(40, 100, 32));
        require(chest != null, "CFM source fixture chest is missing");
        ItemStack direct = chest.getStackInSlot(0);
        ResourceLocation oak = new ResourceLocation("cfm", "chair_oak");
        require(!direct.isEmpty() && oak.equals(direct.getItem().getRegistryName())
                        && direct.getCount() == 3,
                "CFM chair stack converted without opt-in");
        ItemStack container = chest.getStackInSlot(1);
        require(!container.isEmpty() && container.hasTagCompound(), "Nested CFM source fixture is missing");
        NBTTagCompound nested = container.getTagCompound().getCompoundTag("BlockEntityTag")
                .getTagList("Items", 10).getCompoundTagAt(0);
        require(oak.toString().equals(nested.getString("id")) && nested.getByte("Count") == 2,
                "Nested CFM chair converted without opt-in");
    }

    private static void verifyMetalVariants(boolean baseMetalsExpected) {
        int expected = baseMetalsExpected ? 23 : 2;
        require(MetalVariantHelper.getAvailableVariants().size() == expected,
                "Unexpected sconce metal catalog with Base Metals "
                        + (baseMetalsExpected ? "installed" : "absent"));
        require(MetalVariantHelper.METAL.getAllowedValues().size() == expected,
                "Sconce blockstates were initialized before the metal catalog was ready");
    }

    private static void verifyCandleSconces() {
        for (String form : Arrays.asList("floor", "wall")) {
            for (String count : Arrays.asList("", "_two", "_three", "_four")) {
                for (String state : Arrays.asList("", "_unlit")) {
                    ResourceLocation id = new ResourceLocation("ironagefurniture",
                            "light_metal_ironage_sconce_" + form + "_candle_iron" + count + state);
                    require(ForgeRegistries.BLOCKS.containsKey(id),
                            "Candle sconce state is missing: " + id);
                    require(!ForgeRegistries.ITEMS.containsKey(id),
                            "Hidden candle sconce has an item registration: " + id);
                }
            }
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
            if ("biomesoplenty".equals(modId)) count += 1504;
            else if ("natura".equals(modId)) count += 1128;
            else if ("forestry".equals(modId)) count += 2726;
            else if ("immersiveengineering".equals(modId)) count += 103;
            else if ("basemetals".equals(modId)) count += 21;
            else if ("cfm".equals(modId)) count += 6;
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
