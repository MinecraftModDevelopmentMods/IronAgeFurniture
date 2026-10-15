package zone.moddev.mc.ironagefurniture.fixture;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmlserverevents.FMLServerStartedEvent;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.ironagefurniture.api.ShieldChairItemData;
import zone.moddev.mc.ironagefurniture.api.blocks.furniture.ShieldChair;
import zone.moddev.mc.ironagefurniture.api.entity.FallingShieldChair;
import zone.moddev.mc.ironagefurniture.api.tile.ShieldChairTileEntity;

/** Runs real registry, crafting, tile storage and Forge harvesting checks. */
@Mod("ironagefurniturephasefourprobe")
public final class PhaseFourRuntimeProbe {
    public PhaseFourRuntimeProbe() {
        MinecraftForge.EVENT_BUS.addListener(net.minecraftforge.eventbus.api.EventPriority.HIGHEST,
                CfmRuntimeProbe::beforeStart);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarted);
        MinecraftForge.EVENT_BUS.addListener(SavedWorldRuntimeProbe::trace);
        MinecraftForge.EVENT_BUS.addListener((net.minecraftforge.event.entity.EntityJoinWorldEvent event) -> {
            // Freeze only copied upgrade-fixture stacks before their first tick.
            // Otherwise normal item merging removes the UUID used by the audit.
            if (event.getEntity() instanceof ItemEntity) {
                ItemEntity item = (ItemEntity)event.getEntity();
                if (Boolean.getBoolean("iaf.probe.savedWorld")) item.setNeverPickUp();
                if (Boolean.getBoolean("iaf.probe.legacyMetals")) {
                    item.setNoGravity(true);
                    item.setDeltaMovement(Vec3.ZERO);
                    item.setNeverPickUp();
                }
            }
        });
    }

    private void serverStarted(FMLServerStartedEvent event) {
        // The client probe loads a copied world to receive its real server tags.
        // Leave that integrated server running until the render checks finish.
        if (Boolean.getBoolean("iaf.probe.liveBaseMetalsClient") || Boolean.getBoolean("iaf.probe.inWorldClient")) return;
        MinecraftServer server = event.getServer();
        // Entity regions load asynchronously in 1.17. Running harvesting checks
        // inside ServerStarted can query an empty entity index even after a drop
        // has been accepted. Wait for the actual fixture chunks to be ticking.
        java.util.List<BlockPos> chunks = new java.util.ArrayList<>();
        if (Boolean.getBoolean("iaf.probe.savedWorld")) {
            try { SavedWorldRuntimeProbe.loadChunks(server); }
            catch (Exception failure) { server.halt(false); throw new IllegalStateException("Could not load saved fixture chunks", failure); }
        } else {
            ServerLevel world = server.getLevel(Level.OVERWORLD);
            int[][] areas = {{4, 10, 4, 11}, {6, 67, 20, 25}, {11, 23, 10, 23},
                    {30, 39, 10, 11}, {48, 55, 12, 19}, {64, 77, 63, 75}, {100, 100, 100, 100}};
            for (int[] area : areas) for (int x = area[0]; x <= area[1]; x++)
                for (int z = area[2]; z <= area[3]; z++) {
                    BlockPos pos = new BlockPos(x * 16, 80, z * 16);
                    world.setChunkForced(x, z, true);
                    world.getChunk(pos);
                    chunks.add(pos);
                }
        }
        int[] waiting = {0};
        boolean[] started = {false};
        MinecraftForge.EVENT_BUS.addListener((net.minecraftforge.event.TickEvent.ServerTickEvent tick) -> {
            if (tick.phase != net.minecraftforge.event.TickEvent.Phase.END || started[0]) return;
            boolean ready;
            try { ready = Boolean.getBoolean("iaf.probe.savedWorld") ? SavedWorldRuntimeProbe.chunksReady(server)
                    : chunks.stream().allMatch(pos -> server.getLevel(Level.OVERWORLD).isPositionTickingWithEntitiesLoaded(pos)); }
            catch (Exception failure) { throw new IllegalStateException("Could not check fixture readiness", failure); }
            if (!ready) {
                if (++waiting[0] > 600) { server.halt(false); throw new IllegalStateException("Fixture chunks did not finish loading"); }
                return;
            }
            started[0] = true;
            runTests(server);
        });
    }

    private void runTests(MinecraftServer server) {
        if (Boolean.getBoolean("iaf.probe.sconceRedstone") || Boolean.getBoolean("iaf.probe.twinTorchRedstone")) {
            SconceRedstoneRuntimeProbe.start(server);
            return;
        }
        if (Boolean.getBoolean("iaf.probe.savedWorld")) {
            try { SavedWorldRuntimeProbe.prepare(server); }
            catch (Exception failure) { server.halt(false); throw new IllegalStateException("Could not load saved fixture chunks", failure); }
            // Compatibility benches complete at the end of the world's tick.
            // Observe the normal lifecycle instead of calling the migrator.
            int[] ticks = {0};
            MinecraftForge.EVENT_BUS.addListener(net.minecraftforge.eventbus.api.EventPriority.LOWEST,
                    (net.minecraftforge.event.TickEvent.ServerTickEvent tick) -> {
                if (tick.phase != net.minecraftforge.event.TickEvent.Phase.END || ++ticks[0] != 2) return;
                try {
                    int saved = SavedWorldRuntimeProbe.run(server);
                    Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\nsaved_world_cases="+saved+"\n").getBytes(StandardCharsets.UTF_8));
                } catch (Exception failure) { throw new IllegalStateException("Saved-world runtime probe failed", failure); }
                finally { server.halt(false); }
            });
            return;
        }
        ServerLevel world = server.getLevel(Level.OVERWORLD);
        FakePlayer player = FakePlayerFactory.getMinecraft(world);
        // Forge sends block-change packets even for a fake player's harvest.
        // No real client is connected to this disposable server probe.
        player.connection = new net.minecraft.server.network.ServerGamePacketListenerImpl(server,
                new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND), player) {
            @Override public void send(net.minecraft.network.protocol.Packet<?> packet) { }
        };
        if (Boolean.getBoolean("iaf.probe.allLighting")) {
            try {
                int direct = DirectSconceHarvestRuntimeProbe.run(world, player);
                int support = SupportLossRuntimeProbe.run(world, player);
                int buckets = CandleBucketRuntimeProbe.run(world, player);
                int lava = WallLavaMiningRuntimeProbe.run(world, player);
                Files.write(Paths.get("lighting-immediate-pass.properties"), ("status=PASS\ndirect_sconce_cases="
                        + direct + "\nsupport_loss_cases=" + support + "\ncandle_bucket_cases=" + buckets
                        + "\nwall_lava_mining_cases=" + lava + "\n").getBytes(StandardCharsets.UTF_8));
                SconceRedstoneRuntimeProbe.start(server);
            } catch (Exception failure) {
                server.halt(false);
                throw new IllegalStateException("Complete lighting regression failed", failure);
            }
            return;
        }
        int chairs = 0;
        int states = 0;
        try {
            if (Boolean.getBoolean("iaf.probe.metalsOnly")) {
                int cases = SconceMetalRuntimeProbe.run(world, player);
                Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\nmetal_cases=" + cases + "\n")
                        .getBytes(StandardCharsets.UTF_8));
                return;
            }
            if (Boolean.getBoolean("iaf.probe.candleBuckets")) {
                int cases = CandleBucketRuntimeProbe.run(world, player);
                Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\ncandle_bucket_cases=" + cases + "\n").getBytes(StandardCharsets.UTF_8));
                return;
            }
            if (Boolean.getBoolean("iaf.probe.wallLavaMiningOnly")) {
                int cases = WallLavaMiningRuntimeProbe.run(world, player);
                Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\nwall_lava_mining_cases=" + cases + "\n").getBytes(StandardCharsets.UTF_8));
                return;
            }
            if (Boolean.getBoolean("iaf.probe.furnitureFireOnly")) {
                int burns = FurnitureFireRuntimeProbe.run(world);
                Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\nfurniture_fire_cases=" + burns + "\n").getBytes(StandardCharsets.UTF_8));
                return;
            }
            if (Boolean.getBoolean("iaf.probe.directSconceOnly")) {
                int harvests = DirectSconceHarvestRuntimeProbe.run(world, player);
                Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\ndirect_sconce_cases=" + harvests + "\n").getBytes(StandardCharsets.UTF_8));
                return;
            }
            if (Boolean.getBoolean("iaf.probe.supportLossOnly")) {
                int support = SupportLossRuntimeProbe.run(world, player);
                Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\nsupport_loss_cases=" + support + "\n").getBytes(StandardCharsets.UTF_8));
                return;
            }
            if (Boolean.getBoolean("iaf.probe.chairPlacementOnly")) {
                int placements = ChairPlacementRuntimeProbe.run(world, player);
                Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\nchair_placements=" + placements + "\n").getBytes(StandardCharsets.UTF_8));
                return;
            }
            if (System.getProperty("iaf.probe.taggedMode") != null) {
                int cases = TaggedItemMigrationProbe.run(world, "seed".equals(System.getProperty("iaf.probe.taggedMode")));
                Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\ntagged_cases=" + cases + "\n").getBytes(StandardCharsets.UTF_8));
                return;
            }
            if (Boolean.getBoolean("iaf.probe.legacyMetals")) {
                int legacy = LegacyMetalRuntimeProbe.run(world);
                Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\nlegacy_metal_cases="+legacy+"\n").getBytes(StandardCharsets.UTF_8));
                return;
            }
            int creativeItems = CreativeTabsRuntimeProbe.run();
            if (Boolean.getBoolean("iaf.probe.tabsOnly")) {
                Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\ncreative_items=" + creativeItems + "\n").getBytes(StandardCharsets.UTF_8));
                return;
            }
            int cfm = CfmRuntimeProbe.run(server, world);
            if (!"none".equals(System.getProperty("iaf.probe.cfmMode", "none"))) {
                Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\ncfm_cases=" + cfm
                        + "\ncfm_mode=" + System.getProperty("iaf.probe.cfmMode") + "\n").getBytes(StandardCharsets.UTF_8));
                return;
            }
            if (Boolean.getBoolean("iaf.probe.legacyBeds")) UpholsteryRuntimeProbe.verifyLegacyBeds(world);
            for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                if (!(block instanceof ShieldChair)) continue;
                ShieldChair chair = (ShieldChair) block;
                verifySavedChair(world, chair, new BlockPos(64 + chairs * 4, 80, 80));
                for (Direction facing : Direction.Plane.HORIZONTAL) {
                    for (boolean wet : new boolean[] { false, true }) {
                        verifyChair(world, player, chair, new BlockPos(64 + chairs * 4, 80, 64), facing, wet);
                        states++;
                    }
                }
                verifyRecipe(server, chair);
                chairs++;
            }
            require(chairs >= 6, "Vanilla shield chairs did not register");
            verifyLavaHarvest(world, player);
            int wallMining = WallLavaMiningRuntimeProbe.run(world, player);
            int placements = ChairPlacementRuntimeProbe.run(world, player);
            int upholstery = UpholsteryRuntimeProbe.run(server, world, player);
            int candles = CandleRuntimeProbe.run(world, player);
            int sconces = SconceContentsRuntimeProbe.run(world, player);
            int rockSalt = RockSaltRuntimeProbe.run(world, player);
            int traps = LavaTrapRuntimeProbe.run(world, player);
            int metals = SconceMetalRuntimeProbe.run(world, player);
            Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\nshield_chairs=" + chairs
                    + "\nshield_states=" + states + "\nupholstered_forms=" + upholstery + "\ncandle_cases=" + candles
                    + "\nsconce_cases=" + sconces + "\nrocksalt_cases=" + rockSalt + "\ntrap_cases=" + traps
                    + "\ncfm_cases=" + cfm + "\nmetal_cases=" + metals + "\nchair_placements=" + placements
                    + "\nwall_lava_mining_cases=" + wallMining + "\n").getBytes(StandardCharsets.UTF_8));
            org.apache.logging.log4j.LogManager.getLogger().info("IAF PHASE FOUR SHIELD PROBE PASSED: {} chairs, {} states", chairs, states);
        } catch (Exception failure) {
            throw new IllegalStateException("Phase 4 runtime probe failed", failure);
        } finally { server.halt(false); }
    }

    private static ItemStack shield() {
        ItemStack shield = new ItemStack(Items.SHIELD);
        shield.setDamageValue(23);
        shield.setHoverName(new TextComponent("Family Crest"));
        shield.enchant(Enchantments.UNBREAKING, 3);
        CompoundTag banner = new CompoundTag();
        banner.putInt("Base", 14);
        CompoundTag pattern = new CompoundTag();
        pattern.putString("Pattern", "cre");
        pattern.putInt("Color", 4);
        ListTag patterns = new ListTag();
        patterns.add(pattern);
        banner.put("Patterns", patterns);
        shield.getOrCreateTag().put("BlockEntityTag", banner);
        shield.getOrCreateTag().putString("ExtraData", "keep");
        return shield;
    }

    private static void verifyRecipe(MinecraftServer server, ShieldChair chair) {
        Recipe<?> recipe = server.getRecipeManager().byKey(chair.getRegistryName()).orElseThrow(
                () -> new IllegalStateException("Missing shield-chair recipe " + chair.getRegistryName()));
        require(recipe instanceof zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe, "Wrong shield recipe type");
        CraftingContainer grid = new CraftingContainer(new AbstractContainerMenu(null, 0) {
            @Override public boolean stillValid(Player player) { return false; }
        }, 3, 3);
        String classic = chair.getRegistryName().getPath().replace("_shield_", "_classic_");
        grid.setItem(0, shield());
        grid.setItem(8, new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ironagefurniture", classic))));
        zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe crafting = (zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe) recipe;
        require(crafting.matches(grid, null), "Shield recipe does not match");
        sameShield(shield(), ShieldChairItemData.getShield(crafting.assemble(grid)));
    }

    private static void verifyChair(ServerLevel world, FakePlayer player, ShieldChair chair,
            BlockPos pos, Direction facing, boolean wet) {
        world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
        BlockState state = chair.defaultBlockState().setValue(ShieldChair.DIRECTION, facing).setValue(ShieldChair.WATERLOGGED, wet);
        world.setBlock(pos, state, 2);
        require(world.getBlockEntity(pos) instanceof ShieldChairTileEntity, "No shield tile");
        ShieldChairTileEntity tile = (ShieldChairTileEntity) world.getBlockEntity(pos);
        require(tile.getShield().getItem() == Items.SHIELD, "Legacy chair lost its default shield");
        ItemStack item = ShieldChairItemData.createChair(chair, shield());
        chair.setPlacedBy(world, pos, state, player, item);
        sameShield(shield(), tile.getShield());
        CompoundTag saved = tile.save(new CompoundTag()).copy();
        require("ironagefurniture:shield_chair".equals(saved.getString("id")), "Changed historical tile ID");
        BlockEntity loaded = BlockEntity.loadStatic(pos, state, saved);
        require(loaded instanceof ShieldChairTileEntity, "Tile could not reload");
        sameShield(shield(), ((ShieldChairTileEntity) loaded).getShield());
        world.setBlock(pos, state.setValue(ShieldChair.WATERLOGGED, !wet), 2);
        require(world.getBlockEntity(pos) == tile, "State update replaced the shield tile");
        sameShield(shield(), ShieldChairItemData.getShield(chair.getPickBlock(state, null, world, pos, player)));
        List<ItemStack> drops = chair.getDrops(state, new LootContext.Builder(world).withParameter(LootContextParams.BLOCK_ENTITY, tile));
        require(drops.size() == 2 && ShieldChairItemData.isEmptyFrame(drops.get(0)), "Incorrect frame drop");
        sameShield(shield(), drops.get(1));
        player.setShiftKeyDown(true);
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        BlockHitResult hit = new BlockHitResult(Vec3.atLowerCornerOf(pos), Direction.UP, pos, false);
        require(chair.use(state, world, pos, player, InteractionHand.MAIN_HAND, hit).consumesAction(), "Could not detach shield");
        require(!tile.hasShield(), "Detached shield remained installed");
        tile.load(tile.save(new CompoundTag()).copy());
        require(!tile.hasShield(), "Empty frame grew a shield on reload");
        player.setShiftKeyDown(false);
        player.setItemInHand(InteractionHand.MAIN_HAND, shield());
        require(chair.use(state, world, pos, player, InteractionHand.MAIN_HAND, hit).consumesAction(), "Could not attach shield");
        sameShield(shield(), tile.getShield());
        require(player.getMainHandItem().isEmpty(), "Survival attachment did not consume shield");

        FallingShieldChair falling = new FallingShieldChair(world, pos, state, tile);
        CompoundTag savedEntity = new CompoundTag();
        falling.saveAsPassenger(savedEntity);
        require("ironagefurniture:falling_shield_chair".equals(savedEntity.getString("id")), "Falling chair saved as vanilla sand");
        Entity reloadedEntity = EntityTypeRead.read(savedEntity, world);
        require(reloadedEntity instanceof FallingShieldChair, "Falling entity could not reload");
        sameShield(shield(), ShieldChairItemData.readShield(((FallingShieldChair) reloadedEntity).blockData));
        ItemEntity dropped = falling.spawnAtLocation(new ItemStack(chair), 0);
        sameShield(shield(), ShieldChairItemData.getShield(dropped.getItem()));
        dropped.discard();

        BlockPos above = pos.above(4);
        // A fresh terrain generator may put rock between the two test heights.
        // Clear the flight path, not just the source and intended landing cell.
        for (int height = 1; height < 4; height++) world.setBlock(pos.above(height), Blocks.AIR.defaultBlockState(), 2);
        world.setBlock(above, state, 2);
        ShieldChairTileEntity airborneTile = (ShieldChairTileEntity) world.getBlockEntity(above);
        airborneTile.setShield(shield());
        FallingShieldChair landing = new FallingShieldChair(world, above, state, airborneTile);
        world.addFreshEntity(landing);
        // Tick the falling entity explicitly; this probe runs before the server's
        // regular tick loop and uses the same collision and landing code as play.
        world.setBlock(pos, wet ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 2);
        for (int step = 0; step < 100 && !landing.isRemoved(); step++) landing.tick();
        require(world.getBlockState(pos).getBlock() == chair, "Falling chair did not land: " + chair.getRegistryName()
                + "/" + facing + "/wet=" + wet + "/entity=" + landing.position()
                + "/removed=" + landing.isRemoved() + "/state=" + world.getBlockState(pos));
        sameShield(shield(), ((ShieldChairTileEntity) world.getBlockEntity(pos)).getShield());

        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
        player.setPos(pos.getX(), pos.getY(), pos.getZ());
        require(player.gameMode.destroyBlock(pos), "Forge harvest refused the chair");
        List<ItemEntity> harvested = world.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(1));
        require(harvested.stream().anyMatch(entity -> entity.getItem().getItem() == Items.SHIELD
                && entity.getItem().save(new CompoundTag()).equals(shield().save(new CompoundTag()))), "Real mining lost shield data");
        harvested.forEach(entity -> entity.discard());
        player.getInventory().clearContent();
        world.removeBlock(pos, false);
        world.removeBlock(pos.below(), false);
    }

    private static void verifySavedChair(ServerLevel world, ShieldChair chair, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() == chair) {
            sameShield(shield(), ((ShieldChairTileEntity) world.getBlockEntity(pos)).getShield());
            require(!((ShieldChairTileEntity) world.getBlockEntity(pos.east())).hasShield(), "Saved empty frame gained a shield");
        }
        world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
        world.setBlock(pos.east().below(), Blocks.STONE.defaultBlockState(), 2);
        world.setBlock(pos, chair.defaultBlockState().setValue(ShieldChair.DIRECTION, Direction.EAST), 2);
        world.setBlock(pos.east(), chair.defaultBlockState().setValue(ShieldChair.DIRECTION, Direction.WEST), 2);
        ((ShieldChairTileEntity) world.getBlockEntity(pos)).setShield(shield());
        ((ShieldChairTileEntity) world.getBlockEntity(pos.east())).setShield(ItemStack.EMPTY);
    }

    private static void sameShield(ItemStack expected, ItemStack actual) {
        require(expected.save(new CompoundTag()).equals(actual.save(new CompoundTag())), "Shield data changed");
    }

    private static void verifyLavaHarvest(ServerLevel world, FakePlayer player) {
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        player.removeAllEffects();
        player.setOnGround(true);
        for (Block lamp : ForgeRegistries.BLOCKS.getValues()) {
            String path = lamp.getRegistryName().getPath();
            if (!"ironagefurniture".equals(lamp.getRegistryName().getNamespace())
                    || !path.startsWith("light_metal_ironage_block_floor_")
                    || !(path.contains("_lava_") || path.contains("_glow_") || path.contains("_red_"))) continue;
            BlockState state = lamp.defaultBlockState();
            BlockPos pos = new BlockPos(64, 80, 96);
            require(state.getMaterial() == net.minecraft.world.level.material.Material.GLASS,
                    "Standalone vial still uses metal material: " + path);
            require(Math.abs(state.getDestroySpeed(world, pos) - .3F) < .00001F,
                    "Standalone vial does not use glass hardness: " + path);
            require(state.getDestroyProgress(player, world, pos) >= .1F,
                    "Standalone vial takes more than ten bare-hand swings: " + path);
        }
        // Clear only this probe's mining area, including drops left by a failed
        // previous test run. The server directory is always disposable.
        for (int index = 0; index < 5; index++) {
            BlockPos old = new BlockPos(64 + index * 4, 80, 96);
            world.removeBlock(old, false);
            world.removeBlock(old.below(), false);
            world.removeBlock(old.south(), false);
        }
        world.getEntitiesOfClass(ItemEntity.class, new AABB(62, 78, 94, 84, 84, 100))
                .forEach(entity -> entity.discard());
        String[] paths = { "light_metal_ironage_block_floor_lava_clear",
                "light_metal_ironage_sconce_floor_lava_iron", "light_metal_ironage_sconce_wall_lava_iron" };
        for (String path : paths) {
            Block lamp = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ironagefurniture", path));
            boolean sconce = path.contains("sconce");
            for (int toolCase = 0; toolCase < 5; toolCase++) {
                BlockPos pos = new BlockPos(64 + toolCase * 4, 80, 96);
                world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
                world.setBlock(pos.south(), Blocks.STONE.defaultBlockState(), 2);
                BlockState state = lamp.defaultBlockState().setValue(ShieldChair.DIRECTION, Direction.NORTH)
                        .setValue(ShieldChair.WATERLOGGED, false);
                world.setBlock(pos, state, 2);
                ItemStack tool = toolCase == 4 ? ItemStack.EMPTY : new ItemStack(Items.DIAMOND_PICKAXE);
                if (toolCase == 1) tool.enchant(Enchantments.BLOCK_EFFICIENCY, 2);
                if (toolCase == 2) tool.enchant(Enchantments.SILK_TOUCH, 1);
                player.setGameMode(toolCase == 3 ? net.minecraft.world.level.GameType.CREATIVE : net.minecraft.world.level.GameType.SURVIVAL);
                player.setItemInHand(InteractionHand.MAIN_HAND, tool);
                player.setPos(pos.getX(), pos.getY(), pos.getZ());
                require(player.gameMode.destroyBlock(pos), "Cannot mine lava light " + path);
                WallLavaMiningRuntimeProbe.settleReleased(world, pos);
                // Removed entities stay in chunk lists until the next world tick.
                List<ItemEntity> drops = world.getEntitiesOfClass(ItemEntity.class,
                        new AABB(pos).inflate(2), item -> !item.isRemoved());
                long intact = drops.stream().filter(item -> item.getItem().getItem() == ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("ironagefurniture:light_metal_ironage_block_floor_lava_clear")))
                        .mapToLong(item -> item.getItem().getCount()).sum();
                long frames = drops.stream().filter(item -> item.getItem().getItem() == ForgeRegistries.ITEMS.getValue(
                        new ResourceLocation("ironagefurniture:light_metal_ironage_sconce_floor_empty_iron")))
                        .mapToLong(item -> item.getItem().getCount()).sum();
                require(intact == (toolCase == 2 ? 1 : 0), "Wrong intact-lamp drop count for " + path
                        + ", tool " + toolCase + ": " + intact + ", drops " + drops.stream()
                        .map(item -> item.getItem().toString()).collect(java.util.stream.Collectors.joining(", ")));
                require(frames == (sconce && toolCase != 3 ? 1 : 0), "Wrong sconce drop count for " + path);
                require(world.getBlockState(pos).getBlock() == (toolCase < 2 || toolCase == 4 ? Blocks.FIRE : Blocks.AIR),
                        "Wrong lava shatter result for " + path + " with tool " + toolCase
                        + ": " + world.getBlockState(pos));
                drops.forEach(entity -> entity.discard());
                world.removeBlock(pos, false);
                world.removeBlock(pos.below(), false);
                world.removeBlock(pos.south(), false);
            }
        }
        player.setGameMode(net.minecraft.world.level.GameType.SURVIVAL);
    }
    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }

    private static final class EntityTypeRead {
        private static Entity read(CompoundTag tag, ServerLevel world) {
            return net.minecraft.world.entity.EntityType.create(tag, world).orElse(null);
        }
    }
}
