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
                game.launchIntegratedServer("world", "Base Metals sconce test", null);
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
        verify(game);
    }

    private static boolean inWorld() {
        return Boolean.getBoolean("iaf.probe.liveBaseMetalsClient") || Boolean.getBoolean("iaf.probe.inWorldClient");
    }

    private static void verify(Minecraft game) {
        complete = true;
        try {
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
                    for (net.minecraft.block.BlockState state : block.getStateContainer().getValidStates())
                        require(game.getBlockRendererDispatcher().getModelForState(state) != game.getModelManager().getMissingModel(),
                                "Missing upholstered blockstate: " + state);
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
            Files.write(Paths.get("phase-four-client-pass.properties"),
                    ("status=PASS\nshield_chairs=" + chairs + "\nupholstered_forms=" + upholstery
                            + "\nmetal_models=" + metalModels + "\n").getBytes(StandardCharsets.UTF_8));
            org.apache.logging.log4j.LogManager.getLogger().info("IAF PHASE FOUR CLIENT PROBE PASSED: {} chairs", chairs);
        } catch (Exception failure) {
            throw new IllegalStateException("Phase 4 client probe failed", failure);
        } finally { game.shutdown(); }
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
