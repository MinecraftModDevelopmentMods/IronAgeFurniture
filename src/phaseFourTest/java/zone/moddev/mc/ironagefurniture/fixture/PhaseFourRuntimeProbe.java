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
    public PhaseFourRuntimeProbe() { MinecraftForge.EVENT_BUS.addListener(this::serverStarted); }

    private void serverStarted(FMLServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        ServerWorld world = server.getWorld(DimensionType.OVERWORLD);
        FakePlayer player = FakePlayerFactory.getMinecraft(world);
        // Forge sends block-change packets even for a fake player's harvest.
        // No real client is connected to this disposable server probe.
        player.connection = new net.minecraft.network.play.ServerPlayNetHandler(server,
                new net.minecraft.network.NetworkManager(net.minecraft.network.PacketDirection.SERVERBOUND), player) {
            @Override public void sendPacket(net.minecraft.network.IPacket<?> packet) { }
        };
        int chairs = 0;
        int states = 0;
        try {
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
            int upholstery = UpholsteryRuntimeProbe.run(server, world, player);
            Files.write(Paths.get("phase-four-pass.properties"), ("status=PASS\nshield_chairs=" + chairs
                    + "\nshield_states=" + states + "\nupholstered_forms=" + upholstery + "\n").getBytes(StandardCharsets.UTF_8));
            org.apache.logging.log4j.LogManager.getLogger().info("IAF PHASE FOUR SHIELD PROBE PASSED: {} chairs, {} states", chairs, states);
        } catch (Exception failure) {
            throw new IllegalStateException("Phase 4 runtime probe failed", failure);
        } finally { server.initiateShutdown(false); }
    }

    private static ItemStack shield() {
        ItemStack shield = new ItemStack(Items.SHIELD);
        shield.setDamage(23);
        shield.setDisplayName(new StringTextComponent("Family Crest"));
        shield.addEnchantment(Enchantments.UNBREAKING, 3);
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
        IRecipe<?> recipe = server.getRecipeManager().getRecipe(chair.getRegistryName()).orElseThrow(
                () -> new IllegalStateException("Missing shield-chair recipe " + chair.getRegistryName()));
        require(recipe instanceof zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe, "Wrong shield recipe type");
        CraftingInventory grid = new CraftingInventory(new Container(null, 0) {
            @Override public boolean canInteractWith(PlayerEntity player) { return false; }
        }, 3, 3);
        String classic = chair.getRegistryName().getPath().replace("_shield_", "_classic_");
        grid.setInventorySlotContents(0, shield());
        grid.setInventorySlotContents(8, new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ironagefurniture", classic))));
        zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe crafting = (zone.moddev.mc.ironagefurniture.api.recipes.ShieldChairRecipe) recipe;
        require(crafting.matches(grid, null), "Shield recipe does not match");
        sameShield(shield(), ShieldChairItemData.getShield(crafting.getCraftingResult(grid)));
    }

    private static void verifyChair(ServerWorld world, FakePlayer player, ShieldChair chair,
            BlockPos pos, Direction facing, boolean wet) {
        world.setBlockState(pos.down(), Blocks.STONE.getDefaultState());
        BlockState state = chair.getDefaultState().with(ShieldChair.DIRECTION, facing).with(ShieldChair.WATERLOGGED, wet);
        world.setBlockState(pos, state);
        require(world.getTileEntity(pos) instanceof ShieldChairTileEntity, "No shield tile");
        ShieldChairTileEntity tile = (ShieldChairTileEntity) world.getTileEntity(pos);
        require(tile.getShield().getItem() == Items.SHIELD, "Legacy chair lost its default shield");
        ItemStack item = ShieldChairItemData.createChair(chair, shield());
        chair.onBlockPlacedBy(world, pos, state, player, item);
        sameShield(shield(), tile.getShield());
        CompoundNBT saved = tile.write(new CompoundNBT()).copy();
        require("ironagefurniture:shield_chair".equals(saved.getString("id")), "Changed historical tile ID");
        TileEntity loaded = TileEntity.create(saved);
        require(loaded instanceof ShieldChairTileEntity, "Tile could not reload");
        sameShield(shield(), ((ShieldChairTileEntity) loaded).getShield());
        world.setBlockState(pos, state.with(ShieldChair.WATERLOGGED, !wet));
        require(world.getTileEntity(pos) == tile, "State update replaced the shield tile");
        sameShield(shield(), ShieldChairItemData.getShield(chair.getPickBlock(state, null, world, pos, player)));
        List<ItemStack> drops = chair.getDrops(state, new LootContext.Builder(world).withParameter(LootParameters.BLOCK_ENTITY, tile));
        require(drops.size() == 2 && ShieldChairItemData.isEmptyFrame(drops.get(0)), "Incorrect frame drop");
        sameShield(shield(), drops.get(1));
        player.setSneaking(true);
        player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
        BlockRayTraceResult hit = new BlockRayTraceResult(new Vec3d(pos), Direction.UP, pos, false);
        require(chair.onBlockActivated(state, world, pos, player, Hand.MAIN_HAND, hit), "Could not detach shield");
        require(!tile.hasShield(), "Detached shield remained installed");
        tile.read(tile.write(new CompoundNBT()).copy());
        require(!tile.hasShield(), "Empty frame grew a shield on reload");
        player.setSneaking(false);
        player.setHeldItem(Hand.MAIN_HAND, shield());
        require(chair.onBlockActivated(state, world, pos, player, Hand.MAIN_HAND, hit), "Could not attach shield");
        sameShield(shield(), tile.getShield());
        require(player.getHeldItemMainhand().isEmpty(), "Survival attachment did not consume shield");

        FallingShieldChair falling = new FallingShieldChair(world, pos, state, tile);
        CompoundNBT savedEntity = new CompoundNBT();
        falling.writeUnlessRemoved(savedEntity);
        require("ironagefurniture:falling_shield_chair".equals(savedEntity.getString("id")), "Falling chair saved as vanilla sand");
        Entity reloadedEntity = EntityTypeRead.read(savedEntity, world);
        require(reloadedEntity instanceof FallingShieldChair, "Falling entity could not reload");
        sameShield(shield(), ShieldChairItemData.readShield(((FallingShieldChair) reloadedEntity).tileEntityData));
        ItemEntity dropped = falling.entityDropItem(new ItemStack(chair), 0);
        sameShield(shield(), ShieldChairItemData.getShield(dropped.getItem()));
        dropped.remove();

        BlockPos above = pos.up(4);
        world.setBlockState(above, state);
        ShieldChairTileEntity airborneTile = (ShieldChairTileEntity) world.getTileEntity(above);
        airborneTile.setShield(shield());
        FallingShieldChair landing = new FallingShieldChair(world, above, state, airborneTile);
        world.addEntity(landing);
        // Tick the falling entity explicitly; this probe runs before the server's
        // regular tick loop and uses the same collision and landing code as play.
        world.removeBlock(pos, false);
        for (int step = 0; step < 100 && !landing.removed; step++) landing.tick();
        require(world.getBlockState(pos).getBlock() == chair, "Falling chair did not land");
        sameShield(shield(), ((ShieldChairTileEntity) world.getTileEntity(pos)).getShield());

        player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
        player.setPosition(pos.getX(), pos.getY(), pos.getZ());
        require(player.interactionManager.tryHarvestBlock(pos), "Forge harvest refused the chair");
        List<ItemEntity> harvested = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).grow(1));
        require(harvested.stream().anyMatch(entity -> entity.getItem().getItem() == Items.SHIELD
                && entity.getItem().write(new CompoundNBT()).equals(shield().write(new CompoundNBT()))), "Real mining lost shield data");
        harvested.forEach(Entity::remove);
        player.inventory.clear();
        world.removeBlock(pos, false);
        world.removeBlock(pos.down(), false);
    }

    private static void verifySavedChair(ServerWorld world, ShieldChair chair, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() == chair) {
            sameShield(shield(), ((ShieldChairTileEntity) world.getTileEntity(pos)).getShield());
            require(!((ShieldChairTileEntity) world.getTileEntity(pos.east())).hasShield(), "Saved empty frame gained a shield");
        }
        world.setBlockState(pos.down(), Blocks.STONE.getDefaultState());
        world.setBlockState(pos.east().down(), Blocks.STONE.getDefaultState());
        world.setBlockState(pos, chair.getDefaultState().with(ShieldChair.DIRECTION, Direction.EAST));
        world.setBlockState(pos.east(), chair.getDefaultState().with(ShieldChair.DIRECTION, Direction.WEST));
        ((ShieldChairTileEntity) world.getTileEntity(pos)).setShield(shield());
        ((ShieldChairTileEntity) world.getTileEntity(pos.east())).setShield(ItemStack.EMPTY);
    }

    private static void sameShield(ItemStack expected, ItemStack actual) {
        require(expected.write(new CompoundNBT()).equals(actual.write(new CompoundNBT())), "Shield data changed");
    }

    private static void verifyLavaHarvest(ServerWorld world, FakePlayer player) {
        // Clear only this probe's mining area, including drops left by a failed
        // previous test run. The server directory is always disposable.
        for (int index = 0; index < 4; index++) {
            BlockPos old = new BlockPos(64 + index * 4, 80, 96);
            world.removeBlock(old, false);
            world.removeBlock(old.down(), false);
            world.removeBlock(old.south(), false);
        }
        world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(62, 78, 94, 80, 84, 100))
                .forEach(Entity::remove);
        String[] paths = { "light_metal_ironage_block_floor_lava_clear",
                "light_metal_ironage_sconce_floor_lava_iron", "light_metal_ironage_sconce_wall_lava_iron" };
        for (String path : paths) {
            Block lamp = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("ironagefurniture", path));
            boolean sconce = path.contains("sconce");
            for (int toolCase = 0; toolCase < 4; toolCase++) {
                BlockPos pos = new BlockPos(64 + toolCase * 4, 80, 96);
                world.setBlockState(pos.down(), Blocks.STONE.getDefaultState());
                world.setBlockState(pos.south(), Blocks.STONE.getDefaultState());
                BlockState state = lamp.getDefaultState().with(ShieldChair.DIRECTION, Direction.NORTH)
                        .with(ShieldChair.WATERLOGGED, false);
                world.setBlockState(pos, state);
                ItemStack tool = new ItemStack(Items.DIAMOND_PICKAXE);
                if (toolCase == 1) tool.addEnchantment(Enchantments.EFFICIENCY, 2);
                if (toolCase == 2) tool.addEnchantment(Enchantments.SILK_TOUCH, 1);
                player.interactionManager.setGameType(toolCase == 3 ? net.minecraft.world.GameType.CREATIVE : net.minecraft.world.GameType.SURVIVAL);
                player.setHeldItem(Hand.MAIN_HAND, tool);
                player.setPosition(pos.getX(), pos.getY(), pos.getZ());
                require(player.interactionManager.tryHarvestBlock(pos), "Cannot mine lava light " + path);
                // Removed entities stay in chunk lists until the next world tick.
                List<ItemEntity> drops = world.getEntitiesWithinAABB(ItemEntity.class,
                        new AxisAlignedBB(pos).grow(2), item -> !item.removed);
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
                drops.forEach(Entity::remove);
                world.removeBlock(pos, false);
                world.removeBlock(pos.down(), false);
                world.removeBlock(pos.south(), false);
            }
        }
        player.interactionManager.setGameType(net.minecraft.world.GameType.SURVIVAL);
    }
    private static void require(boolean passed, String message) { if (!passed) throw new IllegalStateException(message); }

    private static final class EntityTypeRead {
        private static Entity read(CompoundNBT tag, ServerWorld world) {
            return net.minecraft.entity.EntityType.loadEntityUnchecked(tag, world).orElse(null);
        }
    }
}
