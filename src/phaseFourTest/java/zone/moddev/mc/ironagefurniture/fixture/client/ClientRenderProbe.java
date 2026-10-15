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
        client.player.setYRot(180);
        client.player.setXRot(24);
        if (++readyTicks < 160) return;
        boolean network = Boolean.getBoolean("iaf.probe.multiplayer");
        if (network && (!(client.level.getBlockEntity(ClientScene.position(0, false)) instanceof CandleSconceBlockEntity first)
                || first.candles().size() != 2 || !ItemStack.matches(first.candles().getFirst(), ClientScene.candle(0, 0, true)))) return;
        try {
            var coreFile = net.neoforged.fml.ModList.get().getModContainerById("ironagefurniture").orElseThrow()
                    .getModInfo().getOwningFile().getFile().getFilePath();
            require(Files.isSameFile(coreFile, client.gameDirectory.toPath().resolve("mods/iron-age-furniture-0.4.0.2603002.jar")),
                    "The client is running the packaged core jar, not project classes");
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
                require(!((net.minecraft.client.renderer.item.ItemStackRenderState)field(state, "shield")).isEmpty(), "The stored shield extracts a native three-dimensional model");
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
    private static void require(boolean okay, String message) { if (!okay) throw new AssertionError(message); checks++; }
    private static void fail(Minecraft client, Throwable failure) {
        finished = true; failure.printStackTrace(); System.err.println("IAF_CLIENT_RENDER_FAIL checks=" + checks); client.stop();
    }
    private ClientRenderProbe() { }
}
