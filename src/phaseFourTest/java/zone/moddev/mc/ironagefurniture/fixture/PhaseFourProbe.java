package zone.moddev.mc.ironagefurniture.fixture;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.AABB;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import zone.moddev.mc.ironagefurniture.api.CandleInventory;
import zone.moddev.mc.ironagefurniture.api.UpholsteryItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.FurnitureBed;
import zone.moddev.mc.ironagefurniture.api.blocks.lightsource.phasefour.CandleSconceBlock;
import zone.moddev.mc.ironagefurniture.api.enumerations.UpholsteryColour;
import zone.moddev.mc.ironagefurniture.api.tile.CandleSconceBlockEntity;
import zone.moddev.mc.ironagefurniture.init.PhaseFourFurniture;
import zone.moddev.mc.ironagefurniture.init.PhaseFourLighting;

/** Executed inside a real dedicated server, with its registries and codecs loaded. */
@Mod("iafphasefourprobe")
public final class PhaseFourProbe {
    private int checks;
    private SavedWorldProbe worldProbe;
    private boolean normalProbeQueued;
    private int worldTicks;
    public PhaseFourProbe() {
        NeoForge.EVENT_BUS.addListener(this::started);
        NeoForge.EVENT_BUS.addListener(this::tick);
        NeoForge.EVENT_BUS.addListener(this::entityJoined);
        NeoForge.EVENT_BUS.addListener(ClientScene::joined);
    }
    private void entityJoined(net.neoforged.neoforge.event.entity.EntityJoinLevelEvent event) {
        if (Boolean.getBoolean("iaf.probe.savedWorld") && event.getEntity() instanceof ItemEntity item) item.setNeverPickUp();
    }
    private void started(ServerStartedEvent event) {
        event.getServer().execute(() -> {
            if (Boolean.getBoolean("iaf.probe.clientRendering")) { ClientScene.create(event.getServer()); return; }
            if (!Boolean.getBoolean("iaf.probe.savedWorld")) {
                event.getServer().getCommands().performPrefixedCommand(event.getServer().createCommandSourceStack(), "time set midnight");
                // Cross-chunk beds need the surrounding entity sections loaded before counting drops.
                for (int x = -2; x <= 2; x++) for (int z = -2; z <= 2; z++) {
                    event.getServer().overworld().setChunkForced(x, z, true);
                    event.getServer().overworld().getChunk(x, z);
                }
                normalProbeQueued = true;
                return;
            }
            try { worldProbe = new SavedWorldProbe(); worldProbe.loadChunks(event.getServer()); }
            catch (Throwable failure) { failure.printStackTrace(); System.err.println("IAF_WORLD_PROBE_FAIL"); event.getServer().halt(false); }
        });
    }
    private void tick(net.neoforged.neoforge.event.tick.ServerTickEvent.Post event) {
        if (Boolean.getBoolean("iaf.probe.clientRendering")) { ClientScene.tick(event.getServer()); return; }
        if (normalProbeQueued) {
            if (++worldTicks < 20) return;
            normalProbeQueued = false;
            run(event.getServer());
            return;
        }
        if (worldProbe == null || ++worldTicks < 20) return;
        try { System.out.println("IAF_WORLD_PROBE_PASS checks=" + worldProbe.verify(event.getServer())); }
        catch (Throwable failure) { failure.printStackTrace(); System.err.println("IAF_WORLD_PROBE_FAIL"); }
        finally { worldProbe = null; event.getServer().halt(false); }
    }
    private void check(boolean value, String description) {
        if (!value) throw new AssertionError(description);
        checks++;
    }
    private void run(MinecraftServer server) {
        try {
            ServerLevel level = server.overworld();
            if (Boolean.getBoolean("iaf.probe.roundTrip")) {
                WorldRoundTripProbe.run(level, this::check);
                System.out.println("IAF_PHASE4_PROBE_PASS checks=" + checks);
                return;
            }
            if (Boolean.getBoolean("iaf.probe.disabled")) {
                check(!zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration.enabled(zone.moddev.mc.ironagefurniture.IronAgeFurnitureConfiguration.LIGHTS), "Disabled gameplay settings loaded before resource evaluation");
                for (String id : new String[]{"chair_wood_ironage_classic_oak", "chair_wood_ironage_shield_oak", "chair_wood_ironage_wingback_oak_blue",
                        "chair_wood_ironage_throne_oak_blue", "bed_wood_foot_oak_blue", "bed_canopy_foot_lower_oak_blue", "bed_canopy_foot_left_lower_oak_blue",
                        "light_metal_ironage_sconce_floor_empty_iron", "light_metal_ironage_block_floor_lava_clear", "candles_from_tallow", "tallow_from_porkchop"})
                    check(server.getRecipeManager().byKey(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE,
                            Identifier.fromNamespaceAndPath("ironagefurniture", id))).isEmpty(), "Disabled recipes are absent: " + id);
            }
            List<ItemStack> candles = BuiltInRegistries.ITEM.stream().map(ItemStack::new).filter(CandleInventory::accepts).toList();
            check(candles.size() == 17, "All seventeen vanilla candle types");
            CandleInventory inventory = new CandleInventory();
            check(!inventory.insert(new ItemStack(Items.STICK)), "Reject non-candles");
            for (int a = 0; a < 17; a++) for (int b = 0; b < 17; b++) for (int c = 0; c < 17; c++) for (int d = 0; d < 17; d++) {
                List<ItemStack> originals = List.of(candles.get(a), candles.get(b), candles.get(c), candles.get(d));
                for (ItemStack stack : originals) check(inventory.insert(stack), "Insert mixed candle");
                check(!inventory.insert(candles.getFirst()), "Reject a fifth candle without mutation");
                CandleInventory restored = new CandleInventory();
                restored.restore(inventory.snapshot());
                for (int index = 3; index >= 0; index--)
                    check(ItemStack.isSameItemSameComponents(originals.get(index), restored.removeNewest()), "Newest-first order");
                inventory.drain();
            }
            for (boolean wall : new boolean[]{false, true}) for (Direction facing : Direction.Plane.HORIZONTAL) {
                BlockPos pos = new BlockPos(0, 100, 0);
                BlockPos support = wall ? pos.relative(facing.getOpposite()) : pos.below();
                level.setBlockAndUpdate(support, Blocks.STONE.defaultBlockState());
                CandleSconceBlock holder = wall ? PhaseFourLighting.WALL_CANDLES.get() : PhaseFourLighting.FLOOR_CANDLES.get();
                BlockState state = holder.defaultBlockState().setValue(CandleSconceBlock.DIRECTION, facing);
                level.setBlockAndUpdate(pos, state);
                check(level.getBlockEntity(pos) instanceof CandleSconceBlockEntity, "Candle storage registration");
                CandleSconceBlockEntity storage = (CandleSconceBlockEntity)level.getBlockEntity(pos);
                List<ItemStack> originals = new ArrayList<>();
                for (int index = 0; index < 4; index++) {
                    ItemStack candle = candles.get(index * 4).copy();
                    candle.set(DataComponents.CUSTOM_NAME, Component.literal("Original candle " + index));
                    originals.add(candle);
                    check(storage.insert(candle), "Store named candle");
                }
                TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, level.registryAccess());
                storage.saveWithFullMetadata(output);
                CandleSconceBlockEntity decoded = new CandleSconceBlockEntity(pos, level.getBlockState(pos));
                decoded.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), output.buildResult()));
                for (int index = 0; index < 4; index++)
                    check(ItemStack.isSameItemSameComponents(originals.get(index), decoded.candles().get(index)), "Stored components survive the real save codec");
                level.destroyBlock(pos, true);
                List<ItemEntity> drops = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(3));
                check(drops.stream().mapToInt(item -> item.getItem().getCount()).sum() == 5, "One frame plus four candle drops");
                for (ItemStack original : originals) check(drops.stream().anyMatch(item -> ItemStack.isSameItemSameComponents(original, item.getItem())), "Original candle dropped");
                drops.forEach(ItemEntity::discard);
                level.setBlockAndUpdate(support, Blocks.AIR.defaultBlockState());
            }
            for (String wood : PhaseFourFurniture.VANILLA_WOODS) for (String form : new String[]{"bed_wood_foot_", "bed_wood_foot_left_", "bed_canopy_foot_lower_", "bed_canopy_foot_left_lower_"}) {
                Block block = BuiltInRegistries.BLOCK.getValue(Identifier.parse("ironagefurniture:" + form + wood));
                check(block instanceof FurnitureBed, "Native bed registration " + form + wood);
                for (UpholsteryColour colour : UpholsteryColour.values()) {
                    ItemStack item = UpholsteryItemData.create(block, colour);
                    check(!item.isEmpty() && UpholsteryItemData.getColour(item) == colour, "Flattened bed colour identity");
                }
            }
            LightingProbe.run(level, this::check);
            zone.moddev.mc.ironagefurniture.fixture.migration.MigrationProbe.run(level, this::check);
            FurnitureProbe.run(level, this::check);
            PlayerMiningProbe.run(level, this::check);
            PlayerLightInteractionProbe.run(level, this::check);
            PlayerFurnitureInteractionProbe.run(level, this::check);
            RecipeBookProbe.run(level, this::check);
            System.out.println("IAF_PHASE4_PROBE_PASS checks=" + checks);
        } catch (Throwable failure) {
            System.err.println("IAF_PHASE4_PROBE_FAIL checks=" + checks);
            failure.printStackTrace();
        } finally { server.halt(false); }
    }
}
