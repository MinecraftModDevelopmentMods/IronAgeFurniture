package zone.moddev.mc.ironagefurniture.fixture;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
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
    }

    private void serverStarted(FMLServerStartedEvent event) {
        // The client probe loads a copied world to receive its real server tags.
        // Leave that integrated server running until the render checks finish.
        if (Boolean.getBoolean("iaf.probe.liveBaseMetalsClient") || Boolean.getBoolean("iaf.probe.inWorldClient")) return;
        MinecraftServer server = event.getServer();
        if (Boolean.getBoolean("iaf.probe.twinTorchRedstone")) {
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
        ServerWorld world = server.getLevel(DimensionType.OVERWORLD);
        FakePlayer player = FakePlayerFactory.getMinecraft(world);
        // Forge sends block-change packets even for a fake player's harvest.
        // No real client is connected to this disposable server probe.
        player.connection = new net.minecraft.network.play.ServerPlayNetHandler(server,
                new net.minecraft.network.NetworkManager(net.minecraft.network.PacketDirection.SERVERBOUND), player) {
            @Override public void send(net.minecraft.network.IPacket<?> packet) { }
        };
        int chairs = 0;
        int states = 0;
        try {
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
                    + "\ncfm_cases=" + cfm + "\nmetal_cases=" + metals + "\nchair_placements=" + placements + "\n").getBytes(StandardCharsets.UTF_8));
            org.apache.logging.log4j.LogManager.getLogger().info("IAF PHASE FOUR SHIELD PROBE PASSED: {} chairs, {} states", chairs, states);
        } catch (Exception failure) {
            throw new IllegalStateException("Phase 4 runtime probe failed", failure);
        } finally { server.halt(false); }
    }

    private static ItemStack shield() {
        ItemStack shield = new ItemStack(Items.SHIELD);
        shield.setDamageValue(23);
        shield.setHoverName(new StringTextComponent("Family Crest"));
        shield.enchant(Enchantments.UNBREAKING, 3);
        CompoundNBT banner = new CompoundNBT();
        banner.putInt("Base", 14);
        CompoundNBT pattern = new CompoundNBT();
        pattern.putString("Pattern", "cre");
        pattern.putInt("Color", 4);
        ListNBT patterns = new ListNBT();
        patterns.add(pattern);
        banner.put("Patterns", patterns);
        shield.getOrCreateTag().put("BlockEntityTag", banner);
        shield.getOrCreateTag().putString("ExtraData", "keep");
        return shield;
    }

    private static void verifyRecipe(MinecraftServer server, ShieldChair chair) {
        IRecipe<?> recipe = server.getRecipeManager().byKey(chair.getRegistryName()).orElseThrow(
                () -> new IllegalStateException("Missing shield-chair recipe " + chair.getRegistryName()));
        require(recipe instanceof zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe, "Wrong shield recipe type");
        CraftingInventory grid = new CraftingInventory(new Container(null, 0) {
            @Override public boolean stillValid(PlayerEntity player) { return false; }
        }, 3, 3);
        String classic = chair.getRegistryName().getPath().replace("_shield_", "_classic_");
        grid.setItem(0, shield());
        grid.setItem(8, new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ironagefurniture", classic))));
        zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe crafting = (zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe) recipe;
        require(crafting.matches(grid, null), "Shield recipe does not match");
        sameShield(shield(), ShieldChairItemData.getShield(crafting.assemble(grid)));
    }

    private static void verifyChair(ServerWorld world, FakePlayer player, ShieldChair chair,
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
        CompoundNBT saved = tile.save(new CompoundNBT()).copy();
        require("ironagefurniture:shield_chair".equals(saved.getString("id")), "Changed historical tile ID");
        TileEntity loaded = TileEntity.loadStatic(saved);
        require(loaded instanceof ShieldChairTileEntity, "Tile could not reload");
        sameShield(shield(), ((ShieldChairTileEntity) loaded).getShield());
        world.setBlock(pos, state.setValue(ShieldChair.WATERLOGGED, !wet), 2);
        require(world.getBlockEntity(pos) == tile, "State update replaced the shield tile");
        sameShield(shield(), ShieldChairItemData.getShield(chair.getPickBlock(state, null, world, pos, player)));
        List<ItemStack> drops = chair.getDrops(state, new LootContext.Builder(world).withParameter(LootParameters.BLOCK_ENTITY, tile));
        require(drops.size() == 2 && ShieldChairItemData.isEmptyFrame(drops.get(0)), "Incorrect frame drop");
        sameShield(shield(), drops.get(1));
        player.setShiftKeyDown(true);
        player.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
        BlockRayTraceResult hit = new BlockRayTraceResult(new Vec3d(pos), Direction.UP, pos, false);
        require(chair.use(state, world, pos, player, Hand.MAIN_HAND, hit).consumesAction(), "Could not detach shield");
        require(!tile.hasShield(), "Detached shield remained installed");
        tile.load(tile.save(new CompoundNBT()).copy());
        require(!tile.hasShield(), "Empty frame grew a shield on reload");
        player.setShiftKeyDown(false);
        player.setItemInHand(Hand.MAIN_HAND, shield());
        require(chair.use(state, world, pos, player, Hand.MAIN_HAND, hit).consumesAction(), "Could not attach shield");
        sameShield(shield(), tile.getShield());
        require(player.getMainHandItem().isEmpty(), "Survival attachment did not consume shield");

        FallingShieldChair falling = new FallingShieldChair(world, pos, state, tile);
        CompoundNBT savedEntity = new CompoundNBT();
        falling.saveAsPassenger(savedEntity);
        require("ironagefurniture:falling_shield_chair".equals(savedEntity.getString("id")), "Falling chair saved as vanilla sand");
        Entity reloadedEntity = EntityTypeRead.read(savedEntity, world);
        require(reloadedEntity instanceof FallingShieldChair, "Falling entity could not reload");
        sameShield(shield(), ShieldChairItemData.readShield(((FallingShieldChair) reloadedEntity).blockData));
        ItemEntity dropped = falling.spawnAtLocation(new ItemStack(chair), 0);
        sameShield(shield(), ShieldChairItemData.getShield(dropped.getItem()));
        dropped.remove();

        BlockPos above = pos.above(4);
        world.setBlock(above, state, 2);
        ShieldChairTileEntity airborneTile = (ShieldChairTileEntity) world.getBlockEntity(above);
        airborneTile.setShield(shield());
        FallingShieldChair landing = new FallingShieldChair(world, above, state, airborneTile);
        world.addFreshEntity(landing);
        // Tick the falling entity explicitly; this probe runs before the server's
        // regular tick loop and uses the same collision and landing code as play.
        world.removeBlock(pos, false);
        for (int step = 0; step < 100 && !landing.removed; step++) landing.tick();
        require(world.getBlockState(pos).getBlock() == chair, "Falling chair did not land");
        sameShield(shield(), ((ShieldChairTileEntity) world.getBlockEntity(pos)).getShield());

        player.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
        player.setPos(pos.getX(), pos.getY(), pos.getZ());
        require(player.gameMode.destroyBlock(pos), "Forge harvest refused the chair");
        List<ItemEntity> harvested = world.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(pos).inflate(1));
        require(harvested.stream().anyMatch(entity -> entity.getItem().getItem() == Items.SHIELD
                && entity.getItem().save(new CompoundNBT()).equals(shield().save(new CompoundNBT()))), "Real mining lost shield data");
        harvested.forEach(entity -> entity.remove());
        player.inventory.clearContent();
        world.removeBlock(pos, false);
        world.removeBlock(pos.below(), false);
    }

    private static void verifySavedChair(ServerWorld world, ShieldChair chair, BlockPos pos) {
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
        require(expected.save(new CompoundNBT()).equals(actual.save(new CompoundNBT())), "Shield data changed");
    }

    private static void verifyLavaHarvest(ServerWorld world, FakePlayer player) {
        // Clear only this probe's mining area, including drops left by a failed
        // previous test run. The server directory is always disposable.
        for (int index = 0; index < 4; index++) {
            BlockPos old = new BlockPos(64 + index * 4, 80, 96);
            world.removeBlock(old, false);
            world.removeBlock(old.below(), false);
            world.removeBlock(old.south(), false);
        }
        world.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(62, 78, 94, 80, 84, 100))
                .forEach(entity -> entity.remove());
        String[] paths = { "light_metal_ironage_block_floor_lava_clear",
                "light_metal_ironage_sconce_floor_lava_iron", "light_metal_ironage_sconce_wall_lava_iron" };
        for (String path : paths) {
            Block lamp = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ironagefurniture", path));
            boolean sconce = path.contains("sconce");
            for (int toolCase = 0; toolCase < 4; toolCase++) {
                BlockPos pos = new BlockPos(64 + toolCase * 4, 80, 96);
                world.setBlock(pos.below(), Blocks.STONE.defaultBlockState(), 2);
                world.setBlock(pos.south(), Blocks.STONE.defaultBlockState(), 2);
                BlockState state = lamp.defaultBlockState().setValue(ShieldChair.DIRECTION, Direction.NORTH)
                        .setValue(ShieldChair.WATERLOGGED, false);
                world.setBlock(pos, state, 2);
                ItemStack tool = new ItemStack(Items.DIAMOND_PICKAXE);
                if (toolCase == 1) tool.enchant(Enchantments.BLOCK_EFFICIENCY, 2);
                if (toolCase == 2) tool.enchant(Enchantments.SILK_TOUCH, 1);
                player.gameMode.setGameModeForPlayer(toolCase == 3 ? net.minecraft.world.GameType.CREATIVE : net.minecraft.world.GameType.SURVIVAL);
                player.setItemInHand(Hand.MAIN_HAND, tool);
                player.setPos(pos.getX(), pos.getY(), pos.getZ());
                require(player.gameMode.destroyBlock(pos), "Cannot mine lava light " + path);
                // Removed entities stay in chunk lists until the next world tick.
                List<ItemEntity> drops = world.getEntitiesOfClass(ItemEntity.class,
                        new AxisAlignedBB(pos).inflate(2), item -> !item.removed);
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
                require(world.getBlockState(pos).getBlock() == (toolCase < 2 ? Blocks.FIRE : Blocks.AIR),
                        "Wrong lava shatter result for " + path + " with tool " + toolCase
                        + ": " + world.getBlockState(pos));
                drops.forEach(entity -> entity.remove());
                world.removeBlock(pos, false);
                world.removeBlock(pos.below(), false);
                world.removeBlock(pos.south(), false);
            }
        }
        player.gameMode.setGameModeForPlayer(net.minecraft.world.GameType.SURVIVAL);
    }
    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }

    private static final class EntityTypeRead {
        private static Entity read(CompoundNBT tag, ServerWorld world) {
            return net.minecraft.entity.EntityType.create(tag, world).orElse(null);
        }
    }
}
