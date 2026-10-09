package zone.moddev.mc.ironagefurniture.fixture;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairTileEntity;

/** Checks real client-side models after resources and tile renderers have loaded. */
@Mod.EventBusSubscriber(modid = "ironagefurniturephasefourprobe", value = Dist.CLIENT)
public final class PhaseFourClientProbe {
    private static boolean complete;
    private static boolean worldStarted;
    private static int worldTicks;
    private static boolean discoveryStarted;
    private static int syncTicks;
    private static boolean bedDiscoveryStarted;
    private static int bedSyncTicks;
    private static boolean placementStarted;
    private static volatile boolean placementComplete;
    private static volatile Throwable placementFailure;
    private static volatile int placementCases;

    private PhaseFourClientProbe() { }

    @SubscribeEvent public static void draw(GuiScreenEvent.DrawScreenEvent.Post event) {
        Minecraft game = Minecraft.getInstance();
        // Forge may show its non-fatal loading-warnings screen before the menu.
        // Both screens appear after model baking; errors are deliberately not skipped.
        String screen = event.getGui().getClass().getSimpleName();
        if (complete || (!(event.getGui() instanceof MainMenuScreen)
                && !screen.equals("ModLoadingWarningScreen"))) return;
        if (inWorld()) {
            if (!worldStarted) {
                worldStarted = true;
                game.launchIntegratedServer("world", "IAF furniture test", Boolean.getBoolean("iaf.probe.tabsOnly")
                        ? new net.minecraft.world.WorldSettings(6L, net.minecraft.world.GameType.SURVIVAL, true, false, net.minecraft.world.WorldType.DEFAULT) : null);
            }
            return;
        }
        verify(game);
    }

    @SubscribeEvent public static void tick(net.minecraftforge.event.TickEvent.ClientTickEvent event) {
        Minecraft game = Minecraft.getInstance();
        if (complete || !inWorld()
                || event.phase != net.minecraftforge.event.TickEvent.Phase.END
                || game.world == null || game.player == null || ++worldTicks < 40) return;
        if (Boolean.getBoolean("iaf.probe.throneDiscovery")) {
            if (!discoveryStarted) {
                discoveryStarted = true;
                game.getIntegratedServer().execute(() -> ThroneDiscoveryRuntimeProbe.run(game.getIntegratedServer(),
                        game.getIntegratedServer().getPlayerList().getPlayerByUUID(game.player.getUniqueID())));
            }
            if (!ThroneDiscoveryRuntimeProbe.complete) return;
            if (ThroneDiscoveryRuntimeProbe.failure != null) throw new IllegalStateException("Real-player discovery failed", ThroneDiscoveryRuntimeProbe.failure);
            if (++syncTicks < 20) return;
            for (net.minecraft.util.ResourceLocation id : ThroneDiscoveryRuntimeProbe.expected)
                require(game.player.getRecipeBook().isUnlocked(game.world.getRecipeManager().getRecipe(id).get()),
                        "Matching throne unlock did not reach client: " + id);
            org.apache.logging.log4j.LogManager.getLogger().info("IAF THRONE DISCOVERY CLIENT SYNC PASSED: {} recipes", ThroneDiscoveryRuntimeProbe.expected.size());
        }
        if (Boolean.getBoolean("iaf.probe.bedDiscovery")) {
            if (!bedDiscoveryStarted) {
                bedDiscoveryStarted = true;
                game.getIntegratedServer().execute(() -> BedDiscoveryRuntimeProbe.run(game.getIntegratedServer(),
                        game.getIntegratedServer().getPlayerList().getPlayerByUUID(game.player.getUniqueID())));
            }
            if (!BedDiscoveryRuntimeProbe.complete) return;
            if (BedDiscoveryRuntimeProbe.failure != null) throw new IllegalStateException("Real-player bed discovery failed", BedDiscoveryRuntimeProbe.failure);
            if (++bedSyncTicks < 20) return;
            for (net.minecraft.util.ResourceLocation id : BedDiscoveryRuntimeProbe.expected)
                require(game.player.getRecipeBook().isUnlocked(game.world.getRecipeManager().getRecipe(id).get()),
                        "Matching bed unlock did not reach client: " + id);
            org.apache.logging.log4j.LogManager.getLogger().info("IAF BED DISCOVERY CLIENT SYNC PASSED: {} recipes", BedDiscoveryRuntimeProbe.expected.size());
        }
        if (Boolean.getBoolean("iaf.probe.chairPlacementClient")) {
            if (!placementStarted) {
                placementStarted = true;
                game.getIntegratedServer().execute(() -> {
                    try {
                        net.minecraft.entity.player.ServerPlayerEntity player = game.getIntegratedServer().getPlayerList()
                                .getPlayerByUUID(game.player.getUniqueID());
                        placementCases = ChairPlacementRuntimeProbe.run(
                                game.getIntegratedServer().getWorld(net.minecraft.world.dimension.DimensionType.OVERWORLD), player);
                    } catch (Throwable failure) { placementFailure = failure; }
                    finally { placementComplete = true; }
                });
            }
            if (!placementComplete) return;
            if (placementFailure != null) throw new IllegalStateException("Connected-player chair placement failed", placementFailure);
        }
        verify(game);
    }

    private static boolean inWorld() {
        return Boolean.getBoolean("iaf.probe.liveBaseMetalsClient") || Boolean.getBoolean("iaf.probe.inWorldClient");
    }

    private static void verify(Minecraft game) {
        complete = true;
        try {
            int creativeItems = CreativeTabsRuntimeProbe.run();
            java.util.Set<net.minecraft.item.Item> icons = new java.util.HashSet<>();
            for (net.minecraft.item.ItemGroup group : new net.minecraft.item.ItemGroup[] {
                    zone.moddev.mc.ironagefurniture.Ironagefurniture.IAF_CHAIRS_GROUP,
                    zone.moddev.mc.ironagefurniture.Ironagefurniture.IAF_BENCHES_GROUP,
                    zone.moddev.mc.ironagefurniture.Ironagefurniture.IAF_BEDS_GROUP,
                    zone.moddev.mc.ironagefurniture.Ironagefurniture.IAF_LIGHTS_GROUP})
            {
                require(!group.createIcon().isEmpty() && icons.add(group.createIcon().getItem()), "Empty or duplicate creative icon");
                require(game.getItemRenderer().getItemModelWithOverrides(group.createIcon(), null, null)
                        != game.getModelManager().getMissingModel(), "Missing creative icon model");
            }
            if (Boolean.getBoolean("iaf.probe.disabledCreative")) {
                require(creativeItems == 8, "Disabled families or integrations remain in creative inventory");
                require(zone.moddev.mc.ironagefurniture.Ironagefurniture.IAF_CHAIRS_GROUP.createIcon().getItem() == Items.OAK_STAIRS
                        && zone.moddev.mc.ironagefurniture.Ironagefurniture.IAF_BENCHES_GROUP.createIcon().getItem() == Items.OAK_SLAB
                        && zone.moddev.mc.ironagefurniture.Ironagefurniture.IAF_BEDS_GROUP.createIcon().getItem() == Items.RED_BED,
                        "Disabled furniture did not use safe vanilla icons");
            }
            if (Boolean.getBoolean("iaf.probe.tabsOnly") || Boolean.getBoolean("iaf.probe.missingThroneTarget")) {
                Files.write(Paths.get("phase-four-client-pass.properties"), ("status=PASS\ncreative_items=" + creativeItems + "\n").getBytes(StandardCharsets.UTF_8));
                game.shutdown();
                return;
            }
            if (inWorld()) {
                net.minecraft.client.util.ClientRecipeBook book = game.player.getRecipeBook();
                book.rebuildTable();
                java.util.Set<net.minecraft.util.ResourceLocation> visible = new java.util.HashSet<>();
                for (net.minecraft.client.gui.recipebook.RecipeList list : book.getRecipes())
                    for (net.minecraft.item.crafting.IRecipe<?> recipe : list.getRecipes()) visible.add(recipe.getId());
                int hidden = 0;
                int tallChairs = 0;
                int canopies = 0;
                for (net.minecraft.item.crafting.IRecipe<?> recipe : game.world.getRecipeManager().getRecipes()) {
                    if (!"ironagefurniture".equals(recipe.getId().getNamespace())) continue;
                    if (recipe.getId().getPath().contains("_recolour_")) {
                        require(recipe.isDynamic() && !visible.contains(recipe.getId()), "Client recipe book contains bed recolouring");
                        hidden++;
                    } else if (recipe.getId().getPath().startsWith("bed_")) {
                        require(!recipe.isDynamic() && visible.contains(recipe.getId()), "Client recipe book lost a construction recipe");
                        if (recipe.getId().getPath().startsWith("bed_canopy_foot_lower_")) {
                            require(recipe.getIngredients().size() == 3, "Client canopy recipe lacks its matching carpet");
                            net.minecraft.item.ItemStack bed = recipe.getIngredients().get(0).getMatchingStacks()[0];
                            net.minecraft.item.ItemStack carpet = recipe.getIngredients().get(2).getMatchingStacks()[0];
                            zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour colour =
                                    zone.moddev.mc.ironagefurniture.api.UpholsteryItemData.getColour(recipe.getRecipeOutput());
                            require(zone.moddev.mc.ironagefurniture.api.UpholsteryItemData.getColour(bed) == colour
                                    && carpet.getItem() == ForgeRegistries.ITEMS.getValue(new net.minecraft.util.ResourceLocation(
                                            "minecraft", colour.getName() + "_carpet")), "Client canopy recipe changes upholstery colour");
                            net.minecraft.item.crafting.RecipeItemHelper inventory = new net.minecraft.item.crafting.RecipeItemHelper();
                            inventory.accountStack(bed);
                            inventory.accountStack(recipe.getIngredients().get(1).getMatchingStacks()[0]);
                            require(!inventory.canCraft(recipe, null), "Client marks a canopy craftable without fabric");
                            inventory.accountStack(carpet);
                            require(inventory.canCraft(recipe, null), "Client rejects complete canopy ingredients");
                            canopies++;
                        }
                    } else if (recipe.getId().getPath().startsWith("chair_wood_ironage_wingback_")
                            || recipe.getId().getPath().startsWith("chair_wood_ironage_throne_")) {
                        require(recipe instanceof net.minecraft.item.crafting.ShapelessRecipe && !recipe.isDynamic()
                                && visible.contains(recipe.getId()), "Client lost a shapeless tall-chair recipe");
                        require(recipe.getIngredients().size() == 3, "Client tall-chair recipe lost an ingredient");
                        for (net.minecraft.item.crafting.Ingredient ingredient : recipe.getIngredients())
                            require(ingredient.getMatchingStacks().length == 1, "Client tall-chair recipe cycles through different chairs");
                        if (recipe.getId().getPath().startsWith("chair_wood_ironage_throne_")) {
                            net.minecraft.item.ItemStack chair = recipe.getIngredients().get(2).getMatchingStacks()[0];
                            require(zone.moddev.mc.ironagefurniture.api.UpholsteryItemData.getColour(chair)
                                    == zone.moddev.mc.ironagefurniture.api.UpholsteryItemData.getColour(recipe.getRecipeOutput()),
                                    "Client throne recipe uses a differently coloured wingback");
                        }
                        tallChairs++;
                    }
                }
                require(hidden >= 384, "Client did not receive all vanilla bed recolouring recipes");
                org.apache.logging.log4j.LogManager.getLogger().info("IAF RECOLOUR RECIPE-BOOK PROBE PASSED: {} hidden recipes", hidden);
                require(tallChairs >= 192, "Client did not receive the vanilla tall-chair recipes");
                org.apache.logging.log4j.LogManager.getLogger().info("IAF TALL-CHAIR RECIPE-BOOK CLIENT PROBE PASSED: {} recipes", tallChairs);
                require(canopies >= 96, "Client did not receive every vanilla canopy colour");
                org.apache.logging.log4j.LogManager.getLogger().info("IAF CANOPY CARPET CLIENT PROBE PASSED: {} recipes", canopies);
            }
            if (Boolean.getBoolean("iaf.probe.liveBaseMetalsClient")) {
                long available = java.util.Arrays.stream(zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal.values())
                        .filter(zone.moddev.mc.ironagefurniture.api.SconceMetalData::available).count();
                require(available == (Boolean.getBoolean("iaf.probe.baseMetalsDisabled") ? 2 : 23),
                        "Client did not receive published Base Metals tags/configuration: " + available);
            }
            int chairs = 0;
            for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                if (!(block instanceof ShieldChair)) continue;
                IBakedModel installed = game.getItemRenderer().getItemModelWithOverrides(new ItemStack(block), null, null);
                IBakedModel empty = game.getItemRenderer().getItemModelWithOverrides(
                        ShieldChairItemData.createChair(block, ItemStack.EMPTY), null, null);
                require(installed != game.getModelManager().getMissingModel(), "Missing chair item model");
                require(empty != game.getModelManager().getMissingModel(), "Missing empty-frame item model");
                int fullQuads = installed.getQuads(null, null, new Random(0)).size();
                int frameQuads = empty.getQuads(null, null, new Random(0)).size();
                require(frameQuads > 0 && frameQuads < fullQuads, "Empty-frame model still includes the shield");
                for (net.minecraft.block.BlockState state : block.getStateContainer().getValidStates()) {
                    IBakedModel placed = game.getBlockRendererDispatcher().getModelForState(state);
                    require(placed != game.getModelManager().getMissingModel(), "Missing shield-chair blockstate");
                }
                chairs++;
            }
            require(chairs >= 6, "Vanilla chairs not loaded");
            int upholstery = 0;
            for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                if (block instanceof zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed
                        || block instanceof zone.moddev.mc.ironagefurniture.api.blocks.furniture.MultiBlockChair)
                    for (net.minecraft.block.BlockState state : block.getStateContainer().getValidStates()) {
                        require(game.getBlockRendererDispatcher().getModelForState(state) != game.getModelManager().getMissingModel(),
                                "Missing upholstered blockstate: " + state);
                        if (block instanceof zone.moddev.mc.ironagefurniture.api.blocks.furniture.MultiBlockChair)
                            verifyChairBackModel(game, state);
                    }
                if (!(block.asItem() instanceof zone.moddev.mc.ironagefurniture.api.items.UpholsteredBlockItem)) continue;
                for (zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour colour
                        : zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour.values()) {
                    IBakedModel model = game.getItemRenderer().getItemModelWithOverrides(
                            zone.moddev.mc.ironagefurniture.api.UpholsteryItemData.create(block, colour), null, null);
                    require(model != game.getModelManager().getMissingModel(), "Missing upholstered inventory model");
                    java.util.List<net.minecraft.client.renderer.model.BakedQuad> quads = model.getQuads(null, null, new Random(0));
                    require(quads.stream().anyMatch(quad -> quad.getSprite().getName().equals(
                            new net.minecraft.util.ResourceLocation("minecraft:block/" + colour.getName() + "_wool"))),
                            "Inventory colour does not select its wool texture: " + block.getRegistryName() + "/" + colour);
                }
                upholstery++;
            }
            require(upholstery >= 36, "Upholstered furniture not loaded");
            if (Boolean.getBoolean("iaf.probe.chairPlacementClient"))
                org.apache.logging.log4j.LogManager.getLogger().info("IAF CONNECTED-PLAYER CHAIR PLACEMENT PASSED: {} cases; baked backs match all state rotations", placementCases);
            for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                if (!(block instanceof zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.Candle)
                        && !(block instanceof zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.AdditionalSconce)
                        && !(block instanceof zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.RockSaltSconce)) continue;
                for (net.minecraft.block.BlockState state : block.getStateContainer().getValidStates()) {
                    IBakedModel placed = game.getBlockRendererDispatcher().getModelForState(state);
                    require(placed != game.getModelManager().getMissingModel(), "Missing candle/twin-torch block model");
                    require(placed.getQuads(state, null, new Random(0)).stream().noneMatch(quad -> quad.getSprite().getName().getPath().contains("missing")),
                            "Sconce block has an unresolved texture: " + block.getRegistryName());
                }
                IBakedModel item = game.getItemRenderer().getItemModelWithOverrides(new ItemStack(block), null, null);
                require(item != game.getModelManager().getMissingModel(), "Missing candle item model");
                require(item.getQuads(null, null, new Random(0)).stream().noneMatch(quad -> quad.getSprite().getName().getPath().contains("missing")),
                        "Candle/sconce item has an unresolved texture: " + block.getRegistryName());
            }
            require(TileEntityRendererDispatcher.instance.getRenderer(new ShieldChairTileEntity()) != null,
                    "Shield tile renderer not registered");
            int metalModels = 0;
            for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                if (!(block instanceof zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce)) continue;
                for (zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal metal
                        : zone.moddev.mc.ironagefurniture.api.enumerations.SconceMetal.values()) {
                    net.minecraft.util.ResourceLocation expected = game.getBlockRendererDispatcher().getModelForState(
                            zone.moddev.mc.ironagefurniture.api.SconceMetalData.textureBlock(metal).getDefaultState())
                            .getParticleTexture().getName();
                    if (Boolean.getBoolean("iaf.probe.liveBaseMetalsClient") && metal.isBaseMetal()
                            && !Boolean.getBoolean("iaf.probe.baseMetalsDisabled"))
                        {
                            java.util.Collection<Block> suppliers = net.minecraft.tags.BlockTags.getCollection()
                                    .getOrCreate(zone.moddev.mc.ironagefurniture.api.SconceMetalData.storage(metal)).getAllElements();
                            Block selected = zone.moddev.mc.ironagefurniture.api.SconceMetalData.textureBlock(metal);
                            // IE may supply the same metal tag as Base Metals.
                            // Either real storage block is valid, but iron or a
                            // missing sprite must never stand in for it.
                            require(suppliers.contains(selected) && selected != net.minecraft.block.Blocks.IRON_BLOCK
                                    && !expected.getPath().contains("missing"),
                                    "Published metal resolved to a fallback texture: " + metal + "/" + expected);
                            if (suppliers.size() == 1)
                                require("basemetals".equals(expected.getNamespace()) && selected.getRegistryName()
                                        .equals(new net.minecraft.util.ResourceLocation("basemetals", metal.getName() + "_block")),
                                        "Published Base Metals texture changed: " + metal + "/" + expected);
                        }
                    if (block.asItem() != Items.AIR) {
                        ItemStack stack = zone.moddev.mc.ironagefurniture.api.SconceMetalData.create(block, metal);
                        IBakedModel item = game.getItemRenderer().getItemModelWithOverrides(stack, null, null);
                        require(item.getQuads(null, null, new Random(0)).stream().anyMatch(quad -> expected.equals(quad.getSprite().getName())),
                                "Inventory frame has wrong metal texture: " + block.getRegistryName() + "/" + metal);
                        verifyMetalUvs(item.getQuads(null, null, new Random(0)), expected,
                                "Inventory " + block.getRegistryName() + "/" + metal);
                        for (net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType transform
                                : net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType.values()) {
                            IBakedModel rendered = item.handlePerspective(transform).getLeft();
                            java.util.List<net.minecraft.client.renderer.model.BakedQuad> renderedQuads =
                                    rendered.getQuads(null, null, new Random(0));
                            require(renderedQuads.stream().anyMatch(quad -> expected.equals(quad.getSprite().getName())),
                                    "Camera transform discarded the metal model: " + block.getRegistryName() + "/" + metal + "/" + transform);
                            verifyMetalUvs(renderedQuads, expected, "Rendered " + metal + "/" + transform);
                        }
                    }
                    for (net.minecraft.block.BlockState state : block.getStateContainer().getValidStates()) {
                        if (state.get(zone.moddev.mc.ironagefurniture.api.SconceMetalData.METAL) != metal) continue;
                        IBakedModel model = game.getBlockRendererDispatcher().getModelForState(state);
                        java.util.List<net.minecraft.client.renderer.model.BakedQuad> quads = model.getQuads(state, null, new Random(0));
                        require(quads.stream().anyMatch(quad -> expected.equals(quad.getSprite().getName())),
                                "Placed frame has wrong metal texture: " + state);
                        require(quads.stream().noneMatch(quad -> quad.getSprite().getName().getPath().contains("missing")),
                                "Missing metal model texture: " + state);
                        verifyMetalUvs(quads, expected, "Placed " + state);
                        metalModels++;
                    }
                }
            }
            ItemStack patterned = new ItemStack(Items.SHIELD);
            net.minecraft.nbt.CompoundNBT pattern = new net.minecraft.nbt.CompoundNBT();
            pattern.putInt("Base", 14);
            patterned.getOrCreateTag().put("BlockEntityTag", pattern);
            ShieldChair chair = (ShieldChair) ForgeRegistries.BLOCKS.getValue(
                    new net.minecraft.util.ResourceLocation("ironagefurniture:chair_wood_ironage_shield_oak"));
            // Exercise vanilla's banner-texture cache and the plate renderer on
            // the render thread, for both plain and patterned shields.
            zone.moddev.mc.ironagefurniture.client.renderer.ShieldChairRenderer renderer =
                    new zone.moddev.mc.ironagefurniture.client.renderer.ShieldChairRenderer();
            renderer.renderShield(new ItemStack(Items.SHIELD), chair.getDefaultState(), -100, -100, -100);
            renderer.renderShield(patterned, chair.getDefaultState(), -100, -100, -100);
            zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp lamp =
                    new zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp(
                            zone.moddev.mc.ironagefurniture.registers.entities.RELEASED_LAVA_LAMP.get(), null);
            net.minecraft.client.renderer.entity.EntityRenderer<?> lampRenderer = game.getRenderManager().getRenderer(lamp);
            require(lampRenderer instanceof zone.moddev.mc.ironagefurniture.client.renderer.ReleasedLavaLampRenderer,
                    "Released lava lamp has no dedicated glass renderer");
            ((zone.moddev.mc.ironagefurniture.client.renderer.ReleasedLavaLampRenderer)lampRenderer)
                    .doRender(lamp, -100, -100, -100, 0, 0);
            int torchParticles = Boolean.getBoolean("iaf.probe.sconceParticles") ? SconceFlameClientProbe.run(game) : 0;
            Files.write(Paths.get("phase-four-client-pass.properties"),
                    ("status=PASS\nshield_chairs=" + chairs + "\nupholstered_forms=" + upholstery
                            + "\nmetal_models=" + metalModels + "\nchair_placements=" + placementCases
                            + "\ntwin_torch_particle_cases=" + torchParticles + "\n").getBytes(StandardCharsets.UTF_8));
            org.apache.logging.log4j.LogManager.getLogger().info("IAF PHASE FOUR CLIENT PROBE PASSED: {} chairs", chairs);
        } catch (Exception failure) {
            throw new IllegalStateException("Phase 4 client probe failed", failure);
        } finally { game.shutdown(); }
    }

    private static void verifyChairBackModel(Minecraft game, net.minecraft.block.BlockState state) {
        net.minecraft.util.Direction heading = state.get(zone.moddev.mc.ironagefurniture.api.blocks.furniture.Chair.DIRECTION);
        IBakedModel model = game.getBlockRendererDispatcher().getModelForState(state);
        java.util.List<net.minecraft.client.renderer.model.BakedQuad> quads = new java.util.ArrayList<>(model.getQuads(state, null, new Random(0)));
        quads.addAll(model.getQuads(state, heading.getOpposite(), new Random(0)));
        boolean backFound = false;
        for (net.minecraft.client.renderer.model.BakedQuad quad : quads) {
            if (quad.getFace() != heading.getOpposite()) continue;
            int[] vertices = quad.getVertexData();
            int stride = quad.getFormat().getIntegerSize();
            double minAlong = 2, minAcross = 2, maxAcross = -2, minY = 2, maxY = -2;
            for (int vertex = 0; vertex < 4; vertex++) {
                double x = Float.intBitsToFloat(vertices[vertex * stride]) - .5;
                double y = Float.intBitsToFloat(vertices[vertex * stride + 1]);
                double z = Float.intBitsToFloat(vertices[vertex * stride + 2]) - .5;
                minAlong = Math.min(minAlong, x * heading.getXOffset() + z * heading.getZOffset());
                double across = x * heading.getZOffset() - z * heading.getXOffset();
                minAcross = Math.min(minAcross, across);
                maxAcross = Math.max(maxAcross, across);
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
            }
            // A broad upright back panel, not an arm or the throne's top canopy.
            if (minAlong > .35 && maxAcross - minAcross >= .5 && maxY - minY > .4) backFound = true;
        }
        require(backFound, "Rendered chair back is not behind its seat: " + state);
    }

    private static void verifyMetalUvs(java.util.List<net.minecraft.client.renderer.model.BakedQuad> quads,
            net.minecraft.util.ResourceLocation expected, String description) {
        for (net.minecraft.client.renderer.model.BakedQuad quad : quads) {
            if (!expected.equals(quad.getSprite().getName())) continue;
            net.minecraft.client.renderer.texture.TextureAtlasSprite sprite = quad.getSprite();
            int stride = quad.getFormat().getIntegerSize();
            int uv = quad.getFormat().getUvOffsetById(0) / 4;
            int[] vertices = quad.getVertexData();
            for (int vertex = 0; vertex < 4; vertex++) {
                float u = Float.intBitsToFloat(vertices[vertex * stride + uv]);
                float v = Float.intBitsToFloat(vertices[vertex * stride + uv + 1]);
                require(u >= sprite.getMinU() - 0.000001F && u <= sprite.getMaxU() + 0.000001F
                                && v >= sprite.getMinV() - 0.000001F && v <= sprite.getMaxV() + 0.000001F,
                        description + " names " + expected + " but samples another atlas texture: " + u + "," + v
                                + " outside " + sprite.getMinU() + ".." + sprite.getMaxU()
                                + "," + sprite.getMinV() + ".." + sprite.getMaxV());
            }
        }
    }

    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }
}
