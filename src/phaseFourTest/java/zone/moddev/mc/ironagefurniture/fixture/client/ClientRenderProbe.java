package zone.moddev.mc.ironagefurniture.fixture.client;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;
import zone.moddev.mc.ironagefurniture.client.renderer.CandleSconceRenderer;
import zone.moddev.mc.ironagefurniture.fixture.ClientScene;

/** Runs only in the separate packaged client profile, never in a normal game. */
@EventBusSubscriber(modid = "iafphasefourprobe", value = Dist.CLIENT)
public final class ClientRenderProbe {
    private static int ticks, readyTicks, checks;
    private static boolean finished;
    private static boolean hidden;
    private static int shieldClickStage, shieldClickTicks;
    private static int candleGalleryTicks;
    private static Vec3 shieldPlayerStart;
    private static final java.util.Set<Integer> releasedLamps = new java.util.HashSet<>();
    @SubscribeEvent public static void tick(ClientTickEvent.Post event) {
        if (!Boolean.getBoolean("iaf.probe.clientRendering") || finished) return;
        Minecraft client = Minecraft.getInstance();
        if (!hidden) { org.lwjgl.sdl.SDLVideo.SDL_HideWindow(client.getWindow().handle()); hidden = true; }
        client.options.pauseOnLostFocus = false;
        if (ticks % 200 == 0)
            System.out.println("IAF_CLIENT_PROGRESS screen=" + (client.gui.screen() == null ? "none" : client.gui.screen().getClass().getSimpleName())
                    + " world=" + (client.level != null));
        if (++ticks > 3600) { fail(client, new AssertionError("Client gallery did not load in time")); return; }
        if (client.level == null || client.player == null) return;
        for (var entity : client.level.entitiesForRendering())
            if (entity instanceof zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp) releasedLamps.add(entity.getId());
        client.player.setYRot(180);
        client.player.setXRot(24);
        if (Boolean.getBoolean("iaf.probe.shieldGallery")) {
            client.player.setPos(28.5, 102.5, 16);
            client.player.setXRot(15);
        }
        if (++readyTicks < 160) return;
        boolean network = Boolean.getBoolean("iaf.probe.multiplayer");
        if (network && (!(client.level.getBlockEntity(ClientScene.position(0, false)) instanceof CandleSconceBlockEntity first)
                || first.candles().size() != 2 || !ItemStack.matches(first.candles().getFirst(), ClientScene.candle(0, 0, true)))) return;
        try {
            var coreFile = net.neoforged.fml.ModList.get().getModContainerById("ironagefurniture").orElseThrow()
                    .getModInfo().getOwningFile().getFile().getFilePath();
            require(Files.isSameFile(coreFile, client.gameDirectory.toPath().resolve("mods/iron-age-furniture-0.4.0.2603002.jar")),
                    "The client is running the packaged core jar, not project classes");
            if (!verifyShieldClickPath(client)) return;
            if (Boolean.getBoolean("iaf.probe.candleGallery")) {
                client.player.setPos(4.5, 100.5, 14);
                client.player.setXRot(15);
                if (++candleGalleryTicks < 8) return;
            }
            verifyItemRegressions(client);
            CandleSconceRenderChecks.matrix(client, ClientRenderProbe::require);
            var released = new zone.moddev.mc.ironagefurniture.api.entity.ReleasedLavaLamp(client.level, 0, 110, 0, 110, false);
            Vec3 start = released.position();
            for (int tick = 0; tick < 10; tick++) { released.commonTick(); released.tick(); }
            require(released.position().equals(start) && released.getDeltaMovement().equals(Vec3.ZERO),
                    "A client-side released lamp never simulates authoritative physics");
            released.moveOrInterpolateTo(start.add(0, -3, 0), 0, 0);
            require(released.getInterpolation().hasActiveInterpolation() && released.position().equals(start),
                    "A released lamp smoothly interpolates server updates instead of snapping");
            for (int tick = 0; tick < 3; tick++) { released.commonTick(); released.tick(); }
            require(released.position().distanceTo(start.add(0, -3, 0)) < .00001, "Client interpolation reaches the server position");
            if (!network) require(releasedLamps.size() >= 2, "Real floor and wall support-loss lamps spawn and tick safely on the client");
            System.out.println("IAF_CLIENT_LAVA_PASS tracked=" + releasedLamps.size());
            int models = 0;
            for (boolean wall : new boolean[]{false, true}) for (int index = 0; index < ClientScene.TYPES.size(); index++) {
                var pos = ClientScene.position(index, wall);
                require(client.level.getBlockEntity(pos) instanceof CandleSconceBlockEntity, "Client receives the candle block entity");
                var storage = (CandleSconceBlockEntity)client.level.getBlockEntity(pos);
                List<ItemStack> contents = storage.candles();
                require(contents.size() == (index + (network ? 1 : 0)) % 4 + 1, "Client receives the exact slot count");
                for (int slot = 0; slot < contents.size(); slot++)
                    require(ItemStack.isSameItemSameComponents(contents.get(slot), ClientScene.candle(index, slot, network)), "Client receives the original candle components");
                var renderer = (CandleSconceRenderer)(Object)client.getBlockEntityRenderDispatcher().getRenderer(storage);
                require(renderer != null, "The native candle renderer is registered");
                var state = renderer.createRenderState();
                renderer.extractRenderState(storage, state, 0, Vec3.ZERO, null);
                List<?> parts = (List<?>)field(state, "candles");
                require(parts.size() == contents.size(), "Extraction contains one three-dimensional model per candle");
                for (int slot = 0; slot < parts.size(); slot++) {
                    BlockModelRenderState model = (BlockModelRenderState)field(parts.get(slot), "model");
                    require(!model.isEmpty(), "The candle model is not empty");
                    @SuppressWarnings("unchecked") List<BlockStateModelPart> geometry = (List<BlockStateModelPart>)field(model, "modelParts");
                    String expected = "block/" + ClientScene.TYPES.get((index + slot + (network ? 3 : 0)) % ClientScene.TYPES.size());
                    require(!geometry.isEmpty() && geometry.stream().allMatch(part -> {
                        String texture = part.particleMaterial().sprite().contents().name().getPath();
                        return texture.equals(expected) || texture.equals(expected + "_lit");
                    }), "The baked candle material matches its actual vanilla colour: " + expected);
                    models++;
                }
            }
            for (int index = 0; index < ClientScene.shieldChairs().size(); index++) {
                var storage = client.level.getBlockEntity(ClientScene.shieldPosition(index));
                require(storage instanceof zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity, "Client receives each core/add-on shield chair");
                var shield = (zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity)storage;
                require(ItemStack.matches(shield.shield(), ClientScene.shield(index, network)), "Shield components synchronize to the client");
                var renderer = (zone.moddev.mc.ironagefurniture.client.renderer.ShieldChairRenderer)(Object)client.getBlockEntityRenderDispatcher().getRenderer(shield);
                require(renderer != null, "Core renderer is registered for every core/add-on shield chair");
                var state = renderer.createRenderState();
                renderer.extractRenderState(shield, state, 0, Vec3.ZERO, null);
                ShieldChairRenderChecks.verify(renderer, state, shield.shield(),
                        shield.getBlockState().getValue(zone.moddev.mc.ironagefurniture.api.blocks.base.FurnitureBlock.DIRECTION), ClientRenderProbe::require);
                if (index == 0) ShieldChairRenderChecks.matrix(client, renderer, shield.getBlockState(), ClientRenderProbe::require);
            }
            int itemModels = 0;
            for (var item : net.minecraft.core.registries.BuiltInRegistries.ITEM) {
                var id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item);
                if (!id.getNamespace().equals("ironagefurniture") || !zone.moddev.mc.ironagefurniture.init.ModCreativeTab.visible(item)) continue;
                for (var context : List.of(net.minecraft.world.item.ItemDisplayContext.GUI,
                        net.minecraft.world.item.ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, net.minecraft.world.item.ItemDisplayContext.GROUND)) {
                    var state = new net.minecraft.client.renderer.item.ItemStackRenderState();
                    client.getItemModelResolver().updateForTopItem(state, new ItemStack(item), context, client.level, client.player, 0);
                    require(!state.isEmpty(), "A visible furniture item has a native model: " + id + " " + context);
                    var material = state.pickParticleMaterial(net.minecraft.util.RandomSource.create(0));
                    require(material == null || !material.sprite().contents().name().getPath().contains("missingno"), "The item material is not missing: " + id);
                    if (item instanceof zone.moddev.mc.ironagefurniture.api.items.MetalSconceBlockItem frame && !frame.getMetal().isBaseMetal())
                        require(material != null && material.sprite().contents().name().getPath().equals("block/" + frame.getMetal().getName() + "_block"),
                                "The baked iron/gold/copper frame uses its own metal, not the iron fallback: " + id);
                    itemModels++;
                }
            }
            require(itemModels > 1000, "The client audited the complete visible furniture catalog");
            System.out.println("IAF_CLIENT_ITEM_MODELS_PASS models=" + itemModels + " shieldChairs=" + ClientScene.shieldChairs().size());
            int sconces = 0;
            var display = net.minecraft.client.renderer.block.model.BlockDisplayContext.create();
            for (var block : net.minecraft.core.registries.BuiltInRegistries.BLOCK.stream()
                    .filter(value -> value instanceof zone.moddev.mc.ironagefurniture.api.blocks.base.LightHolderSconce).toList()) {
                var material = zone.moddev.mc.ironagefurniture.api.SconceMetalData.get(block.defaultBlockState());
                for (var blockState : block.getStateDefinition().getPossibleStates()) {
                    BlockModelRenderState model = new BlockModelRenderState();
                    client.getBlockModelResolver().update(model, blockState, display);
                    @SuppressWarnings("unchecked") List<BlockStateModelPart> parts = (List<BlockStateModelPart>)field(model, "modelParts");
                    require(parts.stream().noneMatch(part -> part.particleMaterial().sprite().contents().name().getPath().contains("missingno")),
                            "No flattened sconce state has a missing model or texture: " + blockState);
                    if (!material.isBaseMetal()) require(!model.isEmpty() && parts.stream().allMatch(part ->
                            part.particleMaterial().sprite().contents().name().getPath().equals("block/" + material.getName() + "_block")),
                            "Every native-metal placed sconce uses its own material: " + blockState);
                    sconces++;
                }
            }
            require(sconces == 12880, "All registered fixed-metal sconce states were baked and audited");
            System.out.println("IAF_CLIENT_SCONCE_MODELS_PASS states=" + sconces);
            finished = true;
            int rendered = models;
            String screenshot = "phase-four-candles-" + System.currentTimeMillis() + ".png";
            Screenshot.grab(client.gameDirectory, screenshot, client.gameRenderer.mainRenderTarget(), 1, message -> {
                try {
                    require(Files.size(client.gameDirectory.toPath().resolve("screenshots/" + screenshot)) > 1000, "The rendered gallery screenshot was saved");
                    System.out.println("IAF_CLIENT_RENDER_PASS checks=" + checks + " models=" + rendered + " screenshot=" + screenshot);
                } catch (Throwable failure) { failure.printStackTrace(); System.err.println("IAF_CLIENT_RENDER_FAIL"); }
                finally { client.execute(client::stop); }
            });
        } catch (Throwable failure) { fail(client, failure); }
    }
    private static Object field(Object value, String name) throws ReflectiveOperationException {
        Field field = value.getClass().getDeclaredField(name); field.setAccessible(true); return field.get(value);
    }
    private static void verifyItemRegressions(Minecraft client) throws ReflectiveOperationException {
        if (!Boolean.getBoolean("iaf.probe.skipShieldItem")) for (var block : ClientScene.shieldChairs())
                for (var context : List.of(net.minecraft.world.item.ItemDisplayContext.GUI,
                        net.minecraft.world.item.ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, net.minecraft.world.item.ItemDisplayContext.GROUND)) {
            var full = new net.minecraft.client.renderer.item.ItemStackRenderState();
            var empty = new net.minecraft.client.renderer.item.ItemStackRenderState();
            client.getItemModelResolver().updateForTopItem(full, new ItemStack(block), context, client.level, client.player, 0);
            client.getItemModelResolver().updateForTopItem(empty,
                    zone.moddev.mc.ironagefurniture.api.ShieldChairItemData.createChair(block, ItemStack.EMPTY), context, client.level, client.player, 0);
            require(quadCount(empty) == 72, "An empty shield-chair item renders just the twelve wooden frame cuboids: " + block + " " + context);
            require(quadCount(full) > quadCount(empty), "Legacy/default shield-chair items retain their mounted shield");
        }
        var chunk = new net.minecraft.client.renderer.item.ItemStackRenderState();
        var item = net.minecraft.core.registries.BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.fromNamespaceAndPath("ironagefurniture", "obsidian_chunk"));
        client.getItemModelResolver().updateForTopItem(chunk, new ItemStack(item), net.minecraft.world.item.ItemDisplayContext.GUI, client.level, client.player, 0);
        Object layer = ((Object[])field(chunk, "layers"))[0];
        var transform = (net.minecraft.client.resources.model.cuboid.ItemTransform)field(layer, "itemTransform");
        require(transform.rotation().x() >= 30 && transform.rotation().y() >= 30, "The obsidian chunk has an angled three-dimensional inventory transform");
        require(quadCount(chunk) == 6, "The obsidian chunk remains real cuboid geometry, not a flat generated sprite");
        var centre = chunk.getModelBoundingBox().getCenter();
        require(Math.abs(centre.x) < .02 && Math.abs(centre.y) < .02 && Math.abs(centre.z) < .02,
                "The angled obsidian chunk is centred inside its inventory slot");
        System.out.println("IAF_ITEM_REGRESSIONS_PASS");
    }
    private static boolean verifyShieldClickPath(Minecraft client) {
        var server = client.getSingleplayerServer();
        if (server == null || shieldClickStage == 5) return true;
        var pos = ClientScene.shieldPosition(0);
        var original = ClientScene.shield(0);
        if (shieldClickStage == 0) {
            shieldPlayerStart = client.player.position();
            client.player.setPos(pos.getX() + .5, pos.getY(), pos.getZ() + 2.5);
            client.player.getInventory().clearContent();
            client.player.setItemInHand(net.minecraft.world.InteractionHand.OFF_HAND, new ItemStack(net.minecraft.world.item.Items.TORCH));
            client.options.keyShift.setDown(true);
            server.execute(() -> {
                var player = server.getPlayerList().getPlayer(client.player.getUUID());
                player.stopRiding(); player.setPos(pos.getX() + .5, pos.getY(), pos.getZ() + 2.5);
                player.getInventory().clearContent();
                player.setItemInHand(net.minecraft.world.InteractionHand.OFF_HAND, new ItemStack(net.minecraft.world.item.Items.TORCH));
            });
            shieldClickStage = 1; shieldClickTicks = 0; return false;
        }
        if (++shieldClickTicks < 20) return false;
        var storage = (zone.moddev.mc.ironagefurniture.api.tile.ShieldChairBlockEntity)client.level.getBlockEntity(pos);
        var hit = new net.minecraft.world.phys.BlockHitResult(Vec3.atCenterOf(pos), net.minecraft.core.Direction.SOUTH, pos, false);
        if (shieldClickStage == 1) {
            require(client.player.isShiftKeyDown(), "The real client input path has activated sneaking");
            require(client.player.getMainHandItem().isEmpty() && client.player.getOffhandItem().is(net.minecraft.world.item.Items.TORCH), "The removal test has an empty main hand and an occupied offhand");
            require(client.gameMode.useItemOn(client.player, net.minecraft.world.InteractionHand.MAIN_HAND, hit).consumesAction(), "The client accepts sneak-click shield removal");
            shieldClickStage = 2; shieldClickTicks = 0; return false;
        }
        if (shieldClickStage == 2) {
            if ((!storage.shield().isEmpty() || !ItemStack.matches(client.player.getMainHandItem(), original)) && shieldClickTicks < 100) return false;
            require(storage.shield().isEmpty() && ItemStack.matches(client.player.getMainHandItem(), original), "A real use-item packet removes and synchronizes the exact original shield");
            require(!client.player.isPassenger(), "Sneak removal never mounts the real client player");
            client.options.keyShift.setDown(false);
            if (Boolean.getBoolean("iaf.probe.itemGallery")) {
                Screenshot.grab(client.gameDirectory, "empty-shield-frame.png", client.gameRenderer.mainRenderTarget(), 1, message -> { });
                var chair = client.level.getBlockState(pos).getBlock();
                client.player.getInventory().setItem(2, zone.moddev.mc.ironagefurniture.api.ShieldChairItemData.createChair(chair, ItemStack.EMPTY));
                client.player.getInventory().setItem(3, new ItemStack(chair));
                client.player.getInventory().setItem(4, new ItemStack(net.minecraft.core.registries.BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.fromNamespaceAndPath("ironagefurniture", "obsidian_chunk"))));
                client.player.getInventory().setItem(5, new ItemStack(net.minecraft.core.registries.BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.fromNamespaceAndPath("ironagefurniture", "light_metal_ironage_block_floor_lava_clear"))));
                client.gui.setScreen(new net.minecraft.client.gui.screens.inventory.InventoryScreen(client.player));
            }
            shieldClickStage = 3; shieldClickTicks = 0; return false;
        }
        if (shieldClickStage == 3) {
            if (Boolean.getBoolean("iaf.probe.itemGallery")) {
                Screenshot.grab(client.gameDirectory, "shield-and-obsidian-inventory.png", client.gameRenderer.mainRenderTarget(), 1, message -> { });
                client.gui.setScreen(null);
            }
            require(!client.player.isShiftKeyDown(), "The real client has released the sneak key before attaching");
            require(client.gameMode.useItemOn(client.player, net.minecraft.world.InteractionHand.MAIN_HAND, hit).consumesAction(), "The client accepts shield reattachment");
            shieldClickStage = 4; shieldClickTicks = 0; return false;
        }
        if (!ItemStack.matches(storage.shield(), original) && shieldClickTicks < 100) return false;
        require(ItemStack.matches(storage.shield(), original), "Reattachment reaches the server and synchronizes the mounted shield back to the client");
        client.player.setPos(shieldPlayerStart.x, shieldPlayerStart.y, shieldPlayerStart.z);
        server.execute(() -> {
            var player = server.getPlayerList().getPlayer(client.player.getUUID());
            player.setPos(shieldPlayerStart.x, shieldPlayerStart.y, shieldPlayerStart.z);
        });
        shieldClickStage = 5;
        System.out.println("IAF_CLIENT_SHIELD_CLICK_PASS");
        return true;
    }
    private static int quadCount(net.minecraft.client.renderer.item.ItemStackRenderState state) throws ReflectiveOperationException {
        int count = 0, active = (int)field(state, "activeLayerCount");
        Object[] layers = (Object[])field(state, "layers");
        for (int i = 0; i < active; i++) count += ((net.minecraft.client.resources.model.geometry.ItemQuads)field(layers[i], "quads")).all().size();
        return count;
    }
    private static void require(boolean okay, String message) { if (!okay) throw new AssertionError(message); checks++; }
    private static void fail(Minecraft client, Throwable failure) {
        finished = true; failure.printStackTrace(); System.err.println("IAF_CLIENT_RENDER_FAIL checks=" + checks); client.stop();
    }
    private ClientRenderProbe() { }
}
